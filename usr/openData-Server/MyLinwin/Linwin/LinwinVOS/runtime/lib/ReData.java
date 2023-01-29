package LinwinVOS.runtime.lib;

public class ReData {
    public String reData(String user,String command) {
        try{
            String TMP = command;
            TMP = TMP.replace("  ","");

            String data = TMP.substring(TMP.indexOf("'")+1,TMP.indexOf("'."));
            TMP = TMP.substring(TMP.indexOf("'.")+2);
            String type = TMP.substring(0,TMP.indexOf(" "));
            String content = TMP.substring(TMP.indexOf("'")+1,TMP.lastIndexOf("'"));
            String database = TMP.substring(TMP.lastIndexOf("in ")+3);
            
        }catch (Exception exception){
            return "Command syntax error!";
        }
    }
}
