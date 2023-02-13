package remote;

import Data.Json;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashSet;

public class Loader {
    public void loadUser() {
        File file = new File("../../../../opt/LinwinRemote/RemoteData");
        if (file.exists() && file.isDirectory())
        {
            File[] listDir = file.listFiles();
            for (File target : listDir)
            {
                if (target.isFile() && Loader.getLastName(target.getName()).equals(".mydb"))
                {
                    Loader.loadData(target.getAbsolutePath());
                }
            }
        }
        else
        {
            System.out.println("ERROR TO LOAD THE REMOTE DATA , CAN NOT FIND REMOTE DATA PATH OR TARGET IS A FILE");
            System.exit(0);
        }
    }
    public static void loadData(String path)
    {
        File file = new File(path);
        Database database = new Database();
        database.setRemoteDatabase(true);
        database.setName(file.getName().substring(0,file.getName().lastIndexOf(".")));
        database.setCreateTime(Json.readJson(file.getAbsolutePath(),Json.getFileUpdateTime(file.getAbsolutePath())));
        database.setModificationTime(Json.readJson(file.getAbsolutePath(),"Update"));
        database.setSavePath("/mirror/");
        UserRemote.usersHashMap.put(database.getName(),database);

        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
            FileInputStream fileInputStream = new FileInputStream(path);
            FileChannel fileChannel = fileInputStream.getChannel();
            int length = fileChannel.read(byteBuffer);
            StringBuffer fileContent = new StringBuffer("");
            while ((length != -1)) {
                byteBuffer.flip();
                byte[] bytes = byteBuffer.array();
                String s = new String(bytes, Charset.defaultCharset());
                try {
                    fileContent.append(s);
                    fileContent.append("\n");
                } catch (Exception exception) {
                    try {
                        Thread.sleep(100);
                        continue;
                    } catch (Exception exception1) {
                        exception1.printStackTrace();
                    }
                }
                byteBuffer.clear();
                length = fileChannel.read(byteBuffer);
            }
            String[] lines = fileContent.toString().split("\n");
            HashSet<String> hashSet = new HashSet<String>(Arrays.asList(lines));

            for (String getLine : hashSet) {
                int n = getLine.indexOf("Name=");
                int v = getLine.indexOf("&Value=");
                int t = getLine.lastIndexOf("&Type=");
                int c = getLine.lastIndexOf("&createTime=");
                int m = getLine.lastIndexOf("&ModificationTime=");
                int Note = getLine.lastIndexOf("&note=");
                if (n != -1 && v != -1 && t != -1 & c != -1 && m != -1 && Note != -1) {
                    String getName = getLine.substring(n + "Name=".length(), v);
                    String getValue = getLine.substring(v + "&Value=".length(), t);
                    String getType = getLine.substring(t + "&Type=".length(), c);
                    String getCreateTime = getLine.substring(c + "&createTime=".length(), m);
                    String getModificationTime = getLine.substring(m + "&ModificationTime=".length(), Note);
                    String getNote = getLine.substring(Note + "&note=".length(), getLine.length());

                    int a1 = getName.indexOf("'");
                    int b1 = getName.indexOf(";");
                    int c1 = getName.indexOf(".");
                    int d1 = getName.indexOf(")");
                    int e1 = getName.indexOf("(");

                    if (a1 != -1 || b1 != -1 || c1 != -1 || d1 != -1 || e1 != -1) {
                        continue;
                    }else {
                        Data data = new Data();
                        data.setName(getName);
                        data.setCreateTime(getCreateTime);
                        data.setModificationTime(getModificationTime);
                        data.setValue(getValue);
                        data.setNote(getNote);
                        database.putData(getName,data);
                    }
                }
            }
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    public static String getLastName(String str)
    {
        try
        {
            return str.substring(str.lastIndexOf("."));
        }catch (Exception exception)
        {
            return str;
        }
    }
}
