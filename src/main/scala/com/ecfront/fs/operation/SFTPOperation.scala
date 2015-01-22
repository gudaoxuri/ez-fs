package com.ecfront.fs.operation

import com.jcraft.jsch.JSch
import org.apache.commons.net.ftp.FTPReply

import scala.collection.mutable.ArrayBuffer

class SFTPOperation(host: String, port: Int, userName: String, password: String) extends FSOperation {

  private val jsch = new JSch()
  private val session = jsch.getSession(userName, host, port)
  if (password != null) {
    session.setPassword(password)
  }
  session.connect()
  private val channel = session.openChannel("sftp")
  channel.connect()

  def getSFTPClient = {
    channel
  }


  /**
   * 关闭SFTP连接
   *
   */
  def close() {
    if (channel != null) {
      channel.disconnect()
    }
    if (session != null) {
      session.disconnect()
    }
  }

  private def formatPath(path: String): String = {
    if (path.startsWith("/")) path.substring(1) else path
  }



  override protected def _isFile(path: String): Boolean = ???

  override protected def separator: String = "/"

  override protected def _createDir(path: String): Boolean = ???

  override protected def _existDir(path: String): Boolean = ???

  override protected def _deleteDir(path: String): Boolean = ???

  override protected def _seekDir(path: String): Array[FileInfo] = ???

  override protected def _existFile(path: String): Boolean = ???

  override protected def _moveDir(sourcePath: String, targetPath: String): Boolean = ???

  override protected def _seekFile(path: String): FileInfo = ???

  override protected def _moveFile(sourcePath: String, targetPath: String): Boolean = ???

  override protected def _deleteFile(path: String): Boolean = ???
}

object SFTPOperation {
  def apply(host: String, port: Int, userName: String, password: String) = new SFTPOperation(host, port, userName, password)
}


