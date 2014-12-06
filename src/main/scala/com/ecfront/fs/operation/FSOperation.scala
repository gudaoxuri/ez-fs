package com.ecfront.fs.operation

import java.io.File

import com.typesafe.scalalogging.slf4j.LazyLogging

/**
 * 文件操作类
 */
trait FSOperation extends LazyLogging {

  protected def separator: String

  /**
   * 创建目录
   * @param path 目录路径
   * @return
   */
  def createDir(path: String): Boolean = try {
    _createDir(path)
    true
  } catch {
    case e: Exception =>
      logger.error("Create dir[" + path + "] error.", e)
      false
  }

  protected def _createDir(path: String): Unit

  /**
   * 删除目录
   * @param path 目录路径
   * @return
   */
  def deleteDir(path: String): Boolean = try {
    _deleteDir(path)
    true
  } catch {
    case e: Exception =>
      logger.error("Delete dir[" + path + "] error.", e)
      false
  }

  protected def _deleteDir(path: String): Unit

  /**
   * 删除文件
   * @param path 文件路径
   * @return
   */
  def deleteFile(path: String): Boolean = try {
    _deleteFile(path)
    true
  } catch {
    case e: Exception =>
      logger.error("Delete file[" + path + "] error.", e)
      false
  }

  protected def _deleteFile(path: String): Unit

  /**
   * 移动目录
   * @param sourcePath 源目录路径
   * @param targetPath 目标目录路径（需要加上新的目录名）
   */
  def moveDir(sourcePath: String, targetPath: String): Boolean = try {
    _moveDir(sourcePath, targetPath)
    true
  } catch {
    case e: Exception =>
      logger.error("Move dir[" + sourcePath + "] to [" + targetPath + "] error.", e)
      false
  }

  protected def _moveDir(sourcePath: String, targetPath: String): Unit

  /**
   * 移动文件
   * @param sourcePath 源文件路径
   * @param targetPath 目标文件路径（需要加上新的文件名）
   */
  def moveFile(sourcePath: String, targetPath: String): Boolean = try {
    _moveFile(sourcePath, targetPath)
    true
  } catch {
    case e: Exception =>
      logger.error("Move file[" + sourcePath + "] to [" + targetPath + "] error.", e)
      false
  }

  protected def _moveFile(sourcePath: String, targetPath: String): Unit

  /**
   * 获取目录下的文件信息列表
   * @param path 目录路径
   * @return 文件信息列表
   */
  def seekDir(path: String): Array[FileInfo] = try {
    _seekDir(path)
  } catch {
    case e: Exception =>
      logger.error("Seek dir[" + path + "] error.", e)
      null
  }

  protected def _seekDir(path: String): Array[FileInfo]

  /**
   * 获取文件信息
   * @param path 文件路径
   * @return 文件信息
   */
  def seekFile(path: String): FileInfo = try {
    _seekFile(path)
  } catch {
    case e: Exception =>
      logger.error("Seek file[" + path + "] error.", e)
      null
  }

  protected def _seekFile(path: String): FileInfo

  /**
   * 目录是否存在
   * @param path 目录路径
   * @return
   */
  def existDir(path: String): Boolean = try {
    _existDir(path)
  } catch {
    case e: Exception =>
      logger.error("Check exist dir[" + path + "] error.", e)
      false
  }

  protected def _existDir(path: String): Boolean

  /**
   * 文件是否存在
   * @param path 文件路径
   * @return
   */
  def existFile(path: String): Boolean = try {
    _existFile(path)
  } catch {
    case e: Exception =>
      logger.error("Check exist file[" + path + "] error.", e)
      false
  }

  protected def _existFile(path: String): Boolean

  /**
   * 指定路径是否是文件
   * @param path 指定的路径
   * @return
   */
  def isFile(path: String): Boolean = try {
    _isFile(path)
  } catch {
    case e: Exception =>
      logger.error("Check is file[" + path + "] error.", e)
      false
  }

  protected def _isFile(path: String): Boolean

  protected def formatDirPath(path: String): String = {
    File.separator
    if (!path.endsWith(separator)) {
      path + separator
    } else {
      path
    }
  }

  protected def getParentPath(path: String): String = {
    path.substring(0, path.lastIndexOf(separator) + 1)
  }

}

/**
 * 文件信息类
 * @param name 文件名
 * @param path 文件全路径
 * @param size 文件大小
 * @param changeTime 修改时间
 */
case class FileInfo(name: String, path: String, size: Long, changeTime: Long)
