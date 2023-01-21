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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataLoader {

    public static void loadData() {
        String databasePath = LinwinVOS.DatabasePath;
        //System.out.println("value="+userList.length);
        for (int i = 0; i < LinwinVOS.usersFileSystems.size() ; i++) {
            int I = i;
            /**
             * Use different thread to load users databases and data.
             */
            File usersDatabase = new File(databasePath+"/"+LinwinVOS.usersFileSystems.get(I).getUserName()+"/Database");
            File[] listDataBase = usersDatabase.listFiles();
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try{
                        long start = System.currentTimeMillis();
                        DataLoader.UsersLoad(listDataBase,LinwinVOS.usersFileSystems.get(I),LinwinVOS.usersFileSystems.get(I).getUserName());
                        long end = System.currentTimeMillis();
                        System.out.println("[*]Finish Load All the Data From User: "+LinwinVOS.usersFileSystems.get(I).getUserName()+" Use Time: "+(end-start)+"ms");
                        LinwinVOS.usersFileSystems.get(I).setLoadOK(true);
                    }catch (Exception exception){
                        exception.printStackTrace();
                    }
                }
            });
            thread.start();
        }
    }
    private static void UsersLoad(File[] listDataBase,UsersFileSystem usersFileSystem,String user) {
        for (int i = 0 ; i < listDataBase.length ; i++)
        {
            int I = i;
            if (listDataBase[i].isFile()) {
                if (DataLoader.getLastname(listDataBase[i].getName()).equals(".mydb")) {
                    /**
                     * Use asynchronous to load all the users' databases.
                     */
                    VosDatabase vosDatabase = new VosDatabase();
                    vosDatabase.setUser(user);
                    vosDatabase.setName(listDataBase[I].getName().substring(0,listDataBase[I].getName().lastIndexOf(".")));
                    vosDatabase.setCreateTime(base.getFileCreateTime(listDataBase[I].getAbsolutePath()));
                    vosDatabase.setModificationTime(base.getFileUpdateTime(listDataBase[I].getAbsolutePath()));
                    vosDatabase.setSavePath("/");

                    /**
                     * Put the data to the database.
                     */
                    Thread LoadDatabaseThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            dbLoader loader = new dbLoader();
                            List<Data> list = loader.LoadDB(listDataBase[I].getAbsolutePath());
                            for (int j = 0 ; j < list.size() ; j++){
                                //System.out.println("Name="+list.get(j).getName()+" ; Value="+list.get(j).getValue());
                                vosDatabase.putData(list.get(j).getName(),list.get(j));
                            }
                            String name = listDataBase[I].getName().substring(0,listDataBase[I].getName().lastIndexOf("."));
                            usersFileSystem.putDatabase(name,vosDatabase);
                        }
                    });
                    LoadDatabaseThread.start();
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
