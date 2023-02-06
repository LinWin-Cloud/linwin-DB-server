import os
import sys

def MainShell(user:str,passwd:str,host:str,port:int):
    command = input("LinwinDB-Server Shell $ ")
    if command == "clear":
        os.system("clear")
        return True
    else:
        pathNow = os.getcwd()
        os.system("cd '"+pathNow+"/../../release/out/' && java -jar ClientShell.jar "+host+" "+port+" "+user+" "+passwd+" \""+command+"\"")

if __name__ == '__main__':
    try:
        userName = sys.argv[1]
        passWd = sys.argv[2]
        remote = sys.argv[3]
        port = sys.argv[4]

        #print(userName+" "+passWd+" "+remote+" "+port)
        while True:
            MainShell(userName,passWd,remote,port)
    except:
        print('Input Value Error!')
