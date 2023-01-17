package LinwinVOS.Users;

import LinwinVOS.FileSystem.VosDatabase;

import java.util.HashMap;

public class UsersFileSystem {
    private String userName;
    private HashMap<String, VosDatabase> userDatabase = new HashMap<String,VosDatabase>();

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void putDatabase(String name,VosDatabase vosDatabase) {
        this.userDatabase.put(name,vosDatabase);
    }
}
