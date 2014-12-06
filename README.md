EZ RPC
===
### 基于netty的RPC封装，目前只支持Restful HTTP方式

 =======================================================

##使用

    <dependency>
        <groupId>com.ecfront</groupId>
        <artifactId>rpc</artifactId>
        <version>0.1</version>
    </dependency>

###HTTP服务

    //启动HTTP服务
    HttpServer.startup(<端口>)

    //注册业务方法
    Register.<get|post|put|delete>("<URI>", <业务方法>)

    //关闭HTTP服务
    HttpServer.destroy

###HTTP请求

    //初始一个客户端实例
    val client = new HttpClient
    //发起一个请求，返回HttpResult对象，result.code为200表示成功，反之为失败
    val result = client.<get|post|put|delete>("<URI>", classOf[<返回对象类型>])


##示例（更多示例见测试代码）

     HttpServer.startup(3000)

     Register.get("/user/:id/", new SimpleFun {
       override def execute(parameters: Map[String, String], body: String, cookies: Set[Cookie]): HttpResult[_] = {
         HttpResult.success[String](parameters.get("arg").get)
       }
     })

     Register.post("/user/", new Fun[Person](classOf[Person]) {
       override def execute(parameters: Map[String, String], body: Person, cookies: Set[Cookie]): HttpResult[_] = {
         HttpResult.success[Person](body)
       }
     })

     val client = new HttpClient
     val result1 = client.get("http://127.0.0.1:3000/user/100/?arg=测试", classOf[String])
     val result2 = client.post("http://127.0.0.1:3000/user/", Person("孤岛旭日",Address("HangZhou")), classOf[Person])

     HttpServer.destroy

     case class Person(var name: String,var address:Address)
     case class Address(var addr: String)

=======================================================


### Check out sources
`git clone https://github.com/gudaoxuri/ez-rpc.git`

### License

Under version 2.0 of the [Apache License][].

[Apache License]: http://www.apache.org/licenses/LICENSE-2.0

