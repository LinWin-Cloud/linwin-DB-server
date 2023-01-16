package LinwinVOS;
import LinwinVOS.FileSystem.Data;
import LinwinVOS.FileSystem.VMDirectory;
import LinwinVOS.FileSystem.VosFiles;
import LinwinVOS.Users.logon;
import LinwinVOS.runtime.dbLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {

    public static void loadData() {
        String databasePath = LinwinVOS.DatabasePath;
        String[] userList = logon.UsersList.toArray(new String[logon.UsersList.size()]);
        for (int i = 0; i < userList.length ; i++) {
            File usersDatabase = new File(databasePath+"/"+userList[i]+"/");
            File[] listDataBase = usersDatabase.listFiles();
            DataLoader.UsersLoad(listDataBase,"/");
        }
    }
    private static void UsersLoad(File[] listDataBase,String savePath) {
        for (int i = 0 ; i < listDataBase.length ; i++)
        {
            if (listDataBase[i].isDirectory()) {
                VosFiles vosFiles = new VosFiles();
                vosFiles.setName(listDataBase[i].getName());
                vosFiles.setSavePath(savePath+"/"+listDataBase[i].getName());
                vosFiles.setType("DataBaseList");
                VMDirectory vmDirectory = new VMDirectory();
                vosFiles.setDirectory(vmDirectory);

                LinwinVOS.FileSystem.put(listDataBase[i].getName(),vosFiles);
                File file = listDataBase[i];
                File[] files = file.listFiles();
                DataLoader.UsersLoad(files,savePath+"/"+listDataBase[i].getName());
                continue;
            }else {
                if(DataLoader.getLastname(listDataBase[i].getName()).equals(".mydb")) {
                    VosFiles vosFiles = new VosFiles();
                    vosFiles.setName(listDataBase[i].getName());
                    vosFiles.setType("DataBase");
                    vosFiles.setSavePath(savePath+"/"+listDataBase[i].getName());

                    List<Data> dataList = dbLoader.LoadDB(vosFiles);
                }
                continue;
            }
        }
    }
    public static String getLastname(String name) {
        try{
            return name.substring(name.lastIndexOf("."),name.length());
        }catch (Exception exception){
            return "";
        }
    }
}
