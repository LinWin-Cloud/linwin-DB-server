import os

os.system("rm -f *.mydb")
for i in range(3000):
    os.system("touch "+str(i)+".mydb")
    with open(str(i)+".mydb","a") as a:
        a.write("""
{
    "Update" : "2023.1.24"
}
                """)
    for j in range(1000):
        with open(str(i)+".mydb","a") as f:
            f.write("Name=data"+str(i+j)+"&Value="+str(i+j)+"&Type=string&createTime=2023.1.1&ModificationTime=2023.1.1&note=No\n")
