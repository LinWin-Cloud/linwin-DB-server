import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;

public class mys {
    public static void main(String[] args) {
        int commandLength = args.length;
        if (commandLength == 0) {
            System.out.println(mys.getFileContent("../../config/help/MysInfo.txt"));
        }
        if (commandLength == 1) {
            File testFile1 = new File(System.getProperty("user.dir")+"/"+args[1]);
            File testFile2 = new File(args[1]);
            if (testFile1.exists()) {
                mys.loadMysFiles(testFile1.getAbsolutePath());
            }else if (testFile2.exists()) {
                mys.loadMysFiles(testFile2.getAbsolutePath());
            }else {
                System.out.println("[Mys Runtime] can not open file '"+args[1]+"'");
                System.exit(0);
            }
        }else {
            System.out.println(mys.getFileContent("../../config/help/MysInfo.txt"));
        }
    }
    public static String getFileContent(String name) {
        try{
            FileReader fileReader = new FileReader(name);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            StringBuffer stringBuffer = new StringBuffer("");
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
                stringBuffer.append("\n");
            }
            return stringBuffer.toString();
        }catch (Exception exception){
            return "";
        }
    }
    public static void loadMysFiles(String path) {
        try{
            HashSet<String> stringHashSet = new HashSet<>();

            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringHashSet.add(line);
            }
            for (String getLine : stringHashSet) {

            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
}
