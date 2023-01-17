package LinwinVOS.FileSystem;

import sun.util.resources.cldr.zh.CalendarData_zh_Hans_HK;

import java.io.File;
import java.util.*;

public class VosDatabase {
    private String Name;
    private String savePath;
    private String user;
    private int size;
    private String createTime;
    private String ModificationTime;
    private String value;
    private HashMap<String,Data> dataHashMap = new HashMap<String,Data>();
    private List<Data> dataList = new ArrayList<Data>();
    public VosDatabase() {
    }
    public void setName(String name) {
        this.Name = name;
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
    public Data getValue(String name) {
        return this.dataHashMap.get(name);
    }
    public List<Data> getListData() {
        return this.dataList;
    }

    public static boolean getRealFileExists(String var0) {
        File var1 = new File(var0);
        return var1.exists();
    }
}
