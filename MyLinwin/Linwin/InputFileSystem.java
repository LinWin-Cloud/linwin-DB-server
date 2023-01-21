import LinwinVOS.LinwinVOS;
import ThreadSocket.ThreadSocket;

public class InputFileSystem {
    private ThreadSocket threadSocket;
    private LinwinVOS linwinVOS;

    public void setThreadSocket(ThreadSocket threadSocket) {
        this.threadSocket = threadSocket;
    }
    public void setLinwinVOS(LinwinVOS linwinVOS) {
        this.linwinVOS = linwinVOS;
    }
    public void run() {
        String mes = this.threadSocket.getMessage();
    }
}
