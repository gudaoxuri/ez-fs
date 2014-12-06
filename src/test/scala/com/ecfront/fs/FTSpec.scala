package com.ecfront.fs

import com.ecfront.fs.operation.{FSOperation, FTPOperation, HDFSOperation, LocalOperation}
import com.ecfront.fs.transfer._
import org.scalatest.FunSuite

class FTSpec extends FunSuite {

  test("传输测试") {

    var localPath = this.getClass.getResource("/").getPath
    if (System.getProperties.getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1) {
      localPath = localPath.substring(1)
    }
    val ftpPath = "/"
    val hdfsPath = "/test/"

    val localFo = LocalOperation()
    val ftpFo = FTPOperation("192.168.1.123", 21, "sa", "sa")
    val dhfsFo = HDFSOperation()
    val ftpClient = ftpFo.getFTPClient

    transfer(localPath, ftpPath, LocalToFTPTransfer(ftpClient), ftpFo)
    transfer(localPath, hdfsPath, LocalToHDFSTransfer(), dhfsFo)

    transfer(ftpPath, localPath, FTPToLocalTransfer(ftpClient), localFo)
    transfer(ftpPath, hdfsPath, FTPToHDFSTransfer(ftpClient), dhfsFo)

    transfer(hdfsPath, localPath, HDFSToLocalTransfer(), localFo)

  }

  def transfer(sourcePath: String, targetPath: String, ft: FSTransfer, fo: FSOperation): Unit = {
    ft.transfer(sourcePath + "b.txt", targetPath + "b.txt")
    assert(fo.existFile(targetPath + "b.txt"))
    //FileNotFoundException  Permission denied
    ft.transfer(sourcePath + "testdir", targetPath + "testdir")
    assert(fo.existFile(targetPath + "testdir/1.txt"))
  }

}
