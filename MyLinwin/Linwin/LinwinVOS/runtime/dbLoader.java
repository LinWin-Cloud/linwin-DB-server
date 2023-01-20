package LinwinVOS.runtime;

import LinwinVOS.FileSystem.Data;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class dbLoader {
    public static List<Data> LoadDB(String name) {
        List<Data> list = new ArrayList<>();
        Thread LoadDatabaseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
                    FileInputStream fileInputStream = new FileInputStream(name);
                    FileChannel fileChannel = fileInputStream.getChannel();
                    int length = fileChannel.read(byteBuffer);
                    String fileContent = "";
                    while ((length != -1)) {
                        byteBuffer.flip();
                        byte[] bytes = byteBuffer.array();
                        String s = new String(bytes, Charset.defaultCharset());
                        try{
                            fileContent = fileContent + s + "\n";
                        }catch (Exception exception){
                            try{
                                Thread.sleep(100);
                                continue;
                            }catch (Exception exception1){
                                exception1.printStackTrace();
                            }
                        }
                        byteBuffer.clear();
                        length = fileChannel.read(byteBuffer);
                    }
                    String[] lines = fileContent.split("\n");
                    HashSet<String> hashSet = new HashSet<String>(Arrays.asList(lines));
                    for (String getLine : hashSet) {
                        int n = getLine.indexOf("Name=");
                        int v = getLine.indexOf("&Value=");
                        int t = getLine.lastIndexOf("&Type=");
                        int c = getLine.lastIndexOf("&createTime=");
                        int m = getLine.lastIndexOf("&ModificationTime=");
                        int Note = getLine.lastIndexOf("&note=");
                        if (n!=-1&&v!=-1&&t!=-1&c!=-1&&m!=-1&&Note!=-1) {
                            String getName = getLine.substring(n + "Name=".length(), v);
                            String getValue = getLine.substring(v + "&Value=".length(), t);
                            String getType = getLine.substring(t + "&Type=".length(), c);
                            String getCreateTime = getLine.substring(c + "&createTime=".length(), m);
                            String getModificationTime = getLine.substring(m + "&ModificationTime=".length(), Note);
                            String getNote = getLine.substring(Note+"&note=".length(),getLine.length());

                            Data data = new Data();
                            data.setName(getName);
                            data.setCreateTime(getCreateTime);
                            data.setSaveDatabase(name);
                            data.setModificationTime(getModificationTime);
                            data.setValue(getValue);
                            data.setNote(getNote);

                            list.add(data);
                        }
                    }
                }catch (Exception exception){
                    exception.printStackTrace();
                }
            }
        });
        LoadDatabaseThread.start();
        return list;
    }
    /*
     */
}
