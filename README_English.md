# Linwin Data Server database software
In the vast ocean of modern big data, the computer is deeply bound to information and data, and it is database software that carries these billions of data.
Linwin Data Server, a domestic high-performance database software developed based on Java. Support domestic and Linux operating systems, support multi-user operation. Using NoSQL structure, self-developed mys database operation language, more simple, convenient and efficient.
The addition, deletion, modification and check of user data are all operated in memory, and the interactive writing and reading with the hard disk is managed by a special thread, all of which hinder.

#### Current Version: 1.5-2023.2.20 <a href='./Update.md'> update information</a>

### Installation
Method 1: <a href='https://gitee.com/LinwinSoft/Linwin-DB-Server/releases/'> download the deb package</a> 
Method 2:
In the Linux terminal, it belongs to the following command line 
```
 Git Clone https://gitee.com/LinwinSoft/linwin-DB-server/ # Clone source code locally
 
dpkg -b linwin-DB-server/ # package deb package
 
sudo dpkg -i linwin-DB-server.deb # Install the deb package
```

### Linwin Data Server database information
Minimum operating requirements:
1. At least 300MB of disk space
2. 1024Mb (1GB) RAM (minimum 512MB if on ECS)
3. Run on a Linux operating system that is not damaged
4. Networked computers
5. Processor Processing Speed: **1.0 HZ** or **Faster**
6. AMD64 architecture processor (64-bit processor)

Characteristic:
1. Support distributed cluster database deployment
2. Support local storage database
3. Support multi-user operation database
4. NoSQL schema database using key-value pattern

Operating system support: Linux
Documentation support: <a href='https://gitee.com/LinwinSoft/linwin-DB-server/wiki/home'> documentation</a> 
### Mature server architecture
Linwin Data Server adopts the mature architecture of openLinwin high-performance web server software, which can carry ****15,000 - 25,000** requests per second

### Very simple database operation script
mys, a scripting language for database operations by Linwinsoft's own research,
It has the characteristics of simple and easy to understand, supports the most basic addition, deletion, modification and query, and supports functions such as copying indexes.

The mys scripting language based on the mys compiler supports uploading data and variable comments

Login = {
		"remote" : "127.0.0.1",
 		"port" : "8888",
 		"user" : "root",
 		"passwd" : "123456"
	}
	# This is a comment
	This is also a note
	/** Also commented, the following script is used to list all databases in the user's root directory
	list database
	# This code means to index out all data with the 'da' character
	find data da

### Performance
Test Environment: 
1. Ubuntu Linux operating system physical machine
2. Intel i3 4GB RAM
3. No GPU
4. 120GB SSD X2
==============================
1. It takes 4-5s for 100W data loading
2. It takes 0.2-0.3 seconds to query 91W data for 100W (pure server)
3. Up to 200W data can be loaded at one time, and the effect of loading in multiple times is better
