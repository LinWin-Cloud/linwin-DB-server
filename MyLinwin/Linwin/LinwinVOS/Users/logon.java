package LinwinVOS.Users;

import LinwinVOS.LinwinVOS;
import LinwinVOS.data.Json;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;

public class logon {
    public static HashMap<String,Users> UserInformation = new HashMap<String,Users>();
    public static HashSet<String> UsersList = new HashSet<>();
    public static String setPath = "";

    public static void LoadUsers() {
        File nowPath = new File(logon.setPath);
        File TargetDataPath = new File(nowPath.getAbsolutePath()+"/../../Data/");
        System.out.println(TargetDataPath.getAbsolutePath());
        if (TargetDataPath.isDirectory() && TargetDataPath.exists()) {
            File[] listUsers = TargetDataPath.listFiles();
            for (int i = 0 ; i < listUsers.length ; i++) {
                if (listUsers[i].isDirectory()) {
                    File userConfig = new File(listUsers[i].getAbsolutePath()+"/user.json");
                    if (userConfig.exists() && userConfig.isFile()) {
                        String UsersName = listUsers[i].getName();
                        String createTime = Json.readJson(userConfig.getAbsolutePath(),"Create Time");
                        String GrantPermissions = Json.readJson(userConfig.getAbsolutePath(),"Grant Permissions");
                        String Passwd = Json.readJson(userConfig.getAbsolutePath(),"Passwd");

                        File database = new File(listUsers[i].getAbsolutePath()+"/Database");
                        if (database.isDirectory() && database.exists()) {
                            Users users = new Users();
                            users.setName(UsersName);
                            users.setCreateTime(createTime);
                            users.setPasswd(Passwd);
                            users.setGrantPermissions(GrantPermissions);

                            logon.UserInformation.put(UsersName,users);
                            logon.UsersList.add(UsersName);
                        }else {
                            continue;
                        }
                    }else {
                        continue;
                    }
                }
            }
        }else {
            System.out.println("[ERROR] Linwin Data Server Error:");
            System.out.println("" +
                    " Users config and Data was error.");
            System.exit(0);
        }
    }
    public static String md5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No This Md5 Function");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }
    public static Boolean Logon_LinwinVOS(String username,String passwd) {
        Users users = logon.UserInformation.get(username);
        if (users == null) {
            return false;
        }
        String getPasswd_MD5 = logon.md5(users.getPasswd());
        if (getPasswd_MD5.equals(passwd)) {
            LinwinVOS.UsersNowPath.put(username,"/");
            return true;
        }else {
            return false;
        }
    }
}
