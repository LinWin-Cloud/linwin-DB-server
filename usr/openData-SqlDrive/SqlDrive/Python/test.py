import SqlDrive.SqlDrive as a
import time

b = a.SQL_DO("http://127.0.0.1:8888/","root","123456")

s = time.time()
print(len(b.findData("d")))
e = time.time()
print((e-s))