package com.ecfront.fs.operation

import java.io.File
import java.nio.file.{Files, Paths}


class LocalOperation extends FSOperation {

  override protected def _createDir(path: String): Boolean = {
    Files.createDirectory(Paths.get(path))
    true
  }

  override protected def _deleteDir(path: String): Boolean = {
    Files.delete(Paths.get(path))
    true
  }

  override protected def _deleteFile(path: String): Boolean = {
    Files.delete(Paths.get(path))
    true
  }

  override protected def _moveDir(sourcePath: String, targetPath: String): Boolean = {
    Files.move(Paths.get(sourcePath), Paths.get(targetPath))
    true
  }

  override protected def _moveFile(sourcePath: String, targetPath: String): Boolean = {
    Files.move(Paths.get(sourcePath), Paths.get(targetPath))
    true
  }

  override protected def _seekDir(path: String): Array[FileInfo] = {
    for (file <- Paths.get(path).toFile.listFiles().filter(_.isFile)) yield FileInfo(file.getName, file.getPath, file.length(), file.lastModified())
  }

  override protected def _seekFile(path: String): FileInfo = {
    val file = Paths.get(path).toFile
    FileInfo(file.getName, file.getPath, file.length(), file.lastModified())
  }

  override protected def _existDir(path: String): Boolean = {
    Files.exists(Paths.get(path))
  }

  override protected def _existFile(path: String): Boolean = {
    Files.exists(Paths.get(path))
  }

  override protected def _isFile(path: String): Boolean = {
    Files.isRegularFile(Paths.get(path))
  }

  override protected def separator: String = File.separator
}

object LocalOperation {
  def apply() = new LocalOperation
}
