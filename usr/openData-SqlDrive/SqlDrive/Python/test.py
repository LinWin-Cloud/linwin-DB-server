import SqlDrive.SqlDrive as a
import requests

b = a.SQL_DO("http://127.0.0.1:8888/","root","123456")
b.sendMysScript("list database")