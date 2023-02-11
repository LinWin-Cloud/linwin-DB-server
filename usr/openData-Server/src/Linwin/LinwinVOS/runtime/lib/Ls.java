package LinwinVOS.runtime.lib;

import LinwinVOS.FileSystem.Data;
import LinwinVOS.FileSystem.VosDatabase;
import LinwinVOS.LinwinVOS;
import LinwinVOS.Mirror.MirrorHost;
import LinwinVOS.Users.UsersFileSystem;

import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class Ls {
    public String ls(String user,String command)
    {
        try{
            String listDatabase = command.substring(3);
            UsersFileSystem usersFileSystem = LinwinVOS.FileSystem.get(user);
            VosDatabase vosDatabase = usersFileSystem.get(listDatabase);

            if (vosDatabase != null)
            {
                StringBuffer stringBuffer = new StringBuffer("");
                for (Data data : vosDatabase.getListData())
                {
                    stringBuffer.append(data.getName());
                    stringBuffer.append("\n");
                }
                return stringBuffer.toString();
            }
            else {
                HashSet<String> hashSet = new HashSet<>();
                Future<Integer> future = null;
                for (MirrorHost mirrorHost : LinwinVOS.FileSystem.get(user).getMirrorHosts())
                {
                    future = LinwinVOS.executorService.submit(new Callable<Integer>() {
                        @Override
                        public Integer call() throws Exception {

                            String message = mirrorHost.sendCommand("ls "+listDatabase);
                            if (message.equals("Can not find target database"))
                            {
                                return 1;
                            }else {
                                String[] ls = message.split("\n");
                                for (String i : ls)
                                {
                                    StringBuffer stringBuffer = new StringBuffer("");
                                    stringBuffer.append(i);
                                    stringBuffer.append("\n");
                                    hashSet.add(stringBuffer.toString());
                                }
                                return 0;
                            }
                        }
                    });
                }
                try {
                    future.get();
                }catch (Exception exception){}
                StringBuffer stringBuffer = new StringBuffer("");
                for (String s : hashSet)
                {
                    stringBuffer.append(s);
                }
                return stringBuffer.toString();
            }
        }catch (Exception exception) {
            return "Command syntax error!";
        }
    }
}
