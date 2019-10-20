# JDK12的安装搭建

### 一、JDK下载

​		1、JDK官网下载地址：https://www.oracle.com/technetwork/java/javase/downloads/jdk12-downloads-5295953.html 
​    	2、选择：“Accept License Agreement” 
   	 3、下载的文件：jdk-12.0.1_windows-x64_bin.exe

![jdk12下载](https://github.com/msbbigdata/javase/blob/master/image/jdk12下载.png)

### 二、JDK安装

​		1、点击 “jdk-12.0.1_windows-x64_bin.exe”弹出安装向导页面，点击“下一步”；

![安装1](https://github.com/msbbigdata/javase/blob/master/image/安装1.png)

​		2、可自定义选择将JDK安装在目标文件夹（默认即可），选择“下一步”；

![安装2](https://github.com/msbbigdata/javase/blob/master/image/安装2.png)

​		3、选择“关闭”

![安装3](https://github.com/msbbigdata/javase/blob/master/image/安装3.png)

### 三、配置环境变量

​    1、安装前JDK目录下是这个样子的：

![jdk安装目录](https://github.com/msbbigdata/javase/blob/master/image/jdk安装目录.png)

​    2、生成JRE:电脑安装JDK12后，发现新版的JDK12安装完成后没有JRE，运行一下命令，生成JRE： bin\jlink.exe --module-path jmods --add-modules java.desktop --output jre 命令手动生成jre。

![安装jre](https://github.com/msbbigdata/javase/blob/master/image/安装jre.png)

​    3、安装后JDK目录下是这个样子的：

![安装jre后的目录](https://github.com/msbbigdata/javase/blob/master/image/安装jre后的目录.png)

​    4、注意：

​	（1）运行上面生成JRE的命令的时候，CMD窗口路径显示的为: C:\Program Files\Java\jdk-12.0.1>: ，运行的命令为：bin\jlink.exe --module-path jmods --add-modules java.desktop --output jre，运行 的位置和路径一定要一致。

​	（2）在启动cmd命令行的时候最好使用管理员的身份运行。

### 四、配置环境变量

####     1.进入环境变量配置界面

- 方法1：右键点击计算机–>属性–>高级系统设置–>高级–>环境变量
- 方法2：win+ R –> 输入 sysdm.cpl 并回车 –> 高级 –> 环境变量

####     2.添加环境变量

| 变量名    | 变量值                               |
| --------- | ------------------------------------ |
| JAVA_HOME | C:\Program Files\Java\jdk-12         |
| Path      | %JAVA_HOME%\bin;%JAVA_HOME%\jre\bin; |

###     注意：

​        1、"C:\Program Files\Java\jdk-12"该文件夹地址是JDK安装位置
​        2、从JDK 9开始发生重大变化 - - - “之前类和资源文件存储在lib/rt.jar， tools.jar，JDK 9版本开始 lib/dt.jar和其他各种内部JAR文件都存储在一个更有效的格式在实现特定的文件lib目录。所以不需要配置CLASSPASTH变量。”

![环境变量1](https://github.com/msbbigdata/javase/blob/master/image/环境变量1.png)

![环境变量2](https://github.com/msbbigdata/javase/blob/master/image/环境变量2.png)

### 五、检查JDK配置

​    键盘 “Win+R” 快捷键键调出“运行”，输入“cmd”，在命令提示符输入“java -version”

![检测jdk是否安装成功](https://github.com/msbbigdata/javase/blob/master/image/检测jdk是否安装成功.png)

​    以上步骤完成就成功搭建JDK的安装及环境变量了！！！