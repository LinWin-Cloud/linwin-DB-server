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
        
        return True

    def outValue(self):
        print(self.userName+" "+self.Passwd+" "+self.Remote)