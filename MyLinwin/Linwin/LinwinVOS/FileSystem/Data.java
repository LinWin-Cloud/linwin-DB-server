package LinwinVOS.FileSystem;

public class Data {
    private String name;
    private String saveDatabase;
    private String createTime;
    private String ModificationTime;
    private int id;
    private String value;
    private String Type;
    private String note;
    public void setName(String name) {
        this.name = name;
    }
    public void setSaveDatabase(String saveDatabase) {
        this.saveDatabase = saveDatabase;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public void setModificationTime(String modificationTime) {
        this.ModificationTime = modificationTime;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setValue(String value) {
        this.autoSaveType(value);
        this.value = value;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public void autoSaveType(String str) {
        this.Type = this.getDataType(str);
    }

    public String getDataType(String content) {
        String saveType = "string";
        try{
            double d = Double.valueOf(content);
            return "double";
        }catch (Exception exception){
            saveType = "string";
        }
        try{
            Float f = Float.valueOf(content);
            return "float";
        }catch (Exception exception){
            saveType = "string";
        }
        try{
            Boolean bool = Boolean.valueOf(content);
            return "boolean";
        }catch (Exception exception){
            saveType = "string";
        }
        try{
            int i = Integer.valueOf(content);
            return "int";
        }catch (Exception exception){
            saveType = "string";
        }
        try{
            String s = String.valueOf(content);
            return "string";
        }catch (Exception exception){
            saveType = "string";
        }
        return saveType;
    }
    public int getId() {
        return this.id;
    }
    public String getValue() {
        return this.value;
    }
    public String getType() {
        return this.Type;
    }
    public String getCreateTime() {
        return this.createTime;
    }
    public String getSaveDatabase() {
        return this.saveDatabase;
    }
    public String getName() {
        return this.name;
    }
    public String getModificationTime() {
        return this.ModificationTime;
    }
    public String getNote() {return this.note;}
}
