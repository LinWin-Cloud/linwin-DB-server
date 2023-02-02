package sqlDrive;

public class SqlDo {
    private SqlDrive sqlDrive;
    public SqlDo(SqlDrive sqlDrive) {
        this.sqlDrive = sqlDrive;
    }
    public SqlDrive getSqlDrive() {
        return this.sqlDrive;
    }
    public boolean createDatabase(String name) {
        return sqlDrive.sendCommand("create database '"+name+"'");
    }
    public boolean createData(String name,String value,String note,String database) {
        return sqlDrive.sendCommand("create data '"+name+"' setting('"+value+"','"+note+"') in "+database);
    }
    public boolean deleteDatabase(String name) {
        return sqlDrive.sendCommand("delete database "+name+"");
    }
    public boolean deleteData(String name,String database) {
        return sqlDrive.sendCommand("delete data '"+name+"' in "+database);
    }
    public boolean reData(String name,String data,String type,String database) {
        return sqlDrive.sendCommand("redata '"+name+"'."+type+"'"+data+"' in "+database);
    }
    public boolean reNameDatabase(String name,String NewName) {
        return sqlDrive.sendCommand("rename database '"+name+"' '"+NewName+"'");
    }
    public boolean reNameData(String name,String NewName,String database) {
        return sqlDrive.sendCommand("rename data '"+name+"' '"+NewName+"' in "+database);
    }
    public String[] findDataBase(String index) {
        boolean b = sqlDrive.sendCommand("find database "+index);
        if (b) {
            return sqlDrive.getMessage().split("\n");
        }else {
            return null;
        }
    }
    public String[] findData(String index) {
        boolean b = sqlDrive.sendCommand("find data "+index);
        if (b) {
            return sqlDrive.getMessage().split("\n");
        }else {
            return null;
        }
    }
    public String[] indexData(String index,String database) {
        boolean b = sqlDrive.sendCommand("index '"+index+"' in "+database);
        if (b) {
            return sqlDrive.getMessage().split("\n");
        }else {
            return null;
        }
    }
    public String getData(String name,String type,String database) {
        boolean b = sqlDrive.sendCommand("get '"+name+"'."+type+" in "+database);
        if (b) {
            return sqlDrive.getMessage().replace("\n","");
        }else {
            return null;
        }
    }
    public String[] getData(String name,String type) {
        boolean b = sqlDrive.sendCommand("get '"+name+"'."+type);
        if (b) {
            return sqlDrive.getMessage().split("\n");
        }else {
            return null;
        }
    }
}
