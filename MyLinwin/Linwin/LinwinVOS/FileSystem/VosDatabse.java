package LinwinVOS.FileSystem;

import java.io.File;

public class VosDatabse {


    public static boolean getRealFileExists(String name) {
        File file = new File(name);
        return file.exists();
    }
}
