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
        while (true) {
            try{
                Thread.sleep(100);
                if (this.getAllUserLoad_STATUS()) {
                    break;
                }
            }catch (Exception exception){
                exception.printStackTrace();
            }
        }
        HashSet<UsersFileSystem> hashSet = this.linwinVOS.getUserFileSystem();
        for (UsersFileSystem usersFileSystem : hashSet)
        {
            ExecutorService executorService = Executors.newFixedThreadPool(1);
            Future<Integer> future = executorService.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    String users = usersFileSystem.getUserName();
                    HashSet<VosDatabase> databases = usersFileSystem.getDatabase();
                    for (VosDatabase vosDatabase:databases) {
                        String DatabaseName = vosDatabase.getName();
                        String getWriteContent = vosDatabase.getOutputData();
                        OutFileSystem.writeFile(LinwinVOS.DatabasePath+"/"+users+"/"+DatabaseName+".mydb",getWriteContent);
                    }
                    return 0;
                }
            });
            executorService.shutdown();
        }
    }
    private Boolean getAllUserLoad_STATUS() {
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
    public static void writeFile(String name,String content) {
        try{
            File writeFile = new File(name);
            if (!writeFile.exists() || !writeFile.isFile()) {
                writeFile.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(writeFile);
            fileWriter.write(content);
            fileWriter.flush();
            fileWriter.close();
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
}
