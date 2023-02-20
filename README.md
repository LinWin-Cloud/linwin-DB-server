
# Linwin Data Server数据库软件
在广袤无垠的现代大数据海洋之中，计算机深度的和信息以及数据绑定，承载这亿万数据的就是数据库软件。
Linwin Data Server，基于Java开发的国产高性能数据库软件。支持国产和Linux操作系统，支持多用户操作。采用Nosql结构，自研mys数据库操作语言，更加简单方便高效。
用户数据的增删改查全部在内存内操作，与硬盘的交互写入读取交由专门的线程管理，无不妨碍.

#### 当前版本: 1.4-2023.2.13 <a href='./Update.md'>更新信息</a>

### 安装
方法1: <a href='https://gitee.com/LinwinSoft/Linwin-DB-Server/releases/'>下载deb包</a>
方法2:
在Linux终端中属于下列命令行 
```
 git clone https://gitee.com/LinwinSoft/linwin-DB-server/ 	# 把源代码克隆到本地
 
 dpkg -b linwin-DB-server/ 					# 打包deb包
 
 sudo dpkg -i linwin-DB-server.deb 				# 安装deb包
```

### Linwin Data Server数据库信息
最低运行要求:
1. 至少300MB的磁盘空间
2. 1024Mb(1GB)内存 (如果在云服务器上可以512MB最低最低)
3. 在没有损坏的Linux操作系统上运行
4. 联网的计算机
5. 处理器处理速度: **1.0 HZ** 或 **更快**
6. amd64架构处理器(64位处理器)

特性:
1. 支持分布式集群部署数据库
2. 支持本地储存数据库
3. 支持多用户操作数据库
4. 采用key-value模式的Nosql架构数据库

操作系统支持:Linux
文档支持: <a href='https://gitee.com/LinwinSoft/linwin-DB-server/wiki/home'>文档</a>

### 成熟的服务器架构
Linwin Data Server采用 openLinwin 高性能Web服务器软件成熟架构，每秒可承载***1.5万 - 2.5万*** 次请求

### 非常简单的数据库操作脚本
由Linwinsoft自己研究的mys面向数据库操作的脚本语言mys,
具有简单易懂的特点，支持最基础的增删改查，同时支持复制索引等功能.

基于mys编译器的mys脚本语言支持上传数据、变量注释

	Login = {
		"remote" : "127.0.0.1",
 		"port" : "8888",
 		"user" : "root",
 		"passwd" : "123456"
	}
	# 这是一条注释
	// 这也是一条注释
	/** 同样是注释，下面这段脚本用于列出用户根目录所有的数据库
	list database
	# 这条代码意思是索引出所有带有 'da' 字符的数据
	find data da

### 性能
测试环境: 
1. Ubuntu Linux操作系统实体机
2. intel i3 4GB内存
3. 无GPU
4. 120GB SSD X2
==============================
1. 100W数据加载需要4-5s
2. 100w数据查询91w条需要0.2-0.3秒(纯服务端)
3. 一次性最多能够加载200w数据，分多次加载效果更佳
