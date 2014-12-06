package com.ecfront.fs

import java.io.File

import com.ecfront.fs.operation.{LocalOperation, HDFSOperation, FTPOperation}
import org.apache.hadoop.conf.Configuration
import org.scalatest.FunSuite

class FOSpec extends FunSuite {


  test("本地文件系统操作测试") {

    var testPath = this.getClass.getResource("/").getPath
    if (System.getProperties.getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1) {
      testPath = testPath.substring(1)
    }

    val fo = LocalOperation()

    fo.createDir(testPath + "tmp")

    assert(fo.existDir(testPath + "tmp"))

    fo.deleteDir(testPath + "tmp")

    assert(!fo.existDir(testPath + "tmp") )

    assert(!fo.existFile(testPath + "a.txt"))
    assert(!fo.existFile(testPath + "c.txt") )

    assert(fo.isFile(testPath + "b.txt"))
    assert(!fo.isFile(testPath + "testdir"))

    fo.moveFile(testPath + "a.txt", testPath + "testdir" + File.separator + "a.txt")
    assert(fo.existFile(testPath + "a.txt") )
    assert(fo.existFile(testPath + "testdir" + File.separator + "a.txt"))
    fo.moveFile(testPath + "testdir" + File.separator + "a.txt", testPath + "a.txt")

    fo.moveDir(testPath + "testdir2", testPath + "testdir" + File.separator + "testdir2")
    assert(fo.existFile(testPath + "testdir" + File.separator + "testdir2" + File.separator + "c.txt"))
    fo.moveDir(testPath + "testdir" + File.separator + "testdir2", testPath + File.separator + "testdir2")

    val fList = fo.seekDir(testPath)
    assert(fList.length == 4)
    assert(fList(0).name == "a.txt")

    val f = fo.seekFile(testPath + "b.txt")
    assert(f.name == "b.txt")
    //assert(f.path == testPath + "b.txt")

  }

  test("FTP文件系统操作测试") {

    val testPath = "/"

    val fo = FTPOperation("192.168.1.198", 21, "sa", "sa")

    fo.createDir(testPath + "tmp")

    assert(fo.existDir(testPath + "tmp"))

    fo.deleteDir(testPath + "tmp")

    assert(!fo.existDir(testPath + "tmp"))

    assert(fo.existFile(testPath + "a.txt"))
    assert(!fo.existFile(testPath + "c.txt") )

    /*assert(fo.isFile(testPath + "b.txt") == true)
    assert(fo.isFile(testPath + "testdir") == false)*/

    /* fo.moveFile(testPath + "a.txt", testPath + "testdir" + File.separator + "a.txt")
     assert(fo.existFile(testPath + "a.txt") == false)
     assert(fo.existFile(testPath + "testdir" + File.separator + "a.txt") == true)
     fo.moveFile(testPath + "testdir" + File.separator + "a.txt", testPath + "a.txt")

     fo.moveDir(testPath + "testdir2", testPath + "testdir" + File.separator +"testdir2")
     assert(fo.existFile(testPath + "testdir" + File.separator + "testdir2" + File.separator + "c.txt") == true)
     fo.moveDir(testPath + "testdir" + File.separator + "testdir2", testPath+ File.separator + "testdir2")*/

    val fList = fo.seekDir(testPath)
    assert(fList.length == 2)
    assert(fList(0).name == "a.txt")

    val f = fo.seekFile(testPath + "b.txt")
    assert(f.name == "b.txt")
    assert(f.path == testPath + "b.txt")

  }

  test("HDFS文件系统操作测试") {

    val testPath = "/test/"

    val fo = HDFSOperation(new Configuration())

    fo.createDir(testPath + "tmp")

    assert(fo.existDir(testPath + "tmp"))

    fo.deleteDir(testPath + "tmp")

    assert(!fo.existDir(testPath + "tmp"))

    assert(fo.existFile(testPath + "a.txt"))
    assert(!fo.existFile(testPath + "c.txt"))

    assert(fo.isFile(testPath + "b.txt") )
    assert(!fo.isFile(testPath + "testdir") )

    fo.moveFile(testPath + "a.txt", testPath + "testdir" + File.separator + "a.txt")
    assert(!fo.existFile(testPath + "a.txt"))
    assert(fo.existFile(testPath + "testdir" + File.separator + "a.txt") )
    fo.moveFile(testPath + "testdir" + File.separator + "a.txt", testPath + "a.txt")

    fo.moveDir(testPath + "testdir2", testPath + "testdir" + File.separator + "testdir2")
    assert(fo.existFile(testPath + "testdir" + File.separator + "testdir2" + File.separator + "c.txt"))
    fo.moveDir(testPath + "testdir" + File.separator + "testdir2", testPath + File.separator + "testdir2")

    val fList = fo.seekDir(testPath)
    assert(fList.length == 2)
    assert(fList(0).name == "a.txt")

    val f = fo.seekFile(testPath + "b.txt")
    assert(f.name == "b.txt")
    assert(f.path == testPath + "b.txt")

  }

}
