from drive import driver
import time

b = driver.Driver(
    remote="http://127.0.0.1:8888/",
    username="root",
    password="123456",
)

s = time.time()
print(len(b.findData("d")))
e = time.time()
print((e - s))
