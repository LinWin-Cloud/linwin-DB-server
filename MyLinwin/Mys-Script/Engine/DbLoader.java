package Engine;

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

public class DbLoader {
    public List<String> LoadDB_CommandScript(String name) {
        List<String> list = new ArrayList<>();

        try {
            StringBuffer fileContent = new StringBuffer("");

            FileReader fileReader = new FileReader(name);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                fileContent.append(line);
                fileContent.append("\n");
            }
            String databaseName = new File(name).getName();
            String shell_1 = "create database '"+Syntax.getFileName(databaseName)+"'";
            list.add(shell_1);

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

                            String shell_data = "create data '"+getName+"' setting('"+getValue+"','"+getNote+"') in "+Syntax.getFileName(databaseName);
                            list.add(shell_data);
                        }
                    }
                    return 0;
                }
            });
            executorService.shutdownNow();
            future.get();
        } catch (Exception exception) {}
        return list;
    }
}
