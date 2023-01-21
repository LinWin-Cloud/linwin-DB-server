import LinwinVOS.FileSystem.VosDatabase;
import LinwinVOS.LinwinVOS;
import LinwinVOS.Users.UsersFileSystem;
import ThreadSocket.ThreadSocket;
import sun.java2d.loops.FillRect;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;

public class OutFileSystem {
    private  LinwinVOS linwinVOS;
    private ThreadSocket threadSocket;
    public void setLinwinVOS(LinwinVOS linwinVOS) {
        this.linwinVOS = linwinVOS;
    }
    public void run() {
        while (true) {
            try{
                Thread.sleep(20);
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
            String users = usersFileSystem.getUserName();
            HashSet<VosDatabase> databases = usersFileSystem.getDatabase();
            for (VosDatabase vosDatabase:databases) {
                String DatabaseName = vosDatabase.getName();

            }
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
    public void writeFile(String name,String content) {
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
