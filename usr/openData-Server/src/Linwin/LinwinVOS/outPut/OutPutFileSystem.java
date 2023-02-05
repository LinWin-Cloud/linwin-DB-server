package LinwinVOS.outPut;

import LinwinVOS.FileSystem.Data;
import LinwinVOS.FileSystem.VosDatabase;
import LinwinVOS.LinwinVOS;
import LinwinVOS.runtime.Func;

import java.io.FileWriter;
import java.io.IOException;

public class OutPutFileSystem {
    public static void writeDatabase(String name, String user) {
        VosDatabase vosDatabase = LinwinVOS.FileSystem.get(user).get(name);
        FileWriter fileWriter = LinwinVOS.outPutMap.get(name);
        try {
            FileWriter fileWriter1 = new FileWriter(LinwinVOS.DatabasePath+"/"+user+"/Database/"+name+".mydb",false);
            fileWriter1.write("");
            fileWriter1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (vosDatabase == null || fileWriter == null){
            return;
        }else {
            try{
                StringBuffer stringBuffer = new StringBuffer("");
                stringBuffer.append("{\n" +
                        "    \"Update\" : \""+ Func.getNowTime() +"\"\n" +
                        "}\n");
                for (Data data : vosDatabase.dataHashMap.values()) {
                    String content = "Name="+data.getName()+"&Value="+data.getValue()+"&Type="+data.getType()+"&createTime="+data.getCreateTime()+"&ModificationTime="+data.getModificationTime()+"&note="+data.getNote()+"\n";
                    stringBuffer.append(content);
                }
                fileWriter.write(stringBuffer.toString());
                fileWriter.flush();
            }catch (Exception exception){
                exception.printStackTrace();
            }
        }
    }
}
