import { IncomingMessage } from "http";
const md5 = require("md5-node");
const http = require('http');

export default class Driver {
    public host: string;
    public port: number;
    public username: string;
    public password: string;
    public enable_ssl: boolean;
    private readonly remote: string;

    public constructor (username: string, password: string, host: string = "127.0.0.1", port: number = 8888, enable_ssl: boolean = false) {
        /**
         *  @param username: user name (like root, linwin)
         *  @param password: raw password (like 123456)
         *  @param host: host name of the database (like 127.0.0.1, localhost, example.org)
         *  @param port: port of the database, default is 8888
         *  @param enable_ssl: enable ssl. if the variable is true, it will be https:// .
         **/

        this.host = host;
        this.port = port;
        this.enable_ssl = enable_ssl;
        this.remote = `${this.enable_ssl?'https':'http'}://${this.host}:${this.port}/`
        this.username = username;
        this.password = md5(password);
    }
    protected _execute(script: string, callback: (data: string) => any): void {
        http.get(
            `${this.remote}/?Logon=${this.username}?Passwd=${this.password}?Command=${script}`,
            function (response: IncomingMessage) {
                response.setEncoding("utf8");
                let data = '';
                response.on('data', function (chunk: any) {
                    data += chunk;
                }).on('end', function () {
                    callback(data);
                });
            }
        )
    }

    public getRemote(): string {
        return this.remote;
    }

    public execute(script: string, callback: (data: string) => any): void {
        this._execute(script, function(data: string) {
            if (data == '\n') {
                throw new Error('Request execution failure!');
            } else {
                return callback(data.trim());
            }
        })
    }

    public execute_ignore(script: string, callback : (response: boolean) => any): void {
        this._execute(script, function(data: string) {
            return callback(data !== '\n');
        })
    }

    public createData(name: string, value: string, note: string, database: string, callback: (data: string) => any): void {
        this.execute(`create data '${name}' setting('${value}','${note}') in ${database}`, callback);
    }
    public createDatabase(name: string, callback: (data: boolean) => any): void {
        this.execute_ignore(`create database '${name}'`, callback);
    }
    public renameDatabase(database: string, rename: string, callback: (data: boolean) => any): void {
        this.execute_ignore(`rename database '${database}' '${rename}'`, callback);
    }
    public renameData(data: string, rename: string, database: string, callback: (data: boolean) => any): void {
        this.execute_ignore(`rename data '${database}' '${rename}' in ${database}`, callback);
    }
    public findData(name: string, callback: (data: object) => any): void {
        this.execute(`find data ${name}`, (data: string) => (callback(data.split("\n"))));
    }
    public findDatabase(name: string, callback: (data: object) => any): void {
        this.execute(`find database ${name}`, (data: string) => callback(data.split("\n")));
    }
    public getData(name: string, type: string, database: string, callback: (data: string) => any): void {
        this.execute(`get '${name}'.${type} in ${database}`, (data: string) => (callback(data.replace("\n", ""))))
    }
    public indexData(name: string,database: string, callback : (data : object) => any): void {
        this.execute(`index '${name}' in ${database}`, (data: string) => (callback(data.split("\n"))));
    }
    public deleteData(name: string, database: string, callback: (data: boolean) => any): void {
        this.execute_ignore(`delete data '${name}' in ${database}`, callback);
    }
    public deleteDatabase(name: string, callback: (data: boolean) => any): void {
        this.execute_ignore(`delete database ${name}`, callback);
    }
    public copyDatabase(name: string, target: string, callback: (data: boolean) => any): void {
        this.execute_ignore(`copy '${name}' '${target}'`, callback)
    }
    public listDatabase(callback: (data: object) => any): void {
        this.execute('list database', (data: string) => (callback(data.split("\n"))))
    }
    public listDataFromDatabase(database: string, callback: (data: object) => any): void {
        this.execute(`ls ${database}`, (data: string) => (callback(data.split("\n"))))
    }
}