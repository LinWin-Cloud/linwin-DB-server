import requests
import hashlib


def encode(val: str):
    return hashlib.md5(val.encode("utf-8")).hexdigest()


class Driver(object):
    message: str

    def __init__(self, remote: str, username: str, password: str) -> None:
        self.username = username
        self.password = password
        self.remote = remote

    def sendMysScript(self, script: str) -> bool:
        r = requests.get(str(self.remote) + "/?Logon=" + str(self.username) + "?Passwd=" + str(
            encode(self.password)) + "?Command=" + script)
        getText = r.text
        split: list[str] = getText.split("\n")
        message: str = ""
        j = 0
        splitADD: list[str] = []
        for i in split:
            j += 1
            splitADD.append(str(i) + "\n")

        message = message.join(splitADD)

        j = j - 1
        self.message = message
        if j == 0:
            return False
        else:
            return True

    def getMessage(self) -> str:
        return self.message

    def __str__(self) -> str:
        return f"Driver(username={self.username}, password={self.password}, remote={self.remote})"

    def listDatabase(self) -> list[str]:
        if self.sendMysScript("list database"):
            return self.getMessage().split("\n")
        else:
            return []

    def deleteDatabase(self, name: str) -> bool:
        return self.sendMysScript("delete database " + str(name))

    def deleteData(self, name: str, database: str) -> bool:
        return self.sendMysScript("delete data '" + str(name) + "' in " + database)

    def reNameDatabase(self, name: str, NewName: str) -> bool:
        return self.sendMysScript("rename database '" + name + "' '" + NewName + "'")

    def reNameData(self, name: str, NewName: str, database: str) -> bool:
        return self.sendMysScript("rename data '" + name + "' '" + NewName + "' in " + database)

    def createDatabase(self, name: str) -> bool:
        return self.sendMysScript("create database '" + name + "'")

    def createData(self, name: str, database: str) -> bool:
        return self.sendMysScript("create data '" + name + "' in " + database)

    def findDatabase(self, index: str) -> list[str]:
        self.sendMysScript("find database " + index)
        return self.getMessage().split("\n")

    def findData(self, index: str) -> list[str]:
        self.sendMysScript("find data " + index)
        return self.getMessage().split("\n")

    def getData(self, dataName: str, type: str, database: str) -> str:
        self.sendMysScript("get '" + dataName + "'." + type + " in" + database)
        return self.getMessage().replace("\n", "")

    def indexData(self, index: str, database: str) -> list[str]:
        self.sendMysScript("index '" + index + "' in " + database)
        return self.getMessage().split("\n")

    def copyDatabase(self, name: str, target: str) -> bool:
        return self.sendMysScript("copy '" + name + "' '" + target + "'")

    def getAlldata_FromDatabase(self, database: str) -> list[str]:
        self.sendMysScript("ls " + database)
        return self.getMessage().split("\n")
