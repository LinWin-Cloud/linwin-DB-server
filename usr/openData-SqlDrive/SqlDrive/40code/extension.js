//
// description: the extension of TurboWrap/40code
// usage: Linwin DB Server Client Extension
//
// by zmh-program
// generate at 2023.2.4
//
var Driver = /** @class */ (function () {
    function Driver(username, password, host, port, protocol) {
        /**
         *  @param username: user name (like root, linwin)
         *  @param password: raw password (like 123456)
         *  @param host: host name of the database (like 127.0.0.1, localhost, example.org)
         *  @param port: port of the database, default is 8888
         *  @param protocol: like https://, tls:// .
         **/
        if (host === void 0) { host = "127.0.0.1"; }
        if (port === void 0) { port = 8888; }
        if (protocol === void 0) { protocol = "HTTP"; }
        this.host = host;
        this.port = port;
        this.protocol = protocol.toLowerCase();
        this.remote = "".concat(this.protocol, "://").concat(this.host, ":").concat(this.port, "/");
        this.username = username;
        this.password = window['CryptoJS'].MD5(String(password)).toString();
    }
    Driver.prototype._execute = function (script, callback) {
        var xhr = new XMLHttpRequest();
        xhr.open("GET", "".concat(this.remote, "?Logon=").concat(this.username, "?Passwd=").concat(this.password, "?Command=").concat(script.replace(' ', '%20')));
        xhr.setRequestHeader("Access-Control-Allow-Origin", "*");
        xhr.send();
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) {
                callback(xhr.responseText);
            }
        };
    };
    Driver.prototype.getRemote = function () {
        return this.remote;
    };
    Driver.prototype.execute = function (script, callback) {
        this._execute(script, function (data) {
            if (data == '\n') {
                callback('Error: Request execution failure!');
            }
            else {
                return callback(data.trim());
            }
        });
    };
    Driver.prototype.execute_ignore = function (script, callback) {
        this._execute(script, function (data) {
            return callback(data !== '\n');
        });
    };
    Driver.prototype.createData = function (name, value, note, database, callback) {
        this.execute("create data '".concat(name, "' setting('").concat(value, "','").concat(note, "') in ").concat(database), callback);
    };
    Driver.prototype.createDatabase = function (name, callback) {
        this.execute_ignore("create database '".concat(name, "'"), callback);
    };
    Driver.prototype.renameDatabase = function (database, rename, callback) {
        this.execute_ignore("rename database '".concat(database, "' '").concat(rename, "'"), callback);
    };
    Driver.prototype.renameData = function (data, rename, database, callback) {
        this.execute_ignore("rename data '".concat(database, "' '").concat(rename, "' in ").concat(database), callback);
    };
    Driver.prototype.findData = function (name, callback) {
        this.execute("find data ".concat(name), function (data) { return (callback(data.split("\n"))); }); // ok
    };
    Driver.prototype.findDatabase = function (name, callback) {
        this.execute("find database ".concat(name), function (data) { return callback(data.split("\n")); });
    };
    Driver.prototype.getData = function (name, type, database, callback) {
        this.execute("get '".concat(name, "'.").concat(type, " in ").concat(database), function (data) { return (callback(data.replace("\n", ""))); });
    };
    Driver.prototype.indexData = function (name, database, callback) {
        this.execute("index '".concat(name, "' in ").concat(database), function (data) { return (callback(data.split("\n"))); });
    };
    Driver.prototype.deleteData = function (name, database, callback) {
        this.execute_ignore("delete data '".concat(name, "' in ").concat(database), callback);
    };
    Driver.prototype.deleteDatabase = function (name, callback) {
        this.execute_ignore("delete database ".concat(name), callback);
    };
    Driver.prototype.copyDatabase = function (name, target, callback) {
        this.execute_ignore("copy '".concat(name, "' '").concat(target, "'"), callback);
    };
    Driver.prototype.listDatabase = function (callback) {
        this.execute('list database', function (data) { return (callback(data.split("\n"))); });
    };
    Driver.prototype.listDataFromDatabase = function (database, callback) {
        this.execute("ls ".concat(database), function (data) { return (callback(data.split("\n"))); });
    };
    return Driver;
}());
var db = undefined;
function get_db() {
    if (db === undefined) {
        throw new Error("数据库未初始化!");
    }
    return db;
}
function asyncInsertScript(src) {
    var script = document.createElement('script');
    script.src = src;
    script.defer = true;
    script.async = true;
    document.body.appendChild(script);
    return script;
}
function to_promise(wrap) {
    // @ts-ignore
    return new Promise(function (resolve) {
        try {
            wrap(resolve);
        }
        catch (err) {
            resolve(err); // (not reject) display in the block
        }
    });
}
var DatabaseExtension = /** @class */ (function () {
    function DatabaseExtension() {
    }
    DatabaseExtension.prototype.getInfo = function () {
        return {
            id: 'DatabaseExtension',
            name: '云数据库',
            color1: '#00c4ff',
            blocks: [
                {
                    opcode: 'init_db',
                    // @ts-ignore
                    blockType: Scratch.BlockType.COMMAND,
                    text: '连接数据库[host] 用户名[username] 密码[password] 端口[port] [proto]协议',
                    arguments: {
                        host: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue: '127.0.0.1'
                        },
                        username: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue: 'root'
                        },
                        password: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue: '123456'
                        },
                        port: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.NUMBER,
                            defaultValue: 443
                        },
                        proto: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue: "HTTPS",
                            menu: "protocol"
                        }
                    }
                },
                {
                    opcode: "get_remote",
                    // @ts-ignore
                    blockType: Scratch.BlockType.REPORTER,
                    text: "数据库url"
                },
                {
                    opcode: "execute",
                    // @ts-ignore
                    blockType: Scratch.BlockType.REPORTER,
                    text: "运行Mys语句 [script]",
                    arguments: {
                        script: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue: " "
                        }
                    }
                },
                {
                    opcode: "execute_boolean",
                    // @ts-ignore
                    blockType: Scratch.BlockType.BOOLEAN,
                    text: "运行Mys语句 [script]",
                    arguments: {
                        script: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue: " "
                        }
                    }
                },
                {
                    opcode: "create_database",
                    // @ts-ignore
                    blockType: Scratch.BlockType.COMMAND,
                    text: "创建数据库[database]",
                    arguments: {
                        database: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue: " "
                        }
                    }
                },
                {
                    opcode: "create_data",
                    // @ts-ignore
                    blockType: Scratch.BlockType.COMMAND,
                    text: "在数据库[database]中创建数据[name] 值为[value] 注记[note]",
                    arguments: {
                        database: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue: " "
                        },
                        name: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue: " "
                        },
                        value: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue: " "
                        },
                        note: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue: " "
                        }
                    }
                },
                {
                    opcode: "rename_database",
                    // @ts-ignore
                    blockType: Scratch.BlockType.COMMAND,
                    text: "将数据库[database]重命名为[rename]",
                    arguments: {
                        database: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue: " "
                        },
                        rename: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue: " "
                        }
                    }
                },
                {
                    opcode: "get_data",
                    // @ts-ignore
                    blockType: Scratch.BlockType.REPORTER,
                    text: "数据库[database]中数据[data](类型[vtype]) 的值",
                    arguments: {
                        database: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue: " "
                        },
                        data: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue: " "
                        },
                        vtype: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue: "STRING"
                        }
                    }
                },
                {
                    opcode: "get_index",
                    // @ts-ignore
                    blockType: Scratch.BlockType.REPORTER,
                    text: "数据库[database]中数据[data]的索引",
                    arguments: {
                        database: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue: " "
                        },
                        data: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue: " "
                        }
                    }
                },
                {
                    opcode: "copy_database",
                    // @ts-ignore
                    blockType: Scratch.BlockType.COMMAND,
                    text: "将数据库[database]复制到为[target]",
                    arguments: {
                        database: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue: " "
                        },
                        target: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue: " "
                        }
                    }
                },
                {
                    opcode: "rename_data",
                    // @ts-ignore
                    blockType: Scratch.BlockType.COMMAND,
                    text: "将数据库[database]中的数据[data] 重命名为[rename]",
                    arguments: {
                        database: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue: " "
                        },
                        data: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue: " "
                        },
                        rename: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue: " "
                        },
                        note: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue: " "
                        }
                    }
                },
                {
                    opcode: "find_database",
                    // @ts-ignore
                    blockType: Scratch.BlockType.REPORTER,
                    text: "搜索数据库 [database]",
                    arguments: {
                        database: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue: " "
                        }
                    }
                },
                {
                    opcode: "find_data",
                    // @ts-ignore
                    blockType: Scratch.BlockType.REPORTER,
                    text: "搜索数据 [data]",
                    arguments: {
                        data: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue: " "
                        }
                    }
                },
                {
                    opcode: "list_database",
                    // @ts-ignore
                    blockType: Scratch.BlockType.REPORTER,
                    text: "列出全部数据库"
                },
                {
                    opcode: "list_data",
                    // @ts-ignore
                    blockType: Scratch.BlockType.REPORTER,
                    text: "列出数据库[database]中的所有数据",
                    arguments: {
                        database: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue: " "
                        }
                    }
                },
                {
                    opcode: "delete_database",
                    // @ts-ignore
                    blockType: Scratch.BlockType.COMMAND,
                    text: "删除数据库 [database]",
                    arguments: {
                        database: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue: " "
                        }
                    }
                },
                {
                    opcode: "delete_data",
                    // @ts-ignore
                    blockType: Scratch.BlockType.COMMAND,
                    text: "删除数据库[database]中的数据[data]",
                    arguments: {
                        database: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue: " "
                        },
                        data: {
                            // @ts-ignore
                            type: Scratch.ArgumentType.STRING,
                            defaultValue: " "
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
    };
    DatabaseExtension.prototype.init_db = function (_a) {
        var host = _a.host, username = _a.username, password = _a.password, port = _a.port, proto = _a.proto;
        db = new Driver(username, password, host, Number(port), proto);
        return "成功初始化数据库!";
    };
    DatabaseExtension.prototype.get_remote = function () {
        return get_db().getRemote();
    };
    DatabaseExtension.prototype.execute = function (_a) {
        var script = _a.script;
        return to_promise(function (callback) { return (get_db().execute(script, callback)); });
    };
    DatabaseExtension.prototype.execute_boolean = function (_a) {
        var script = _a.script;
        return to_promise(function (callback) { return (get_db().execute_ignore(script, callback)); });
    };
    DatabaseExtension.prototype.create_database = function (_a) {
        var database = _a.database;
        return to_promise(function (callback) { return (get_db().createDatabase(database, callback)); });
    };
    DatabaseExtension.prototype.create_data = function (_a) {
        var database = _a.database, name = _a.name, value = _a.value, note = _a.note;
        return to_promise(function (callback) { return (get_db().createData(name, value, note, database, callback)); });
    };
    DatabaseExtension.prototype.rename_database = function (_a) {
        var database = _a.database, rename = _a.rename;
        return to_promise(function (callback) { return (get_db().renameDatabase(database, rename, callback)); });
    };
    DatabaseExtension.prototype.get_data = function (_a) {
        var database = _a.database, data = _a.data, vtype = _a.vtype;
        return to_promise(function (callback) { return (get_db().getData(data, vtype, database, callback)); });
    };
    DatabaseExtension.prototype.get_index = function (_a) {
        var database = _a.database, data = _a.data;
        return to_promise(function (callback) { return (get_db().indexData(data, database, callback)); });
    };
    DatabaseExtension.prototype.copy_database = function (_a) {
        var database = _a.database, target = _a.target;
        return to_promise(function (callback) { return (get_db().copyDatabase(database, target, callback)); });
    };
    DatabaseExtension.prototype.rename_data = function (_a) {
        var database = _a.database, data = _a.data, rename = _a.rename;
        return to_promise(function (callback) { return (get_db().renameData(data, rename, database, callback)); });
    };
    DatabaseExtension.prototype.find_database = function (_a) {
        var database = _a.database;
        return to_promise(function (callback) { return (get_db().findDatabase(database, callback)); });
    };
    DatabaseExtension.prototype.find_data = function (_a) {
        var data = _a.data;
        return to_promise(function (callback) { return (get_db().findData(data, callback)); });
    };
    DatabaseExtension.prototype.list_database = function () {
        return to_promise(function (callback) { return (get_db().listDatabase(callback)); });
    };
    DatabaseExtension.prototype.list_data = function (_a) {
        var database = _a.database;
        return to_promise(function (callback) { return (get_db().listDataFromDatabase(database, callback)); });
    };
    DatabaseExtension.prototype.delete_database = function (_a) {
        var database = _a.database;
        return to_promise(function (callback) { return (get_db().deleteDatabase(database, callback)); });
    };
    DatabaseExtension.prototype.delete_data = function (_a) {
        var database = _a.database, data = _a.data;
        return to_promise(function (callback) { return (get_db().deleteData(data, database, callback)); });
    };
    return DatabaseExtension;
}());
asyncInsertScript('https://cdn.bootcdn.net/ajax/libs/crypto-js/4.1.1/crypto-js.min.js');
// @ts-ignore
Scratch.extensions.register(new DatabaseExtension());
