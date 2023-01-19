package LinwinVOS.Users;

import LinwinVOS.FileSystem.VosDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class UsersFileSystem {
    private String userName;
    private HashMap<String, VosDatabase> userDatabase = new HashMap<String,VosDatabase>();
    private List<VosDatabase> databases = new ArrayList<VosDatabase>();

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void putDatabase(String name,VosDatabase vosDatabase) {
        this.userDatabase.put(name,vosDatabase);
        this.databases.add(vosDatabase);
    }
    public HashSet<VosDatabase> getDatabase() {
        try{
            return new HashSet<>(userDatabase.values());
        }catch (Exception exception){
            return new HashSet<>();
        }
    }
    public String getUserName() {
        return this.userName;
    }
}
