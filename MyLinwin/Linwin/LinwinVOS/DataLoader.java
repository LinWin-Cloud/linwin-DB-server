package LinwinVOS;
import LinwinVOS.FileSystem.Data;
import LinwinVOS.FileSystem.VosDatabase;
import LinwinVOS.Users.UsersFileSystem;
import LinwinVOS.Users.logon;
import LinwinVOS.data.Json;
import LinwinVOS.runtime.dbLoader;
import LinwinVOS.data.base;

import java.io.File;
import java.util.List;

public class DataLoader {

    public static void loadData() {
        String databasePath = LinwinVOS.DatabasePath;
        //System.out.println("value="+userList.length);
        for (int i = 0; i < LinwinVOS.usersFileSystems.size() ; i++) {
            File usersDatabase = new File(databasePath+"/"+LinwinVOS.usersFileSystems.get(i).getUserName()+"/Database");
            File[] listDataBase = usersDatabase.listFiles();
            DataLoader.UsersLoad(listDataBase,LinwinVOS.usersFileSystems.get(i),LinwinVOS.usersFileSystems.get(i).getUserName());
        }
    }
    private static void UsersLoad(File[] listDataBase,UsersFileSystem usersFileSystem,String user) {
        for (int i = 0 ; i < listDataBase.length ; i++)
        {
            if (listDataBase[i].isFile()) {
                if (DataLoader.getLastname(listDataBase[i].getName()).equals(".mydb")) {
                    VosDatabase vosDatabase = new VosDatabase();
                    vosDatabase.setUser(user);
                    vosDatabase.setName(listDataBase[i].getName().substring(0,listDataBase[i].getName().lastIndexOf(".")));
                    vosDatabase.setCreateTime(base.getFileCreateTime(listDataBase[i].getAbsolutePath()));
                    vosDatabase.setModificationTime(base.getFileUpdateTime(listDataBase[i].getAbsolutePath()));
                    vosDatabase.setSavePath("/");
                    /**
                     * Put the data to the database.
                     */
                    List<Data> list = dbLoader.LoadDB(listDataBase[i].getAbsolutePath());
                    for (int j = 0 ; j < list.size() ; j++){
                        //System.out.println("Name="+list.get(j).getName()+" ; Value="+list.get(j).getValue());
                        vosDatabase.putData(list.get(j).getName(),list.get(j));
                    }
                    String name = listDataBase[i].getName().substring(0,listDataBase[i].getName().lastIndexOf("."));
                    usersFileSystem.putDatabase(name,vosDatabase);
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
