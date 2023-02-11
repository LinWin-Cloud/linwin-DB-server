package MirrorRuntime;

public class Engine {
    private String message;
    public void exec(String command) {
        MirrorExec mirrorExec = new MirrorExec();
        try
        {
            command = Engine.replaceSpace(command);
            command = Engine.replaceLastSpace(command);
            if (command.equals("list database")) {
                this.message = mirrorExec.Show_Database();
                return;
            }
            else if (command.equals("test connection"))
            {
                this.message = "Successful Connection";
            }
            else if (command.substring(0,2).equals("ls"))
            {

            }
        }
        catch (Exception exception)
        {
            this.message = "Error Command and Shell";
            return;
        }
    }
    public String getMessage() {
        return this.message;
    }
    public static String replaceSpace(String str) {
        int length = str.length();
        String getSpaceStr = null;
        for (int i = 0 ; i < length ;i++) {
            String charset = str.substring(i,i+1);
            if (!charset.equals(" ")) {
                String Code = str.substring(str.indexOf(charset),length);
                getSpaceStr = Code;
                break;
            }
        }
        return getSpaceStr;
    }
    public static String replaceLastSpace(String str) {
        int length = str.length();
        String getSpaceStr = null;
        for (int i = 0 ; i < length ;i++) {
            String charset = str.substring(length-i-1,length-i);
            if (!charset.equals(" ")) {
                String Code = str.substring(0,str.lastIndexOf(charset)+1);
                getSpaceStr = Code;
                break;
            }
        }
        return getSpaceStr;
    }
}
