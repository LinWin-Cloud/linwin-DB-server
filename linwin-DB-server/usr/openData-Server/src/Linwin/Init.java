import LogService.LogService;

import java.io.File;

public class Init {
    public void loadLogModule() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                MyLinwin.logService.setPath(new File(MyLinwin.logPath).getAbsolutePath());
                MyLinwin.logService.setFileName("openData-Log");
                MyLinwin.logService.setAutoNewLog(MyLinwin.updateLog);
                MyLinwin.logService.run();
            }
        });
        thread.start();
    }
}
