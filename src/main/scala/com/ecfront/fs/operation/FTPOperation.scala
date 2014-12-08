package com.ecfront.fs.operation

import org.apache.commons.net.ftp.{FTP, FTPClient, FTPReply}

import scala.collection.mutable.ArrayBuffer

class FTPOperation(host: String, port: Int, userName: String, password: String) extends FSOperation {

  private val ftpClient = new FTPClient
  ftpClient.connect(host, port)
  ftpClient.login(userName, password)
  ftpClient.setFileType(FTP.BINARY_FILE_TYPE)
  if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode)) {
    ftpClient.disconnect()
    logger.error("connect error.")
  }

  def getFTPClient = {
    ftpClient
  }

  /**
   * 设置编码
   *
   * @param encoding 编码类型
   */
  def setEncoding(encoding: String) {
    ftpClient.setControlEncoding(encoding)
  }

  /**
   * 关闭FTP连接
   *
   */
  def close() {
    ftpClient.logout
    if (ftpClient.isConnected) {
      ftpClient.disconnect()
    }
  }

  override protected def _createDir(path: String): Unit = {
    ftpClient.makeDirectory(path)
  }

  override protected def _deleteDir(path: String): Unit = {
    ftpClient.removeDirectory(path)
  }

  override protected def _deleteFile(path: String): Unit = {
    ftpClient.deleteFile(path)
  }

  override protected def _moveDir(sourcePath: String, targetPath: String): Unit = {
    ftpClient.rename(sourcePath,targetPath)
  }

  override protected def _moveFile(sourcePath: String, targetPath: String): Unit = {
    ftpClient.rename(sourcePath,targetPath)
  }

  override protected def _seekDir(path: String): Array[FileInfo] = {
    val fileInfo = new ArrayBuffer[FileInfo]()
    ftpClient.listFiles(path).foreach {
      file =>
        if (file.isFile) {
          fileInfo += FileInfo(file.getName, formatDirPath(path) + file.getName, file.getSize, if (file.getTimestamp != null) file.getTimestamp.getTimeInMillis else 0)
        }
    }
    fileInfo.toArray
  }

  override protected def _seekFile(path: String): FileInfo = {
    val file = ftpClient.listFiles(path)(0)
    FileInfo(file.getName,path, file.getSize, file.getTimestamp.getTimeInMillis)
  }

  override protected def _existDir(path: String): Boolean = {
    val pathName = path.substring(path.lastIndexOf(separator) + 1)
    ftpClient.listFiles(getParentPath(path)).exists {
      item =>
        item.isDirectory && item.getName == pathName
    }
  }

  override protected def _existFile(path: String): Boolean = {
    val pathName = path.substring(path.lastIndexOf(separator) + 1)
    ftpClient.listFiles(getParentPath(path)).exists {
      item =>
        item.isFile && item.getName == pathName
    }
  }

  override protected def _isFile(path: String): Boolean = ???

  override protected def separator: String = "/"
}

object FTPOperation {
  def apply(host: String, port: Int, userName: String, password: String) = new FTPOperation(host, port, userName, password)
}

