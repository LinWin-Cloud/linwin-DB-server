package MirrorRuntime;

public class Engine {
    private String message;
    public void exec(String command) {
        MirrorExec mirrorExec = new MirrorExec();
        try
        {
            if (command.equals("list database")) {
                this.message = mirrorExec.Show_Database();
                return;
            }
            else if (command.equals("test connection"))
            {
                this.message = "Successful Connection";
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
}
