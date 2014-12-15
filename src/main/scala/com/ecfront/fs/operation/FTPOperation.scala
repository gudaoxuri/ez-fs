package com.ecfront.fs.operation

import org.apache.commons.net.ftp.{FTP, FTPClient, FTPReply}

import scala.collection.mutable.ArrayBuffer

class FTPOperation(host: String, port: Int, userName: String, password: String) extends FSOperation {

  private val ftpClient = new FTPClient
  ftpClient.enterLocalPassiveMode()
  ftpClient.connect(host, port)
  ftpClient.login(userName, password)
  ftpClient.setFileType(FTP.BINARY_FILE_TYPE)
  if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode)) {
    ftpClient.disconnect()
    logger.error("Connect error.")
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

  private def formatPath(path: String): String = {
    if (path.startsWith("/")) path.substring(1) else path
  }

  override protected def _createDir(path: String): Boolean = {
    if (ftpClient.makeDirectory(formatPath(path))) {
      true
    } else {
      logger.error("Create dir[" + path + "]  error:" + ftpClient.getReplyString)
      false
    }
  }

  override protected def _deleteDir(path: String): Boolean = {
    if (ftpClient.removeDirectory(formatPath(path))) {
      true
    } else {
      logger.error("Delete dir[" + path + "]  error:" + ftpClient.getReplyString)
      false
    }
  }

  override protected def _deleteFile(path: String): Boolean = {
    if (ftpClient.deleteFile(formatPath(path))) {
      true
    } else {
      logger.error("Delete file[" + path + "]  error:" + ftpClient.getReplyString)
      false
    }
  }

  override protected def _moveDir(sourcePath: String, targetPath: String): Boolean = {
    if (ftpClient.rename(formatPath(sourcePath), formatPath(targetPath))) {
      true
    } else {
      logger.error("Move dir[" + sourcePath + "] to [" + targetPath + "] error:" + ftpClient.getReplyString)
      false
    }
  }

  override protected def _moveFile(sourcePath: String, targetPath: String): Boolean = {
    if (ftpClient.rename(formatPath(sourcePath), formatPath(targetPath))) {
      true
    } else {
      logger.error("Move dir[" + sourcePath + "] to [" + targetPath + "] error:" + ftpClient.getReplyString)
      false
    }
  }

  override protected def _seekDir(path: String): Array[FileInfo] = {
    val fileInfo = new ArrayBuffer[FileInfo]()
    ftpClient.listFiles(formatPath(path)).foreach {
      file =>
        if (file.isFile) {
          fileInfo += FileInfo(file.getName, formatDirPath(path) + file.getName, file.getSize, if (file.getTimestamp != null) file.getTimestamp.getTimeInMillis else 0)
        }
    }
    if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode)) {
      fileInfo.toArray
    } else {
      logger.error("Seek dir[" + path + "] error:" + ftpClient.getReplyString)
      null
    }
  }

  override protected def _seekFile(path: String): FileInfo = {
    val file = ftpClient.listFiles(formatPath(path))(0)
    if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode)) {
      FileInfo(file.getName, path, file.getSize, file.getTimestamp.getTimeInMillis)
    } else {
      logger.error("Seek file[" + path + "] error:" + ftpClient.getReplyString)
      null
    }
  }

  override protected def _existDir(path: String): Boolean = {
    val fPath = formatPath(path)
    val pathName = fPath.substring(fPath.lastIndexOf(separator) + 1)
    val result = ftpClient.listFiles(getParentPath(fPath)).exists {
      item =>
        item.isDirectory && item.getName == pathName
    }
    if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode)) {
      result
    } else {
      logger.error("Check exist dir[" + path + "] error:" + ftpClient.getReplyString)
      false
    }
  }

  override protected def _existFile(path: String): Boolean = {
    val fPath = formatPath(path)
    val pathName = fPath.substring(fPath.lastIndexOf(separator) + 1)
    val result = ftpClient.listFiles(getParentPath(fPath)).exists {
      item =>
        item.isFile && item.getName == pathName
    }
    if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode)) {
      result
    } else {
      logger.error("Check exist file[" + path + "] error:" + ftpClient.getReplyString)
      false
    }
  }

  override protected def _isFile(path: String): Boolean = ???

  override protected def separator: String = "/"
}

object FTPOperation {
  def apply(host: String, port: Int, userName: String, password: String) = new FTPOperation(host, port, userName, password)
}

