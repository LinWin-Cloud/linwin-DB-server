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
            if (command.equals("update")) {
                this.message = mirrorExec.update();
            }
            else if (command.equals("test connection"))
            {
                this.message = "Successful Connection";
            }
            else if (command.substring(0,2).equals("ls"))
            {
                this.message = mirrorExec.LS_Database(command);
            }
            else if (command.substring(0,3).equals("get"))
            {
                this.message = mirrorExec.get(command);
            }
            else if (command.substring(0,4).equals("view")) {
                this.message = mirrorExec.viewDatabase(command);
            }
            else if (command.substring(0,6).equals("redata"))
            {
                this.message = mirrorExec.reData(command);
            }
            else if (command.substring(0,6).equals("delete"))
            {
                this.message = mirrorExec.delete(command);
            }
            else if (command.substring(0,6).equals("create"))
            {
                this.message = mirrorExec.create(command);
            }
            else if (command.substring(0,7).equals("existdb")) {
                this.message = mirrorExec.existDatabase(command);
            }
            else if (command.substring(0,8).equals("findData"))
            {
                this.message = mirrorExec.findData(command);
            }
            else {
                this.message = "Error command and shell;";
            }
        }
        catch (Exception exception)
        {
            //exception.printStackTrace();
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
