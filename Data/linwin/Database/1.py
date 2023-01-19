import os

for i in range(100):
    os.system("touch "+str(i)+".mydb")
    with open(str(i)+".mydb","a") as f:
        for j in range(10000):
            f.write("Name=data"+str(j)+"&Value=hello"+str(j)+"&Type=string&createTime=2023.1.16&ModificationTime=2023.1.16&note=linwincloud\n")
