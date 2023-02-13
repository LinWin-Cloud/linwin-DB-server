package LinwinVOS.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Json {
    public static String readJson(String filePath,String value) {
        //read and get the json files content.
        try {
            File file = new File(filePath);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            value = "\"" + value + "\"";
            String getValue = null;

            while ((line = bufferedReader.readLine()) != null)
            {
                line = line.replace("'","\"");
                int s = line.indexOf(value);
                if (s != -1)
                {
                    line = line.substring(line.indexOf(value)+value.length(),line.length());
                    int e = line.indexOf("\"");
                    if (e != -1) {
                        getValue = line.substring(e+1,line.lastIndexOf("\""));
                        break;
                    }else {
                        getValue = null;
                        continue;
                    }
                }
            }
            return getValue;
        }catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
