import os

for i in range(1):
    os.system("touch "+str(i)+".mydb")
    for j in range(1000):
        with open(str(i)+".mydb","a") as f:
            f.write("Name=data"+str(i+j)+"&Value="+str(i+j)+"&Type=string&createTime=2023.1.1&ModificationTime=2023.1.1&note=No\n")
