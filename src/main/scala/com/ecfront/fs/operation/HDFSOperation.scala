package com.ecfront.fs.operation

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, FileUtil, Path}

import scala.collection.mutable.ArrayBuffer

class HDFSOperation(conf: Configuration) extends FSOperation {

  private val fs = FileSystem.get(conf)

  override protected def _createDir(path: String): Boolean = {
    fs.mkdirs(new Path(path))
  }

  override protected def _moveDir(sourcePath: String, targetPath: String): Boolean = {
    val dir: String = targetPath.substring(0, targetPath.lastIndexOf('/'))
    if (!existDir(dir)) {
      createDir(dir)
    }
    fs.rename(new Path(sourcePath), new Path(targetPath))
  }

  override protected def _isFile(path: String): Boolean = {
    fs.isFile(new Path(path))
  }

  override protected def _deleteDir(path: String): Boolean = {
    fs.delete(new Path(path), true)
  }

  override protected def _existFile(path: String): Boolean = {
    if (fs.exists(new Path(path))) {
      fs.isFile(new Path(path))
    } else {
      false
    }
  }

  override protected def _existDir(path: String): Boolean = {
    if (fs.exists(new Path(path))) {
      fs.isDirectory(new Path(path))
    } else {
      false
    }
  }

  override protected def _moveFile(sourcePath: String, targetPath: String): Boolean = {
    val dir: String = targetPath.substring(0, targetPath.lastIndexOf('/'))
    if (!existDir(dir)) {
      createDir(dir)
    }
    fs.rename(new Path(sourcePath), new Path(targetPath))
  }

  override protected def _copyFile(sourcePath: String, targetPath: String): Boolean = {
    val dir: String = targetPath.substring(0, targetPath.lastIndexOf('/'))
    if (!existDir(dir)) {
      createDir(dir)
    }
    FileUtil.copy(fs, new Path(sourcePath), fs, new Path(targetPath), true, conf)
  }

  override protected def _seekFile(path: String): FileInfo = {
    val file = fs.getFileStatus(new Path(path))
    FileInfo(file.getPath.getName, path, file.getLen, file.getModificationTime)
  }

  override protected def _deleteFile(path: String): Boolean = {
    FileSystem.get(conf).delete(new Path(path), true)
  }

  override protected def _seekDir(path: String): Array[FileInfo] = {
    val fileInfo = new ArrayBuffer[FileInfo]()
    fs.listStatus(new Path(path)).foreach {
      file =>
        if (file.isFile) {
          fileInfo += FileInfo(file.getPath.getName, formatDirPath(path) + file.getPath.getName, file.getLen, file.getModificationTime)
        }
    }
    fileInfo.toArray
  }

  override protected def separator: String = "/"
}

object HDFSOperation {
  def apply(conf: Configuration) = new HDFSOperation(conf)

  def apply() = new HDFSOperation(new Configuration)
}

