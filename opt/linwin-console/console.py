import time
import os

if __name__ == '__main__':
    print('''
 LinwinSQL Console.
    ''')
    print("1. create a new user:        Enter '1'")
    print("2. delete a user:            Enter '2'")
    print("3. list user:                Enter: '3'")
    
    option = input("Command_> ")
    if option == "1":
        userName:str = input("Create User's Name: ")
        Passwd:str = input("User's passwd: ")
        
        jsonContent:str = "{\n\"name\" : \"" + str(userName) + "\"\n"+ "\"Create Time\" : \"" + str(time.time()) + "\"\n"+ "\"Passwd\" : \"" + str(Passwd) + "\"\n"+"}"
        os.system("mkdir /usr/openData-Server/Data/"+userName)
        with open("/usr/openData-Server/Data/"+userName+"/user.json","w") as f:
            f.write(jsonContent)
        f.close
        os.system("mkdir /usr/openData-Server/Data/"+userName+"/Database")
        print("successful!")

    if option == "2":
        name = input("Delete UserName: ")
        if name.replace(" ","") == "root":
            print("[ERR] Mustn't Delete User: root")
            exit()
        else:
            os.system("rm -rf /usr/openData-Server/Data/"+name)
            print("ok")
    if option == "3":
        os.system("ls /usr/openData-Server/Data")
    else:
        exit()
