package com.ecfront.fs.transfer

import java.io._

import com.ecfront.fs.operation.FTPOperation
import org.apache.commons.net.ftp.FTPClient


class FTPToLocalTransfer(ftpClient: FTPClient) extends FSTransfer {

  override protected def _transfer(sourcePath: String, targetPath: String): Boolean = {
    val outfile: File = new File(targetPath)
    var oStream: OutputStream = null
    try {
      oStream = new FileOutputStream(outfile)
      return ftpClient.retrieveFile(sourcePath, oStream)
    }
    catch {
      case e: IOException =>
        logger.error("Download error.", e)
        return false
    } finally oStream.close
  }
}


object FTPToLocalTransfer {
  def apply(ftpClient: FTPClient) = new FTPToLocalTransfer(ftpClient)

  def apply(host: String, port: Int, userName: String, password: String) = {
    new FTPToLocalTransfer(FTPOperation(host, port, userName, password).getFTPClient())
  }
}
