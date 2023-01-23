package LinwinVOS.FileSystem;

import java.io.File;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class VosDatabase {
    private String Name;
    private String savePath;
    private String user;
    private int size;
    private int id;
    private String createTime;
    private String ModificationTime;
    private String value;
    public HashMap<String,Data> dataHashMap = new HashMap<String,Data>();
    public VosDatabase() {
    }
    public void setName(String name) {
        int charsetA = name.indexOf("'");
        int charsetB = name.indexOf("\"");
        int charsetC = name.indexOf("/");
        int charsetD = name.indexOf(",");

        if (charsetA != -1 || charsetB != -1 || charsetC != -1 || charsetD != -1){
            return;
        }else {
            if (name.indexOf(" ") != -1) {
                this.Name = name.replace(" ","");
            }else {
                this.Name = name;
            }
        }
    }
    public void setUser(String user) {
        this.user = user;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public void setModificationTime(String modificationTime) {
        this.ModificationTime = modificationTime;
    }
    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }
    public void putData(String name,Data data) {
        this.dataHashMap.put(name,data);
    }
    public Data getData(String name) {
        return this.dataHashMap.get(name);
    }
    public HashSet<Data> getListData() {
        return new HashSet<>(this.dataHashMap.values());
        //return this.dataList;
    }
    public String getName() {
        return this.Name;
    }

    public static boolean getRealFileExists(String var0) {
        File var1 = new File(var0);
        return var1.exists();
    }
    public void removeData(String name) {
        this.dataHashMap.remove(name);
    }
    public String getCreateTime() {
        return this.createTime;
    }
    public String getModificationTime() {
        return this.ModificationTime;
    }
    public String getSavePath() {
        return this.savePath;
    }
    public int getSize() {
        return this.dataHashMap.size();
    }
    public StringBuffer findData(String index) {
        StringBuffer stringBuffer = new StringBuffer("");
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<Integer> future = null;
        for (Data data : this.dataHashMap.values()) {
            future = executorService.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    int s = data.getName().indexOf(index);
                    if (s != -1) {
                        stringBuffer.append(data.getName());
                        stringBuffer.append("\n");
                    }
                    return 0;
                }
            });
        }
        try{
            future.get();
        }catch (Exception exception){
            exception.printStackTrace();
        }
        executorService.shutdown();
        return stringBuffer;
    }
}
