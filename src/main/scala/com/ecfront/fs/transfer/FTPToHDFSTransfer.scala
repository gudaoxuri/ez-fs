package com.ecfront.fs.transfer

import com.ecfront.fs.operation.FTPOperation
import org.apache.commons.net.ftp.{FTPClient, FTPFile}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FSDataOutputStream, FileSystem, Path}


class FTPToHDFSTransfer(ftpClient: FTPClient, hdfsConf: Configuration) extends FSTransfer {

  override protected def _transfer(sourcePath: String, targetPath: String): Boolean = {
    val tFtpFiles: Array[FTPFile] = ftpClient.listFiles(sourcePath)
    if (null == tFtpFiles || tFtpFiles.length == 0) {
      false
    }else{
      val outputStream: FSDataOutputStream = FileSystem.get(hdfsConf).create(new Path(targetPath))
      ftpClient.retrieveFile(sourcePath, outputStream)
      outputStream.close()
      true
    }
  }

}

object FTPToHDFSTransfer {
  def apply(ftpClient: FTPClient) = new FTPToHDFSTransfer(ftpClient, new Configuration)

  def apply(ftpClient: FTPClient, hdfsConf: Configuration) = new FTPToHDFSTransfer(ftpClient, hdfsConf)

  def apply(host: String, port: Int, userName: String, password: String) = {
    new FTPToHDFSTransfer(FTPOperation(host, port, userName, password).getFTPClient, new Configuration)
  }

  def apply(host: String, port: Int, userName: String, password: String, hdfsConf: Configuration) = {
    new FTPToHDFSTransfer(FTPOperation(host, port, userName, password).getFTPClient, hdfsConf)
  }
}
