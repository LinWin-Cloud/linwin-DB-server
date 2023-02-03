import requests
import hashlib

class SQL_DO(object):
    def __init__(self,remote,user,passwd) -> None:
        self.userName = user
        self.Passwd = passwd
        self.Remote = remote

    def add_MD5(self,addStr:str)->str:
        hl = hashlib.md5()
        hl.update(addStr.encode("utf-8"))
        return hl.hexdigest()

    def sendMysScript(self,script:str) -> bool:
        #print(str(self.Remote)+"/?Logon="+str(self.userName)+"?Passwd="+str(self.add_MD5(self.Passwd))+"?Command="+script)
        r = requests.get(str(self.Remote)+"/?Logon="+str(self.userName)+"?Passwd="+str(self.add_MD5(self.Passwd))+"?Command="+script)
        getText = r.text
        split = getText.split("\n")
        message:str = ""
        j = 0
        for i in split:
            j = j + 1
            message = message + str(i) + "\n"
        
        j = j - 1
        self.Message = message
        if j == 0:
            return False
        else:
            return True
    def getMessage(self) -> str:
        return self.Message
    def outValue(self) -> None:
        print(self.userName+" "+self.Passwd+" "+self.Remote)

    def listDatabase(self) -> list[str]:
        if self.sendMysScript("list database") == True:
            return self.getMessage().split("\n")
        else:
            return []
    
    def deleteDatabase(self,name:str) -> bool:
        return self.sendMysScript("delete database "+str(name))
    
    def deleteData(self,name:str,database:str) -> bool:
        return self.sendMysScript("delete data '"+str(name)+"' in "+database)

    
