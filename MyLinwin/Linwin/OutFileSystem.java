import LinwinVOS.LinwinVOS;
import LinwinVOS.Users.UsersFileSystem;
import ThreadSocket.ThreadSocket;

import java.util.HashSet;

public class OutFileSystem {
    private  LinwinVOS linwinVOS;
    private ThreadSocket threadSocket;
    public void setLinwinVOS(LinwinVOS linwinVOS) {
        this.linwinVOS = linwinVOS;
    }
    public void run() {
        while (true) {
            try{
                Thread.sleep(20);
                if (this.getAllUserLoad_STATUS()) {
                    break;
                }
            }catch (Exception exception){
                exception.printStackTrace();
            }
        }
    }
    private Boolean getAllUserLoad_STATUS() {

    }
    public void setThreadSocket(ThreadSocket threadSocket) {
        this.threadSocket = threadSocket;
    }
}
