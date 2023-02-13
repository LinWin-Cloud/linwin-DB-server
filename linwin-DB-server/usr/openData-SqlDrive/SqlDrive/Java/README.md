

### 调用api
```
//把 sqlDrive 包复制到Java项目目录
//在代码内 
import sqlDrive.SqlDrive;
import sqlDrive.SqlDo;

代码内调用api :
SqlDrive sqldrive = new SqlDrive();
sqldrive.setHost("127.0.0.1"); //设置远程地址
sqldrive.setUserName("root"); //设置登陆用户
sqldrive.setPasswd("123456"); //设置登陆密码

//示例
sqldrive.sendCommand("list database") // 返回boolean
System.out.println(sqldrive.getMessage()) //获取数据库列表

//更加方便快捷的操作
SqlDo sqlDo = new SqlDo(sqldrive);
sqlDo.createDatabase("helloDB"); //创建一个数据库
```

### Update更新API
1. createUser(userName:str) 以非root授权形式创建一个数据库用户
2. deleteUser(userName:str , passwd: str) 以非root授权形式删除一个数据库用户，需要被删除用户授权

#### 文档: https://gitee.com/LinwinSoft/linwin-DB-server/wiki/home/