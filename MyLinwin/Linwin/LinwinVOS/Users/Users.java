package LinwinVOS.Users;

import java.util.Arrays;
import java.util.HashSet;

public class Users {
    private String username;
    private String createTime;
    private HashSet<String> GrantPermissions;
    private String passwd;

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
    public void setName(String name) {
        this.username = name;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public void setGrantPermissions(String jsonContent) {
        try{
            String[] getUser = jsonContent.split(",");
            this.GrantPermissions = new HashSet<String>(Arrays.asList(getUser));
        }catch (Exception exception){
            System.out.println("[ERROR] A Error Message of the Json config file write error: "+jsonContent);
        }
    }
    public String getUserName() {
        return this.username;
    }
    public String getCreateTime() {
        return this.createTime;
    }
    public HashSet<String> getGrantPermissions() {
        return this.GrantPermissions;
    }
    public String getPasswd() {
        return this.passwd;
    }
}
