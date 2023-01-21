# Author: Linwincloud
# Mydb Shell for Linwin Data Server
# This is a shell to operate the Linwin Database.

import os

def MainShell(user:str,passwd:str,host:str,port:int):
    command = input("LinwinDB-Mydb Shell $ ")
    pathNow = os.getcwd()
    os.system("cd '"+pathNow+"/../../release/out/' && java -jar ClientShell.jar "+host+" "+port+" "+user+" "+passwd+" \""+command+"\"")

if __name__ == "__main__":
    host = input("Logon Host: ")
    port = input("Logon Port: ")
    user = input("Logon Users: ")
    passwd = input("Logon Passwd: ")
    print("=======================================")
    while True:
        MainShell(user,passwd,host,port)
        
