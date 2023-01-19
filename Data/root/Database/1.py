import os

for i in range(10000):
    os.system("cat main.mydb > "+str(i)+".mydb")
