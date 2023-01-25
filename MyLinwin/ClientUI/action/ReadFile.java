package action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class ReadFile {
    public static String getLine(String name) {
        try{
            FileReader fileReader = new FileReader(name);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            return bufferedReader.readLine();
        }catch (Exception exception){
            exception.printStackTrace();
            return null;
        }
    }
    public static void writeFile(String name,String content) {
        try{
            FileWriter fileWriter = new FileWriter(name);
            fileWriter.write(content);
            fileWriter.close();
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
}
