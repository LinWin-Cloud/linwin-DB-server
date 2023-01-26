import Engine.HeadType;
import Engine.Syntax;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;

public class mys {
    public static void main(String[] args) {
        int commandLength = args.length;
        if (commandLength == 0) {
            System.out.println(mys.getFileContent("../../config/help/MysInfo.txt"));
            System.exit(0);
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
            StringBuffer stringBuffer = new StringBuffer("");

            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = mys.replaceSpace(line);
                line = mys.replaceLastSpace(line);
                stringBuffer.append(line);
                stringBuffer.append("\n");
            }
            String user = "";
            String passwd = "";
            String remote = "";
            String port = "";

            HeadType headType = new HeadType();
            headType.dealHeadType(path);

            user = headType.getUser();
            passwd = headType.getPasswd();
            remote = headType.getRemote();
            port = headType.getPort();

            Syntax syntax = new Syntax();

            for (String getLine : stringHashSet) {
                if (syntax.isAnnotated(getLine)) {
                    continue;
                }
                if (getLine.substring(0,6).equals("upload")) {
                    System.out.println(syntax.UpLoad_Command(getLine,path));
                }
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
    public static String replaceSpace(String str) {
        int length = str.length();
        String getSpaceStr = null;
        for (int i = 0 ; i < length ;i++) {
            String charset = str.substring(i,i+1);
            if (!charset.equals(" ")) {
                String Code = str.substring(str.indexOf(charset),length);
                getSpaceStr = Code;
                break;
            }
        }
        return getSpaceStr;
    }
    public static String replaceLastSpace(String str) {
        int length = str.length();
        String getSpaceStr = null;
        for (int i = 0 ; i < length ;i++) {
            String charset = str.substring(length-i-1,length-i);
            if (!charset.equals(" ")) {
                String Code = str.substring(0,str.lastIndexOf(charset)+1);
                getSpaceStr = Code;
                break;
            }
        }
        return getSpaceStr;
    }
}
