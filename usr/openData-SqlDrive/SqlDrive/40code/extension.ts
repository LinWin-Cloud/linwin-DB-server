//
// description: the extension of TurboWrap/40code
// usage: Linwin DB Server Client Extension
//
// by zmh-program
// generate at 2023.2.4
//

class Driver {
    public host: string;
    public port: number;
    public username: string;
    public password: string;
    public protocol: string;
    private readonly remote: string;

    public constructor (username: string, password: string,
                        host: string = "127.0.0.1", port: number = 8888,
                        protocol: string = "HTTP") {
        /**
         *  @param username: user name (like root, linwin)
         *  @param password: raw password (like 123456)
         *  @param host: host name of the database (like 127.0.0.1, localhost, example.org)
         *  @param port: port of the database, default is 8888
         *  @param protocol: like https://, tls:// .
         **/

        this.host = host;
        this.port = port;
        this.protocol = protocol.toLowerCase();
        this.remote = `${this.protocol}://${this.host}:${this.port}/`
        this.username = username;
        this.password = window['CryptoJS'].MD5(String(password)).toString();
    }
    protected _execute(script: string, callback: (data: string) => any): void {
        const xhr = new XMLHttpRequest();
        xhr.open(
            "GET",
            `${this.remote}?Logon=${this.username}?Passwd=${this.password}?Command=${script.replace(' ', '%20')}`,
        );
        xhr.setRequestHeader("Access-Control-Allow-Origin", "*");
        xhr.send();
        xhr.onreadystatechange = () => {
            if (xhr.readyState === 4) {
                callback(xhr.responseText);
            }
        };
    }

    public getRemote(): string {
        return this.remote;
    }

    public execute(script: string, callback: (data: string) => any): void { // ok
        this._execute(script, function(data: string) {
            if (data == '\n') {
                callback('Error: Request execution failure!');
            } else {
                return callback(data.trim());
            }
        })
    }

    public execute_ignore(script: string, callback : (response: boolean) => any): void { // ok
        this._execute(script, function(data: string) {
            return callback(data !== '\n');
        })
    }

