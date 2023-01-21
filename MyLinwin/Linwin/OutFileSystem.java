import LinwinVOS.LinwinVOS;
import LinwinVOS.Users.UsersFileSystem;

import java.util.HashSet;

public class OutFileSystem {
    private  LinwinVOS linwinVOS;
    public void setLinwinVOS(LinwinVOS linwinVOS) {
        this.linwinVOS = linwinVOS;
    }
    public void run() {

        for (UsersFileSystem usersFileSystem : this.linwinVOS.getUserFileSystem())
        {

        }
    }
}
