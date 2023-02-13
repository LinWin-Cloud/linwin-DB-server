package LinwinVOS;
import LinwinVOS.FileSystem.Data;
import LinwinVOS.FileSystem.VosDatabase;
import LinwinVOS.Users.UsersFileSystem;
import LinwinVOS.data.Json;
import LinwinVOS.runtime.dbLoader;
import LinwinVOS.data.base;

import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DataLoader {

    public static void loadData() {
        String databasePath = LinwinVOS.DatabasePath;
        //System.out.println("value="+userList.length);
        //Collection<UsersFileSystem> list = LinwinVOS.FileSystem.values();
        for (int i = 0 ; i < LinwinVOS.usersFileSystems.size() ;i++) {
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
                        LinwinVOS.FileSystem.get(LinwinVOS.usersFileSystems.get(I).getUserName()).setLoadOK(true);
                    }catch (Exception exception){
                        exception.printStackTrace();
                    }
                }
            });
            thread.start();
        }
    }
    public static void UsersLoad(File[] listDataBase, UsersFileSystem usersFileSystem, String user) {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        Future<Integer> future = null;
        for (int i = 0 ; i < listDataBase.length ; i++)
        {
            int I = i;
            future = executorService.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    try{
                        Thread.sleep(50);
                    }catch (Exception exception){
                        exception.printStackTrace();
                    }
                    if (listDataBase[I].isFile()) {
                        if (DataLoader.getLastname(listDataBase[I].getName()).equals(".mydb")) {
                            /**
                             * Use asynchronous to load all the users' databases.
                             */
                            try{
                                VosDatabase vosDatabase = new VosDatabase();
                                vosDatabase.setUser(user);
                                vosDatabase.setName(listDataBase[I].getName().substring(0,listDataBase[I].getName().lastIndexOf(".")));
                                vosDatabase.setCreateTime(base.getFileCreateTime(listDataBase[I].getAbsolutePath()));
                                vosDatabase.setModificationTime(Json.readJson(listDataBase[I].getAbsolutePath(),"Update"));
                                vosDatabase.setSavePath("/Database/"+user+"/"+vosDatabase.getName());
                                vosDatabase.setRemoteDatabase(false);

                                LinwinVOS.outPutMap.put(vosDatabase.getName(),new FileWriter(LinwinVOS.DatabasePath+"/"+user+"/Database/"+vosDatabase.getName()+".mydb",true));
                                /**
                                 * Put the data to the database.
                                 */
                                dbLoader loader = new dbLoader();
                                HashSet<Data> list = loader.LoadDB(listDataBase[I].getAbsolutePath(),vosDatabase.getSavePath(),executorService);
                                for (Data data:list){
                                    //System.out.println("Name="+list.get(j).getName()+" ; Value="+list.get(j).getValue());
                                    vosDatabase.putData(data.getName(),data);
                                }
                                String name = listDataBase[I].getName().substring(0,listDataBase[I].getName().lastIndexOf("."));
                                usersFileSystem.putDatabase(name,vosDatabase);
                            }catch (Exception exception){
                                exception.printStackTrace();
                            }
                        }
                    }
                    return 0;
                }
            });
        }
        try{
            future.get();
            executorService.shutdown();
        }catch (Exception exception){

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
