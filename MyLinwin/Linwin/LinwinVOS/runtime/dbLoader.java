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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class dbLoader {
    public List<Data> LoadDB(String name) {
        List<Data> list = new ArrayList<>();

        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
            FileInputStream fileInputStream = new FileInputStream(name);
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
            ExecutorService executorService = Executors.newFixedThreadPool(1);
            Future<Integer> future = executorService.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
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
                    return 0;
                }
            });
            executorService.shutdownNow();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return list;
    }
}
