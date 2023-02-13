package MirrorRuntime.outPut;

import MirrorRuntime.lib.Func;
import remote.Database;
import remote.UserRemote;
import remote.Data;
import java.io.FileWriter;
import java.io.IOException;

public class OutPutFileSystem {
    public static void writeDatabase(String name) {
        Database vosDatabase = UserRemote.usersHashMap.get(name);
        try {
            FileWriter fileWriter = new FileWriter("../../../../opt/LinwinRemote/RemoteData/"+name+".mydb",false);
            if (vosDatabase == null){
                return;
            }else {
                StringBuffer stringBuffer = new StringBuffer("");
                stringBuffer.append("{\n" +
                        "    \"Update\" : \""+ Func.getNowTime() +"\"\n" +
                        "}\n");
                for (Data data : vosDatabase.dataHashMap.values()) {
                    String content = "Name="+data.getName()+"&Value="+data.getValue()+"&Type="+data.getType()+"&createTime="+data.getCreateTime()+"&ModificationTime="+data.getModificationTime()+"&note="+data.getNote()+"\n";
                    //System.out.println(content);
                    stringBuffer.append(content);
                }
                fileWriter.write(stringBuffer.toString());
                fileWriter.flush();
                fileWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
