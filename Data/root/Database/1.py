import os

a = 0
for i in range(100):
    a = a +1
    os.system("touch "+str(i)+".mydb")
    with open(str(i)+".mydb","a") as f:
        for j in range(333):
	        a = a + 1
	        f.write("Name=data"+str(j*i*a)+"&Value=hello"+str(j*i)+"&Type=string&createTime=2023.1.16&ModificationTime=2023.1.16&note=linwincloud\n")
	        f.write("Name=weixin wechat"+str(j*i*a)+"&Value=tencent"+str(j*i)+"&Type=string&createTime=2023.1.16&ModificationTime=2023.1.16&note=linwincloud\n")
	        f.write("Name=usersfdsf"+str(j*i*a)+"&Value=11451412313123"+str(j*i)+"&Type=string&createTime=2023.1.16&ModificationTime=2023.1.16&note=linwincloud\n")
           
            
