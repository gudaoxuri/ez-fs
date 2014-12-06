package com.ecfront.fs.transfer

import com.typesafe.scalalogging.slf4j.LazyLogging

/**
 * 文件传输类
 */
trait FSTransfer extends LazyLogging {

  /**
   * 传输文件<br/>
   * 建议不要传输目录，X TO HDFS时目录中的文件会因权限问题无法上传，其它场景无法使用目录
   * @param sourcePath 源文件路径
   * @param targetPath 目标文件路径（需要加上新的文件名）
   * @return
   */
  def transfer(sourcePath: String, targetPath: String): Boolean = try {
    logger.debug("Transfer from " + sourcePath + " to " + targetPath)
    _transfer(sourcePath, targetPath)
  } catch {
    case e: Exception =>
      logger.error("Transfer from[" + sourcePath + "] to [" + targetPath + "] error.", e)
      false
  }

  protected def _transfer(sourcePath: String, targetPath: String): Boolean

}
