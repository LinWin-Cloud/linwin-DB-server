package LinwinVOS.FileSystem;

import LinwinVOS.LinwinVOS;

import java.io.File;
import java.util.HashMap;

public class VosFiles {
    private String Type;
    private String name;
    private String path;
    private String user;
    /**
     * IF the type is a data then do not use the directory function , and set it 'null';
     */
    private HashMap<String,VMDirectory> Directory = new HashMap<String,VMDirectory>();
    /**
     * IF the type is 'Database' then can run this hashMap function.
     * Other type will set it is null.
     */
    private HashMap<String,Data> Database = new HashMap<String,Data>();

    public void setDirectory(VMDirectory vmDirectory){
        if (VosFiles.isDataBaseList(this)) {
            this.Directory.put(this.name,vmDirectory);
        }
    }
    public void setDatabase(Data data) {
        if (VosFiles.isDataBase(this)) {
            this.Database.put(this.name,data);
        }
    }

    public VosFiles() {
    }
    public void setUser(String user) {
        this.user = user;
    }
    public void setType(String type) {
        this.Type = type;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSavePath(String path) {
        this.path = path;
    }

    public String getType() {
        return this.Type;
    }
    public String getName() {
        return this.name;
    }
    public String getPath() {
        return this.path;
    }
    public String getPhysicalPath() {
        File file = new File(LinwinVOS.DatabasePath+"/"+this.user+"/"+this.path);
        return file.getAbsolutePath();
    }

    public static boolean getRealFileExists(String var0) {
        File var1 = new File(var0);
        return var1.exists();
    }
    public static Boolean isDataBaseList(VosFiles vosFiles) {
        try{
            if(vosFiles.getType().equals("DataBaseList")) {
                return true;
            }else {
                return false;
            }
        }catch (Exception exception){
            return false;
        }
    }
    public static Boolean isDataBase(VosFiles vosFiles) {
        try{
            if(vosFiles.getType().equals("DataBase")) {
                return true;
            }else {
                return false;
            }
        }catch (Exception exception){
            return false;
        }
    }
    public static Boolean isData(VosFiles vosFiles) {
        try{
            if(vosFiles.getType().equals("Data")) {
                return true;
            }else {
                return false;
            }
        }catch (Exception exception){
            return false;
        }
    }
}

