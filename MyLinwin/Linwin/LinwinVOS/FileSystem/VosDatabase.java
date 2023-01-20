package LinwinVOS.FileSystem;

import java.io.File;
import java.util.*;

public class VosDatabase {
    private String Name;
    private String savePath;
    private String user;
    private int size;
    private int id;
    private String createTime;
    private String ModificationTime;
    private String value;
    private HashMap<String,Data> dataHashMap = new HashMap<String,Data>();
    private List<Data> dataList = new ArrayList<Data>();
    public VosDatabase() {
    }
    public void setName(String name) {
        if (name.indexOf(" ") != -1) {
            this.Name = name.replace(" ","");
        }else {
            this.Name = name;
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
    public int getSize() {
        return this.dataHashMap.size();
    }
    public void putData(String name,Data data) {
        this.dataHashMap.put(name,data);
        this.dataList.add(data);
    }
    public Data getData(String name) {
        return this.dataHashMap.get(name);
    }
    public List<Data> getListData() {
        //return new HashSet<>(this.dataHashMap.values());
        return this.dataList;
    }
    public String getName() {
        return this.Name;
    }

    public static boolean getRealFileExists(String var0) {
        File var1 = new File(var0);
        return var1.exists();
    }
    public void removeData(String name) {
        this.dataList.remove(this.dataHashMap.get(name));
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

    public HashSet<Data> getDataSet() {
        return new HashSet<>(this.dataList);
    }
}
