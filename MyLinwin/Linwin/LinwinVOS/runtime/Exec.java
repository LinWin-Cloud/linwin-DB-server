package LinwinVOS.runtime;

import LinwinVOS.FileSystem.Data;
import LinwinVOS.FileSystem.VosDatabase;
import LinwinVOS.LinwinVOS;

public class Exec {
    private MydbEngine mydbEngine;
    public void setEngine(MydbEngine mydbEngine) {
        this.mydbEngine = mydbEngine;
    }
    public String listDatabase() {
        String list = "";
        for (int i = 0 ; i < LinwinVOS.databaseName.size() ; i++)
        {
            list = list + "\n" +LinwinVOS.databaseName.get(i);
        }
        return list;
    }
}
