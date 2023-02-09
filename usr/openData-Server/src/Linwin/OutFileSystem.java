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
    public OutFileSystem() {
        Thread outData = new Thread(new Runnable() {
            @Override
            public void run() {
                OutFileSystem outFileSystem = new OutFileSystem();
                outFileSystem.setLinwinVOS(MyLinwin.linwinVOS);
                while (true){
                    try{
                        Thread.sleep(100);
                        if (outFileSystem.getAllUserLoad_STATUS()) {
                            break;
                        }
                    }catch (Exception exception){
                        exception.printStackTrace();
                    }
                }
                int getSleepTime = 0;
                while (true) {
                    try{
                        int getDataSize = MyLinwin.linwinVOS.getDataSize();
                        if (getDataSize <= 100) {
                            getSleepTime = 150;
                        }else if (getDataSize > 100 && getDataSize <= 500) {
                            getSleepTime = 250;
                        }else if(getDataSize > 500 && getDataSize <=1000) {
                            getSleepTime = 1000;
                        }else if(getDataSize > 1000 && getDataSize <=1000) {
                            getSleepTime = 2000;
                        }else if(getDataSize > 1000 && getDataSize <= 10000) {
                            getSleepTime = 40000;
                        }else if (getDataSize > 10000 && getDataSize <= 100000) {
                            getSleepTime = 120000;
                        }else if (getDataSize > 100000 && getDataSize <= 1000000) {
                            getSleepTime = 240000;
                        }else if (getDataSize > 1000000) {
                            getSleepTime = 300000;
                        }

                        outFileSystem.run();
                        Thread.sleep(getSleepTime);
                    }catch (Exception exception){
                        exception.printStackTrace();
                    }
                }
            }
        });
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
                            FileWriter fileWriter = new FileWriter(LinwinVOS.DatabasePath+"/"+usersFileSystem.getUserName()+"/Database/"+DatabaseName+".mydb",false);
                            //FileWriter fw = new FileWriter(LinwinVOS.DatabasePath+"/"+usersFileSystem.getUserName()+"/Database/"+DatabaseName+".mydb",false);
                            //fw.write("");
                            //fw.close();
                            StringBuffer stringBuffer = new StringBuffer("");
                            for (Data data : vosDatabase.dataHashMap.values()) {
                                String content = "Name="+data.getName()+"&Value="+data.getValue()+"&Type="+data.getType()+"&createTime="+data.getCreateTime()+"&ModificationTime="+data.getModificationTime()+"&note="+data.getNote()+"\n";
                                stringBuffer.append(content);
                            }
                            fileWriter.write(stringBuffer.toString());
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
