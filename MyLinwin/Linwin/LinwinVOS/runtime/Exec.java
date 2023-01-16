package LinwinVOS.runtime;

import LinwinVOS.LinwinVOS;

public class Exec {
    private MydbEngine mydbEngine;
    public void setEngine(MydbEngine mydbEngine) {
        this.mydbEngine = mydbEngine;
    }
    public String listDatabase(String command) {
        //String getRunPath = LinwinVOS.UsersNowPath.get(this.mydbEngine.getUser());
        String[] shell = command.split(" ");
        if (shell.length == 3) {
            String showPath = shell[2];

        }else {
            return null;
        }
    }
}
