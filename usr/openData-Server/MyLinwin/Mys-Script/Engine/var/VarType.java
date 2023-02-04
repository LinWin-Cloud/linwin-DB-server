package Engine.var;

public class VarType {
    private String name;
    private String type;
    private String value;

    public void setName(String name) {
        this.name = name;
    }
    public void setValue(String value) {
        this.type = VarType.getType(value);
        this.value = value;
    }
    public String getType() {
        return this.type;
    }
    public String getName() {
        return this.name;
    }
    public String getValue() {
        return this.value;
    }
    public static String getType(String content) {
        String saveType = "string";
        try{
            int i = Integer.valueOf(content);
            return "int";
        }catch (Exception exception){
            saveType = "string";
        }
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
            String s = String.valueOf(content);
            return "string";
        }catch (Exception exception){
            saveType = "string";
        }
        return saveType;
    }
}
