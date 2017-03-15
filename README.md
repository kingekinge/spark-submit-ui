
# spark-submit-ui
>这是一个基于playframwork开发，web管理的spark应用程序

>你需要安装SBT和Java以及PlayFramowrk。项目基于2.2.x 版本开发，需要PlayFramowrk 2.2或更高版本。

#### 测试环境
* JDK8
* Center OS  6.5
* Spark 1.5.2
* Hadoop 2.6.0
* Scala 2.11

#### 主要功能
* hadoop metrics 数据监控
* spark 集群状态信息展示
* 完善的spark app 提交与管理
* 任务状态监控，状态推送

#### 并下载并安装Play Framework 编译环境
 [Installing Play](https://www.playframework.com/documentation/2.5.x/Installing") 


#### 修改配置文件，将集群地址替换为你的
文件路径在
<pre>conf/web-site.conf</pre>
### 编译与运行
然后去 http://localhost:9000 查看正在运行的服务器。

如果运行有这个界面提示，点击Apply this script now 初始化数据表
 ![](http://upload-images.jianshu.io/upload_images/522641-65dbf16c874c1289.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 编译与运行
<pre> activator run </pre>

#### 项目默认使用H2数据库
这是Play 内嵌的一个数据库 H2
H2官方介绍 http://www.h2database.com/html/main.html 

如果想要换成Mysql或者是其他的存储可以参考指引
<b>MySQL 数据库引擎连接属性
配置文件 conf/application.conf
</b>
<pre> 
db.default.driver=com.mysql.jdbc.Driver
db.default.url="jdbc:mysql://localhost/playdb"
db.default.user=playdbuser
db.default.pass="a strong password" </pre>


#其他

通过界面管理，kill或者rerun任务

![](http://upload-images.jianshu.io/upload_images/522641-8bc5a35a895f944e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

如果你的提交参数或配置导致异常，可以在提交时查看相关的错误输出

![](https://github.com/kingekinge/spark-submit-ui/blob/master/public/images/522641-46907f6c8f3b2b0b.png) 



#Link:
IntelliJ IDE支持Maven的和为Scala提供了插件开发.  
IntelliJ download: https://www.jetbrains.com/idea/  
IntelliJ Scala Plugin: http://plugins.jetbrains.com/plugin/?id=1347  
playframework:https://www.playframework.com  
sbt:http://www.scala-sbt.org  
spark:http://spark.apache.org  



