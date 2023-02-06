import requests
import hashlib
from typing import List, Iterable


class DatabaseError(Exception):
    pass


class Driver(object):
    """
    LinWin DB Server Python Client.
    e.g.
    >>> from drive import driver
    >>> driver = driver.Driver(
    >>>        host="127.0.0.1",
    >>>        username="root",
    >>>        password="123456",
    >>>    )

    >>> driver.remote
    > 'http://127.0.0.1:8888'


    """

    def __init__(self, username: str, password: str, host: str, port: int = 8888, enable_ssl=False) -> None:
        """
        :param username: user name (like root, linwin)
        :param password: raw password (like 123456)
        :param host: host name of the database (like 127.0.0.1, localhost, example.org)
        :param port: port of the database, default is 8888
        :param enable_ssl: enable ssl. if the variable is true, it will be https:// .
        """
        self.username = username
        self.password = hashlib.md5(password.encode("utf-8")).hexdigest()  # generate md5 value of raw password
        self.host = host
        self.port = port
        self.enable_ssl = enable_ssl

    @property
    def remote(self):
        return f"{'https' if self.enable_ssl else 'http'}://{self.host}:{self.port}/"

    def execute(self, script: str) -> str:
        raw = requests.get(f"{self.remote}/?Logon={self.username}?Passwd={self.password}?Command={script}").text
        assert raw != "\n", DatabaseError("Request execution failure!")
        return raw

    def execute_ignore(self, script: str) -> bool:
        try:
            self.execute(script)
            return True
        except DatabaseError:
            return False

    def __str__(self) -> str:
        return f"Driver(username={self.username}, password={self.password}, remote={self.remote})"

    def createDatabase(self, name: str) -> bool:
        return self.execute_ignore(f"create database '{name}'")

    def createData(self, name: str, value: str, note: str, database: str) -> bool:
        return self.execute_ignore(f"create data '{name}' setting('{value}','{note}') in {database}")

    def renameDatabase(self, name: str, rename: str) -> bool:
        return self.execute_ignore(f"rename database '{name}' '{rename}'")

    def renameData(self, name: str, rename: str, database: str) -> bool:
        return self.execute_ignore(f"rename data '{name}' '{rename}' in {database}")

    def findDatabase(self, index: str) -> List[str]:
        return self.execute(f"find database {index}").split("\n")

    def findData(self, index: str) -> List[str]:
        return self.execute(f"find data {index}").split("\n")

    def getData(self, name: str, type: str, database: str) -> str:
        return self.execute(f"get '{name}'.{type} in {database}").replace("\n", "")

    def indexData(self, index: str, database: str) -> List[str]:
        return self.execute(f"index '{index}' in {database}").split("\n")

    def deleteDatabase(self, name: str) -> bool:
        return self.execute_ignore(f"delete database {name}")

    def deleteData(self, name: str, database: str) -> bool:
        return self.execute_ignore(f"delete data '{name}' in {database}")

    def copyDatabase(self, name: str, target: str) -> bool:
        return self.execute_ignore(f"copy '{name}' '{target}'")

    def listDatabase(self) -> List[str]:
        return self.execute("list database").split("\n")

    def listDataFromDatabase(self, database: str) -> List[str]:
        return self.execute(f"ls {database}").split("\n")

    def createNewUser(self, userName: str) -> bool:
        return self.execute("sudo createUser '"+userName+"'")

    def deleteUser(self, userName: str , passwd: str) -> bool:
        return self.execute("sudo deleteUser '"+userName+"' '"+passwd+"'")
