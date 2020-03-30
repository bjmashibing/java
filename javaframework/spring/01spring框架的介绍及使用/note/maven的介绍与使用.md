# maven的介绍与使用

### 1、maven的简单介绍

​		Maven是Apache下的项目管理工具，它由纯Java语言开发，可以帮助我们更方便的管理和构建Java项目。

​		maven的优点

​		1、  jar包管理：

​			a)   从Maven中央仓库获取标准的规范的jar包以及相关依赖的jar包，避免自己下载到错误的jar包；

​			b)   本地仓库统一管理jar包，使jar包与项目分离，减轻项目体积。

​		2、  maven是跨平台的可以在window、linux上使用。

​		3、  清晰的项目结构；

​		4、  多工程开发，将模块拆分成若干工程，利于团队协作开发。

​		5、  一键构建项目：使用命令可以对项目进行一键构建。

### 2、maven的安装

​	maven官网：https://maven.apache.org/

​	maven仓库：https://mvnrepository.com/

​	安装步骤：

```
1、安装jdk
2、从官网中下载对应的版本
3、解压安装，然后配置环境变量，需要配置MAVEN_HOME,并且将bin目录添加到path路径下
4、在命令行中输入mvn -v,看到版本信息表示安装成功
```

### 3、maven的基本常识

**maven如何获取jar包**

​		maven通过坐标的方式来获取 jar包，坐标组成为：公司/组织（groupId）+项目名（artifactId）+版本（version）组成，可以从互联网，本地等多种仓库源获取jar包

**maven仓库的分类**

​		本地仓库：本地仓库就是开发者本地已经下载下来的或者自己打包所有jar包的依赖仓库，本地仓库路径配置在maven对应的conf/settings.xml配置文件。

​		私有仓库：私有仓库可以理解为自己公司的仓库，也叫Nexus私服

​		中央仓库：中央仓库即maven默认下载的仓库地址，是maven维护的

**maven的常用仓库**

​		由于网络访问的原因，在国内如果需要下载国外jar包的时候会受限，因此一般在使用过程中需要修改maven的配置文件，将下载jar包的仓库地址修改为国内的源，常用的是阿里云的mvn仓库，修改配置如下：

```xml
<mirror>
<id>alimaven</id>
<name>aliyun maven</name>
<url>http://maven.aliyun.com/nexus/content/groups/public/</url>
<mirrorOf>central</mirrorOf>
</mirror>
```

### 4、maven常用命令

- clean：清理编译后的目录
- compile：编译，只编译main目录，不编译test中的代码
- test-compile：编译test目录下的代码
- test：运行test中的代码
- package：打包，将项目打包成jar包或者war包
- install：发布项目到本地仓库，用在打jar包上，打成的jar包可以被其他项目使用
- deploy：打包后将其安装到pom文件中配置的远程仓库
- site：生成站点目录