package remote;

import java.util.HashMap;

public class MirrorSystem {
    private String key;
    private String MirrorName;
    private HashMap<String,Database> databaseHashMap = new HashMap<>();

    public void setKey(String key) {
        this.key = key;
    }
    public void setMirrorName(String mirrorName) {
        this.MirrorName = mirrorName;
    }
    public String getKey() {
        return this.key;
    }
    public String getMirrorName() {
        return this.MirrorName;
    }
    public void putDatabase(String mirrorName,Database database) {
        try{
            this.databaseHashMap.put(mirrorName,database);
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    public Database getData(String name) {
        return this.databaseHashMap.get(name);
    }
}
