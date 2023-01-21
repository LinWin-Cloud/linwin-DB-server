package LinwinVOS.Users;

import LinwinVOS.FileSystem.VosDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class UsersFileSystem {
    private String userName;
    private HashMap<String, VosDatabase> userDatabase = new HashMap<String,VosDatabase>();
    private Boolean loadOK = false;

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void putDatabase(String name,VosDatabase vosDatabase) {
        this.userDatabase.put(name,vosDatabase);
    }
    public HashSet<VosDatabase> getDatabase() {
        return new HashSet<>(userDatabase.values());
    }
    public String getUserName() {
        return this.userName;
    }
    public VosDatabase get(String key) {
        return this.userDatabase.get(key);
    }
    public Boolean getLoadOk() {
        return this.loadOK;
    }
    public void setLoadOK(Boolean bool) {
        this.loadOK = bool;
    }
}
