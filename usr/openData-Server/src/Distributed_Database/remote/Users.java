package remote;

import java.util.HashMap;

public class Users {
    private String key;
    private String MirrorName;
    private HashMap<String,Data> dataHashMap = new HashMap<>();

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
    public void putData(String mirrorName,Data data) {
        try{
            this.dataHashMap.put(mirrorName,data);
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
