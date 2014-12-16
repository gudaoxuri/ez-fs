package com.ecfront.fs.transfer

import java.io.{IOException, InputStream}

import com.ecfront.fs.operation.FTPOperation
import org.apache.commons.net.ftp.FTPClient
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}


class HDFSToFTPTransfer(ftpClient: FTPClient, hdfsConf: Configuration) extends FSTransfer {

  override protected def _transfer(sourcePath: String, targetPath: String): Boolean = {
    val fPath = if (targetPath.startsWith("/")) targetPath.substring(1) else targetPath
    var iStream: InputStream = null
    try {
      iStream = FileSystem.get(hdfsConf).open(new Path(sourcePath))
      ftpClient.storeFile(fPath, iStream)
      true
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

object HDFSToFTPTransfer {
  def apply(ftpClient: FTPClient) = new HDFSToFTPTransfer(ftpClient, new Configuration)

  def apply(ftpClient: FTPClient, hdfsConf: Configuration) = new HDFSToFTPTransfer(ftpClient, hdfsConf)

  def apply(host: String, port: Int, userName: String, password: String) = {
    new HDFSToFTPTransfer(FTPOperation(host, port, userName, password).getFTPClient, new Configuration)
  }

  def apply(host: String, port: Int, userName: String, password: String, hdfsConf: Configuration) = {
    new HDFSToFTPTransfer(FTPOperation(host, port, userName, password).getFTPClient, hdfsConf)
  }
}


