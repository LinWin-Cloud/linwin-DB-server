package LinwinVOS.runtime;

import LinwinVOS.FileSystem.Data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class dbLoader {
    public static List<Data> LoadDB(String name) {
        List<Data> list = new ArrayList<>();
        try{
            FileReader fileReader = new FileReader(name);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String getLine;
            while ((getLine = bufferedReader.readLine()) != null)
            {
                int n = getLine.indexOf("Name=");
                int v = getLine.indexOf("&Value=");
                int i = getLine.indexOf("&Id=");
                int t = getLine.lastIndexOf("&Type=");
                int c = getLine.lastIndexOf("&createTime=");
                int m = getLine.lastIndexOf("&ModificationTime=");
                if (n!=-1&&v!=-1&&i!=-1&&t!=-1&c!=-1&&m!=-1) {
                    String getName = getLine.substring(n + "Name=".length(), v);
                    String getValue = getLine.substring(v + "&Value=".length(), i);
                    String getId = getLine.substring(i + "&Id=".length(), t);
                    String getCreateTime = getLine.substring(c + "&createTime=".length(), m);
                    String getModificationTime = getLine.substring(m + "&ModificationTime=".length(), getLine.length());

                    Data data = new Data();
                    data.setName(getName);
                    data.setCreateTime(getCreateTime);
                    data.setId(Integer.valueOf(getId));
                    data.setSaveDatabase(name);
                    data.setModificationTime(getModificationTime);
                    data.setValue(getValue);

                    list.add(data);
                }
            }

        }catch (Exception exception){
            exception.printStackTrace();
        }
        return list;
    }
}
