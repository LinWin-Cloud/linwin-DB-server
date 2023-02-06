# Python Api

### 安装依赖
```commandline
pip install -r requirements.txt
```

### 示例
```python
from drive import driver
db = driver.Driver(
    host="127.0.0.1",
    username="root",
    password="123456",
)

try:
    db.createDatabase("Linwin")
    db.createData("Data", "Linwin")
except driver.DatabaseError as e:
    ...
```

### Shell
示例
```commandline
python shell.py
Logon Host[default 127.0.0.1]: localhost
Logon Port[default 8888]: 
Logon User[default root]: 
Logon Password[default 123456]: 
======================= Linwin DB Client =======================
LinwinDB-Mydb Shell $ something-wrong
Error Command and Script

LinwinDB-Mydb Shell $ create database 'asdf'
Create Successful!

Query executed completely! (0.292 sec, 3.42 qps)
LinwinDB-Mydb Shell $ create something-wrong
Command syntax error!

LinwinDB-Mydb Shell $ list database
asdf

Query executed completely! (0.277 sec, 3.61 qps)
LinwinDB-Mydb Shell $ 
LinwinDB-Mydb Shell $ 
LinwinDB-Mydb Shell $ quit

```

### Update更新API
1. createUser(userName:str) 以非root授权形式创建一个数据库用户
2. deleteUser(userName:str , passwd: str) 以非root授权形式删除一个数据库用户，需要被删除用户的密码授权.
