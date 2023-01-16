package LinwinVOS.FileSystem;

import java.io.File;

public class VosFiles {
    private String Type;
    private String name;
    private String path;

    public VosFiles() {
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

