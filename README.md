EZ FS
===
### 实现Local、FTP、HDFS文件系统的统一操作。

 =======================================================

## 支持的操作

* 创建目录
* 删除目录
* 移动目录
* 删除文件
* 移动文件
* 获取目录下的文件信息列表
* 获取单个文件信息
* 判断目录是否存在
* 判断文件是否存在
* 判断指定路径是否是文件
* Local To FTP 的文件传输
* Local To HDFS 的文件传输
* FTP To HDFS 的文件传输
* FTP To Local 的文件传输
* HDFS To Local 的文件传输
* HDFS To FTP 的文件传输

##使用

    <dependency>
        <groupId>com.ecfront</groupId>
        <artifactId>fs</artifactId>
        <version>0.2.4</version>
    </dependency>

** 需要操作HDFS时需要将对应的 `core-site.xml`,`hdfs-site.xml`放在classpath下！

两个接口：`FSOperation` 用于对单一文件系统的操作，`FSTransfer` 用于两个文件系统间的文件传输。

###示例（更多示例见测试代码）

    //创建本文件
    val fo = HDFSOperation(new Configuration())
    //创建目录
    fo.createDir("/test/tmp")
    //移动文件
    fo.moveFile("/test/a.txt", "/tmp/a.txt")
    //获取目录下的文件信息
    val fList = fo.seekDir(testPath)

    //从FTP上传输文件到HDFS
     FTPToHDFSTransfer("127.0.0.1", 21, "sa", "sa").transfer("/a.txt", "/test/a.txt")


=======================================================


### Check out sources
`git clone https://github.com/gudaoxuri/ez-fs.git`

### License

Under version 2.0 of the [Apache License][].

[Apache License]: http://www.apache.org/licenses/LICENSE-2.0

