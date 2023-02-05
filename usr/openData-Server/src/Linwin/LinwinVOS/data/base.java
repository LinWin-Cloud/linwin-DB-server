package LinwinVOS.data;

import LinwinVOS.FileSystem.Data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;

public class base {
    public static String getFileCreateTime(String filePath) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss ");
        FileTime t = null;
        try {
            t = Files.readAttributes(Paths.get(filePath), BasicFileAttributes.class).creationTime();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String createTime = dateFormat.format(t.toMillis());
        return createTime;
    }
    public static String getFileUpdateTime(String filepath) {
        File file = new File(filepath);
        Long lastModified = file.lastModified();
        Date date = new Date(lastModified);

        return date.toString();
    }
}
