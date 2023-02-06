package LinwinVOS.Users.api;

import LinwinVOS.LinwinVOS;
import LinwinVOS.Users.Users;
import LinwinVOS.Users.UsersFileSystem;
import LinwinVOS.Users.logon;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class Sudo {
    public String createUser(String userName) {
        /**
         * This API sets up databases on the cloud.
         * This API can bypass root and execute some database system-level command lines.
         * But cannot delete, look up and create database or data.
         */
        UsersFileSystem usersFileSystem = new UsersFileSystem();
        usersFileSystem.setUserName(userName);
        usersFileSystem.setLoadOK(true);

        Users users = new Users();
        users.setName(userName);
        users.setCreateTime(this.getNowTime());
        users.setPasswd(this.getRandomPasswd());

        String userConfig  = "{\n" +
                "  \"name\" : \""+userName+"\",\n" +
                "  \"Create Time\" : \""+this.getNowTime()+"\",\n" +
                "  \"Passwd\" : \""+users.getPasswd()+"\"\n" +
                "}\n";

        if (LinwinVOS.FileSystem.get(userName) != null) {
            return null;
        }
        try{
            LinwinVOS.FileSystem.put(userName,usersFileSystem);
            File target = new File(LinwinVOS.DatabasePath+"/"+userName+"/user.json");
            File userRoot = new File(LinwinVOS.DatabasePath+"/"+userName);
            File databaseRoot = new File(LinwinVOS.DatabasePath+"/"+userName+"/Database");

            userRoot.mkdir();
            target.createNewFile();
            databaseRoot.mkdir();

            FileWriter fileWriter = new FileWriter(target,false);
            fileWriter.write(userConfig);
            fileWriter.close();

            return users.getPasswd();
        }catch (Exception exception){
            return null;
        }
    }
    public boolean deleteUser(String userName,String passwd) {
        if (userName.equals("root")) {
            return false;
        }else {
            if (logon.Logon_LinwinVOS(userName,logon.md5(passwd))) {
                LinwinVOS.usersFileSystems.remove(userName);
                Sudo.deleteDirectory(LinwinVOS.DatabasePath+"/"+userName);
                return true;
            }else {
                return false;
            }
        }
    }
    public String getNowTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd.HH:mm:ss");
        java.util.Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
    public String getRandomPasswd() {
        Random random = new Random(10);
        int passwd = random.nextInt(100000);
        return String.valueOf(passwd);
    }
    public String dealCommand(String command) {
        /**
         * 'sudo' command:
         * This command is to use the api the finish the options.
         *
         * THIS COMMAND IS TO CREATE A NEW USERS IN DATABASE
         * [1] sudo createUser 'name'
         *
         * This command is to delete a user 'name' , the first input is Username,the second input is passwd.
         * [2] sudo deleteUser 'name' '123456'
         *
         */
        try{
            String options = command.substring(command.indexOf("sudo ")+5);
            options = options.replace("  ","");

            String shell = options.split(" ")[0];
            if (shell.equals("createUser")) {
                String getName = options.substring(options.indexOf("'")+1,options.lastIndexOf("'"));
                if (LinwinVOS.FileSystem.get(getName) == null) {
                    String NewPasswd = this.createUser(getName);
                    if (NewPasswd != null) {
                        return NewPasswd+"\n";
                    }else {
                        return "Create Error!";
                    }
                }else {
                    return "User was exists!";
                }
            }
            if (shell.equals("deleteUser")) {
                String getName = options.split("' '")[0].substring(options.indexOf("'")+1);
                String splitTWO = options.split("' '")[1];
                String getPasswd = splitTWO.substring(0,splitTWO.lastIndexOf("'"));

                if (this.deleteUser(getName,getPasswd)) {
                    return "Delete Successful!\n";
                }else {
                    return "Delete Error!";
                }
            }
            else {
                return "Error command and shell";
            }
        }catch (Exception exception){
            //exception.printStackTrace();
            return "Command syntax error!";
        }
    }
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    public static boolean deleteDirectory(String dir) {
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            return false;
        }
        boolean flag = true;
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            return false;
        }
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }
}
