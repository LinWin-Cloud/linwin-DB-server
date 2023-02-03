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

    def reNameDatabase(self,name:str,NewName:str) -> bool:
        return self.sendMysScript("rename database '"+name+"' '"+NewName+"'")
    
    def reNameData(self,name:str,NewName:str,database:str) -> bool:
        return self.sendMysScript("rename data '"+name+"' '"+NewName+"' in "+database)

    def createDatabase(self,name:str) -> bool:
        return self.sendMysScript("create database '"+name+"'")

    def createData(self,name:str,database:str) -> bool:
        return self.sendMysScript("create data '"+name+"' in "+database)

    def findDatabase(self,index:str) -> list[str]:
        self.sendMysScript("find database "+index)
        return self.getMessage().split("\n")

    def findData(self,index:str) -> list[str]:
        self.sendMysScript("find data "+index)
        return self.getMessage().split("\n")

    def getData(self,dataName:str,type:str,database:str) -> str:
        self.sendMysScript("get '"+dataName+"'."+type+" in"+database)
        return self.getMessage().replace("\n","")

    def indexData(self,index:str,database:str) -> list[str]:
        self.sendMysScript("index '"+index+"' in "+database)
        return self.getMessage().split("\n")

    def copyDatabase(self,name:str,target:str) -> bool:
        return self.sendMysScript("copy '"+name+"' '"+target+"'")

    def getAlldata_FromDatabase(self,database:str) -> list[str]:
        self.sendMysScript("ls "+database)
        return self.getMessage().split("\n")