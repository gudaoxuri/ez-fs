package com.ecfront.fs.transfer

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}


class LocalToHDFSTransfer(hdfsConf: Configuration) extends FSTransfer{

   override protected def _transfer(sourcePath: String, targetPath: String): Boolean = {
     FileSystem.get(hdfsConf).copyFromLocalFile(new Path(sourcePath), new Path(targetPath))
     true
   }

 }

object LocalToHDFSTransfer {
  def apply() = new LocalToHDFSTransfer(new Configuration)
  def apply(hdfsConf: Configuration) = new LocalToHDFSTransfer(hdfsConf)
}

