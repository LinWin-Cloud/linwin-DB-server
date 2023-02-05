"use strict";
exports.__esModule = true;
var md5 = require("md5-node");
var http = require('http');
var Driver = /** @class */ (function () {
    function Driver(username, password, host, port, enable_ssl) {
        /**
         *  @param username: user name (like root, linwin)
         *  @param password: raw password (like 123456)
         *  @param host: host name of the database (like 127.0.0.1, localhost, example.org)
         *  @param port: port of the database, default is 8888
         *  @param enable_ssl: enable ssl. if the variable is true, it will be https:// .
         **/
        if (host === void 0) { host = "127.0.0.1"; }
        if (port === void 0) { port = 8888; }
        if (enable_ssl === void 0) { enable_ssl = false; }
        this.host = host;
        this.port = port;
        this.enable_ssl = enable_ssl;
        this.remote = "".concat(this.enable_ssl ? 'https' : 'http', "://").concat(this.host, ":").concat(this.port, "/");
        this.username = username;
        this.password = md5(password);
    }
    Driver.prototype._execute = function (script, callback) {
        http.get("".concat(this.remote, "/?Logon=").concat(this.username, "?Passwd=").concat(this.password, "?Command=").concat(script), function (response) {
            response.setEncoding("utf8");
            var data = '';
            response.on('data', function (chunk) {
                data += chunk;
            }).on('end', function () {
                callback(data);
            });
        });
    };
    Driver.prototype.getRemote = function () {
        return this.remote;
    };
    Driver.prototype.execute = function (script, callback) {
        this._execute(script, function (data) {
            if (data == '\n') {
                throw new Error('Request execution failure!');
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
        this.execute("find data ".concat(name), function (data) { return (callback(data.split("\n"))); });
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
exports["default"] = Driver;