    public createData(name: string, value: string, note: string, database: string, callback: (data: string) => any): void { // ok
        this.execute(`create data '${name}' setting('${value}','${note}') in ${database}`, callback);
    }
    public createDatabase(name: string, callback: (data: boolean) => any): void { // ok
        this.execute_ignore(`create database '${name}'`, callback);
    }
    public renameDatabase(database: string, rename: string, callback: (data: boolean) => any): void { // ok
        this.execute_ignore(`rename database '${database}' '${rename}'`, callback);
    }
    public renameData(data: string, rename: string, database: string, callback: (data: boolean) => any): void { // ok
        this.execute_ignore(`rename data '${database}' '${rename}' in ${database}`, callback);
    }
    public findData(name: string, callback: (data: object) => any): void {
        this.execute(`find data ${name}`, (data: string) => (callback(data.split("\n")))); // ok
    }
    public findDatabase(name: string, callback: (data: object) => any): void {
        this.execute(`find database ${name}`, (data: string) => callback(data.split("\n")));
    }
    public getData(name: string, type: string, database: string, callback: (data: string) => any): void {
        this.execute(`get '${name}'.${type} in ${database}`, (data: string) => (callback(data.replace("\n", ""))))
    }
    public indexData(name: string, database: string, callback : (data : object) => any): void {
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

let db: Driver | undefined = undefined;
function get_db(): Driver | never {
    if ( db === undefined ) {
        throw new Error("?????????????????????!");
    }
    return db;
}

function asyncInsertScript(src: string): HTMLScriptElement {
    const script = document.createElement('script');
    script.src = src;
    script.defer = true;
    script.async = true;
    document.body.appendChild(script);
    return script;
}

function to_promise(wrap: (callback : () => any) => any): Promise<any> {
    // @ts-ignore
    return new Promise(function(resolve){
        try {
            wrap(resolve);
        } catch (err) {
            resolve(err); // (not reject) display in the block
        }
    })
}

class DatabaseExtension {
    public getInfo() {
        return {
            id: 'DatabaseExtension',
            name: '????????????',
            color1: '#00c4ff',
            blocks: [
                {
                    opcode: 'init_db',
                    // @ts-ignore
                    blockType: Scratch.BlockType.COMMAND,
                    text: '???????????????[host] ?????????[username] ??????[password] ??????[port] [proto]??????',
                    arguments: {
                        host: {
                            // @ts-ignore
                              type: Scratch.ArgumentType.STRING,
                              defaultValue: '127.0.0.1',
                        },
                        username: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue : 'root',
                        },
                        password: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue : '123456',
                        },
                        port: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.NUMBER,
                            defaultValue : 443,
                        },
                        proto: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue : "HTTPS",
                            menu: "protocol",
                        }
                    }
                },
                {
                    opcode: "get_remote",
                    // @ts-ignore
                    blockType: Scratch.BlockType.REPORTER,
                    text: "?????????url",
                },
                {
                    opcode: "execute",
                    // @ts-ignore
                    blockType: Scratch.BlockType.REPORTER,
                    text: "??????Mys?????? [script]",
                    arguments: {
                        script: {
                            // @ts-ignore
                            type : Scratch.ArgumentType.STRING,
                            defaultValue : " ",
                        }
                    }
                },
                {
                    opcode: "execute_boolean",
                    // @ts-ignore
                    blockType: Scratch.BlockType.BOOLEAN,
                    text: "??????Mys?????? [script]",
                    arguments: {
                        script: {
                            // @ts-ignore
                            type : Scratch.ArgumentType.STRING,
                            defaultValue : " ",
                        }
                    }
                },
                {
                    opcode: "create_database",
                    // @ts-ignore
                    blockType: Scratch.BlockType.COMMAND,
                    text : "???????????????[database]",
                    arguments : {
                        database: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue : " ",
                        }
                    }
                },
                {
                    opcode: "create_data",
                    // @ts-ignore
                    blockType: Scratch.BlockType.COMMAND,
                    text: "????????????[database]???????????????[name] ??????[value] ??????[note]",
                    arguments: {
                        database: {
                            // @ts-ignore
                            type : Scratch.ArgumentType.STRING,
                            defaultValue : " ",
                        },
                        name: {
                            // @ts-ignore
                            type : Scratch.ArgumentType.STRING,
                            defaultValue : " ",
                        },
                        value: {
                            // @ts-ignore
                            type : Scratch.ArgumentType.STRING,
                            defaultValue : " ",
                        },
                        note: {
                            // @ts-ignore
                            type : Scratch.ArgumentType.STRING,
                            defaultValue : " ",
                        }
                    }
                },
                {
                    opcode: "rename_database",
                    // @ts-ignore
                    blockType: Scratch.BlockType.COMMAND,
                    text: "????????????[database]????????????[rename]",
                    arguments: {
                        database: {
                            // @ts-ignore
                            type : Scratch.ArgumentType.STRING,
                            defaultValue : " ",
                        },
                        rename: {
                            // @ts-ignore
                            type : Scratch.ArgumentType.STRING,
                            defaultValue : " ",
                        }
                    }
                },
                {
                    opcode: "get_data",
                    // @ts-ignore
                    blockType: Scratch.BlockType.REPORTER,
                    text: "?????????[database]?????????[data](??????[vtype]) ??????",
                    arguments: {
                        database: {
                            // @ts-ignore
                            type : Scratch.ArgumentType.STRING,
                            defaultValue : " ",
                        },
                        data: {
                            // @ts-ignore
                            type : Scratch.ArgumentType.STRING,
                            defaultValue : " ",
                        },
                        vtype: {
                            // @ts-ignore
                            type : Scratch.ArgumentType.STRING,
                            defaultValue : "STRING",
                        }
                    }
                },
                {
                    opcode: "get_index",
                    // @ts-ignore
                    blockType: Scratch.BlockType.REPORTER,
                    text: "?????????[database]?????????[data]?????????",
                    arguments: {
                        database: {
                            // @ts-ignore
                            type : Scratch.ArgumentType.STRING,
                            defaultValue : " ",
                        },
                        data: {
                            // @ts-ignore
                            type : Scratch.ArgumentType.STRING,
                            defaultValue : " ",
                        },
                    }
                },
                {
                    opcode: "copy_database",
                    // @ts-ignore
                    blockType: Scratch.BlockType.COMMAND,
                    text: "????????????[database]????????????[target]",
                    arguments: {
                        database: {
                            // @ts-ignore
                            type : Scratch.ArgumentType.STRING,
                            defaultValue : " ",
                        },
                        target: {
                            // @ts-ignore
                            type : Scratch.ArgumentType.STRING,
                            defaultValue : " ",
                        }
                    }
                },
                {
                    opcode: "rename_data",
                    // @ts-ignore
                    blockType: Scratch.BlockType.COMMAND,
                    text: "????????????[database]????????????[data] ????????????[rename]",
                    arguments: {
                        database: {
                            // @ts-ignore
                            type : Scratch.ArgumentType.STRING,
                            defaultValue : " ",
                        },
                        data: {
                            // @ts-ignore
                            type : Scratch.ArgumentType.STRING,
                            defaultValue : " ",
                        },
                        rename: {
                            // @ts-ignore
                            type : Scratch.ArgumentType.STRING,
                            defaultValue : " ",
                        },
                        note: {
                            // @ts-ignore
                            type : Scratch.ArgumentType.STRING,
                            defaultValue : " ",
                        }
                    }
                },
                {
                    opcode: "find_database",
                    // @ts-ignore
                    blockType: Scratch.BlockType.REPORTER,
                    text : "??????????????? [database]",
                    arguments : {
                        database: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue : " ",
                        }
                    }
                },
                {
                    opcode: "find_data",
                    // @ts-ignore
                    blockType: Scratch.BlockType.REPORTER,
                    text : "???????????? [data]",
                    arguments : {
                        data: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue : " ",
                        }
                    }
                },
                {
                    opcode : "list_database",
                    // @ts-ignore
                    blockType: Scratch.BlockType.REPORTER,
                    text : "?????????????????????",
                },
                {
                    opcode : "list_data",
                    // @ts-ignore
                    blockType: Scratch.BlockType.REPORTER,
                    text : "???????????????[database]??????????????????",
                    arguments : {
                        database: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue : " ",
                        }
                    }
                },
                {
                    opcode: "delete_database",
                    // @ts-ignore
                    blockType: Scratch.BlockType.COMMAND,
                    text : "??????????????? [database]",
                    arguments : {
                        database: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue : " ",
                        }
                    }
                },
                {
                    opcode: "delete_data",
                    // @ts-ignore
                    blockType: Scratch.BlockType.COMMAND,
                    text : "???????????????[database]????????????[data]",
                    arguments : {
                        database: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue : " ",
                        },
                        data: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue : " ",
                        }
                    }
                },
            ],
            menus: {
                "protocol": {
                    items: [
                        "HTTPS",
                        "HTTP",
                        "TLS",
                        "TCP",
                        "UDP"
                    ]
                }
            }
        };
    }
    init_db({host, username, password, port, proto}) {
        db = new Driver(username, password, host, Number(port), proto);
        return "????????????????????????!";
    }
    get_remote() {
        return get_db().getRemote();
    }
    execute({script}) {
        return to_promise(
            (callback) => (
                get_db().execute(script, callback)
            )
        );
    }
    execute_boolean({script}) {
        return to_promise(
            (callback) => (
                get_db().execute_ignore(script, callback)
            )
        );
    }
    create_database({database}) {
        return to_promise(
            (callback) => (
                get_db().createDatabase(database, callback)
            )
        );
    }
    create_data({database, name, value, note}) {
        return to_promise(
            (callback) => (
                get_db().createData(name, value, note, database, callback)
            )
        );
    }
    rename_database({database, rename}) {
        return to_promise(
            (callback) => (
                get_db().renameDatabase(database, rename, callback)
            )
        );
    }
    get_data({database, data, vtype}) {
        return to_promise(
            (callback) => (
                get_db().getData(data, vtype, database, callback)
            )
        );
    }
    get_index({database, data}) {
        return to_promise(
            (callback) => (
                get_db().indexData(data, database, callback)
            )
        );
    }
    copy_database({database, target}) {
        return to_promise(
            (callback) => (
                get_db().copyDatabase(database, target, callback)
            )
        );
    }
    rename_data({database, data, rename}) {
        return to_promise(
            (callback) => (
                get_db().renameData(data, rename, database, callback)
            )
        );
    }
    find_database({database}) {
        return to_promise(
            (callback) => (
                get_db().findDatabase(database, callback)
            )
        );
    }
    find_data({data}) {
        return to_promise(
            (callback) => (
                get_db().findData(data, callback)
            )
        );
    }
    list_database() {
        return to_promise(
            (callback) => (
                get_db().listDatabase(callback)
            )
        );
    }
    list_data({database}) {
        return to_promise(
            (callback) => (
                get_db().listDataFromDatabase(database, callback)
            )
        );
    }
    delete_database({database}) {
        return to_promise(
            (callback) => (
                get_db().deleteDatabase(database, callback)
            )
        );
    }
    delete_data({database, data}) {
        return to_promise(
            (callback) => (
                get_db().deleteData(data, database, callback)
            )
        );
    }
}

asyncInsertScript('https://cdn.bootcdn.net/ajax/libs/crypto-js/4.1.1/crypto-js.min.js');
// @ts-ignore
Scratch.extensions.register(new DatabaseExtension());
