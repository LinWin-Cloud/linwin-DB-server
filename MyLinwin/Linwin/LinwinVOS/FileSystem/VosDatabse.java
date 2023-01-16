package LinwinVOS.FileSystem;

import java.io.File;

public class VosDatabse {
    public VosDatabse() {
    }

    public static boolean getRealFileExists(String var0) {
        File var1 = new File(var0);
        return var1.exists();
    }
}
