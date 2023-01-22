import LinwinVOS.FileSystem.Data;
import LinwinVOS.FileSystem.VosDatabase;
import LinwinVOS.LinwinVOS;
import LinwinVOS.Users.UsersFileSystem;
import ThreadSocket.ThreadSocket;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
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
    public static String getFileContent(String name) throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
        FileInputStream fileInputStream = new FileInputStream(name);
        FileChannel fileChannel = fileInputStream.getChannel();
        int length = fileChannel.read(byteBuffer);
        StringBuffer fileContent = new StringBuffer("");
        while ((length != -1)) {
            byteBuffer.flip();
            byte[] bytes = byteBuffer.array();
            String s = new String(bytes, Charset.defaultCharset());
            try {
                fileContent.append(s);
                fileContent.append("\n");
            } catch (Exception exception) {
                try {
                    Thread.sleep(100);
                    continue;
                } catch (Exception exception1) {
                    exception1.printStackTrace();
                }
            }
            byteBuffer.clear();
            length = fileChannel.read(byteBuffer);
        }
        return fileContent.toString();
    }
    public static String getLastName(String str) {
        try{
            return str.substring(str.lastIndexOf("."),str.length());
        }catch (Exception exception){
            return "";
        }
    }
    public static String onlyGetName(String str) {
        try{
            return str.substring(0,str.lastIndexOf("."));
        }catch (Exception exception){
            return str;
        }
    }
}
