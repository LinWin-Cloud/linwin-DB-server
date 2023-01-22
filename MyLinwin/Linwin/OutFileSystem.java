import LinwinVOS.FileSystem.Data;
import LinwinVOS.FileSystem.VosDatabase;
import LinwinVOS.LinwinVOS;
import LinwinVOS.Users.UsersFileSystem;
import ThreadSocket.ThreadSocket;
import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class OutFileSystem {
    private  LinwinVOS linwinVOS;
    private ThreadSocket threadSocket;
    public void setLinwinVOS(LinwinVOS linwinVOS) {
        this.linwinVOS = linwinVOS;
    }
    public void run() {
        HashSet<UsersFileSystem> hashSet = this.linwinVOS.getUserFileSystem();
        for (UsersFileSystem usersFileSystem : hashSet)
        {
            ExecutorService executorService = Executors.newFixedThreadPool(1);
            Future<Integer> future = executorService.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    try{
                        HashSet<VosDatabase> databases = usersFileSystem.getDatabase();
                        for (VosDatabase vosDatabase:databases) {
                            String  DatabaseName = vosDatabase.getName();
                            FileWriter fileWriter = new FileWriter(LinwinVOS.DatabasePath+"/"+usersFileSystem.getUserName()+"/Database/"+DatabaseName+".mydb",true);
                            FileWriter fw = new FileWriter(LinwinVOS.DatabasePath+"/"+usersFileSystem.getUserName()+"/Database/"+DatabaseName+".mydb",false);
                            fw.write("");
                            fw.close();
                            for (Data data : vosDatabase.dataHashMap.values()) {
                                String content = "Name="+data.getName()+"&Value="+data.getValue()+"&Type="+data.getType()+"&createTime="+data.getCreateTime()+"&ModificationTime="+data.getModificationTime()+"&note="+data.getNote()+"\n";
                                fileWriter.write(content);
                            }
                            fileWriter.close();
                            //System.out.println(getWriteContent);
                        }
                    }catch (Exception exception){
                        exception.printStackTrace();
                    }
                    return 0;
                }
            });
            executorService.shutdown();
        }
    }
    public Boolean getAllUserLoad_STATUS() {
        HashSet<UsersFileSystem> usersFileSystemHashSet = this.linwinVOS.getUserFileSystem();
        Boolean ok = true;
        for (UsersFileSystem usersFileSystem : usersFileSystemHashSet)
        {
            if (usersFileSystem.getLoadOk()) {
                continue;
            }else {
                ok = false;
                break;
            }
        }
        return ok;
    }
    public void setThreadSocket(ThreadSocket threadSocket) {
        this.threadSocket = threadSocket;
    }
    public static void writeFile(String name,String content,FileWriter fileWriter) {
        try{
            name = name.replace("//","/");
            File writeFile = new File(name);
            if (!writeFile.exists()) {
                writeFile.createNewFile();
            }
            fileWriter.write(content);
            fileWriter.flush();
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
}
