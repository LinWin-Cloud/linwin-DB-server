package LinwinVOS.runtime.lib;

import LinwinVOS.FileSystem.Data;
import LinwinVOS.FileSystem.VosDatabase;
import LinwinVOS.LinwinVOS;
import LinwinVOS.Mirror.MirrorHost;
import LinwinVOS.Users.UsersFileSystem;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class View {
    public static StringBuffer stringBuffer = new StringBuffer("");
    public String view(String user,String command) {
        try{
            String database = command.substring(command.indexOf(" ")+1);
            UsersFileSystem usersFileSystem = LinwinVOS.FileSystem.get(user);
            VosDatabase vosDatabase = usersFileSystem.get(database);
            if (vosDatabase != null) {
                for (Data data : vosDatabase.getListData()) {
                    stringBuffer.append(data.getName());
                    stringBuffer.append("   |   ");
                    stringBuffer.append(data.getValue());
                    stringBuffer.append("   |   ");
                    stringBuffer.append(data.getType());
                    stringBuffer.append("   |   ");
                    stringBuffer.append(data.getCreateTime());
                    stringBuffer.append("   |   ");
                    stringBuffer.append(data.getModificationTime());
                    stringBuffer.append("   |   ");
                    stringBuffer.append(data.getNote());
                    stringBuffer.append("   |   ");
                    stringBuffer.append("\n");
                }
                return stringBuffer.toString()+"\n";
            }else {
                Future<Integer> future = null;
                for (MirrorHost mirrorHost : LinwinVOS.FileSystem.get(user).getMirrorHosts())
                {
                    future = LinwinVOS.executorService.submit(new Callable<Integer>() {
                        @Override
                        public Integer call() throws Exception {
                            String message = mirrorHost.sendCommand("view "+database);
                            if (message.equals("Can not find target database")) {
                                return 1;
                            }
                            stringBuffer.append(message);
                            return 0;
                        }
                    });
                }
                try{
                    future.get();
                }catch (Exception exception){}
                return stringBuffer.toString();
            }
        }catch (Exception exception){
            return "Command syntax error!";
        }
    }
}
