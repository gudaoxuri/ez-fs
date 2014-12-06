package com.ecfront.fs.transfer

import java.io._

import com.ecfront.fs.operation.FTPOperation
import org.apache.commons.net.ftp.FTPClient


class LocalToFTPTransfer(ftpClient: FTPClient) extends FSTransfer {

  /**
   *
   * @param sourcePath 只能是文件不能是目录
   * @param targetPath 可指定新的文件名
   * @return
   */
  override protected def _transfer(sourcePath: String, targetPath: String): Boolean = {
    var iStream: InputStream = null
    try {
      iStream = new FileInputStream(sourcePath)
      ftpClient.storeFile(targetPath, iStream)
    }
    catch {
      case e: IOException =>
        logger.error("Upload error.", e)
        false
    } finally {
      if (iStream != null) {
        iStream.close()
      }
    }
  }
}

object LocalToFTPTransfer {
  def apply(ftpClient: FTPClient) = new LocalToFTPTransfer(ftpClient)

  def apply(host: String, port: Int, userName: String, password: String) = {
    new LocalToFTPTransfer(FTPOperation(host, port, userName, password).getFTPClient)
  }
}
