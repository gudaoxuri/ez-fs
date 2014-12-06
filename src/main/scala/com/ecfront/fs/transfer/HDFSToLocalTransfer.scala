package com.ecfront.fs.transfer

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}


class HDFSToLocalTransfer(hdfsConf: Configuration) extends FSTransfer {

  override protected def _transfer(sourcePath: String, targetPath: String): Boolean = {
    val fs: FileSystem = FileSystem.get(hdfsConf)
    fs.copyToLocalFile( new Path(sourcePath), new Path(targetPath))
    true
  }

}

object HDFSToLocalTransfer {
  def apply() = new HDFSToLocalTransfer(new Configuration())

  def apply(hdfsConf: Configuration) = new HDFSToLocalTransfer(hdfsConf)
}
