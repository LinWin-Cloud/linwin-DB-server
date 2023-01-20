import os

for i in range(1000):
    os.system("touch "+str(i)+".mydb")
    with open(str(i)+".mydb","a") as f:
        for j in range(1000):
            f.write("Name=data"+str(j*i)+"&Value=hello"+str(j*i)+"&Type=string&createTime=2023.1.16&ModificationTime=2023.1.16&note=linwincloud\n")
