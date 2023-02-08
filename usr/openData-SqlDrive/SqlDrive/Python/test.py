from drive import driver
from timeit import timeit

driver = driver.Driver(
    host="127.0.0.1",
    username="root",
    password="123456",
)

# tip: Linwin DB Database ip 节流, 短时间内请求次数请不要设置过多

# driver.createDatabase("Linwin")
#driver.createData("Data", "Linwin")
print(len(driver.findData("d")))

# print("qps:", 1/timeit('driver.findData("d")', globals=globals(), number=1))
