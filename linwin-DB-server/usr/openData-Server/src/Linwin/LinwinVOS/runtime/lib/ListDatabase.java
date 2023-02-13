package LinwinVOS.runtime.lib;

import LinwinVOS.FileSystem.VosDatabase;
import LinwinVOS.LinwinVOS;
import LinwinVOS.Mirror.MirrorHost;
import LinwinVOS.Users.UsersFileSystem;

import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class ListDatabase {
    public String listDatabase(String user) {
        UsersFileSystem usersFileSystem = LinwinVOS.FileSystem.get(user);
        HashSet<VosDatabase> databases = usersFileSystem.getDatabase();
        HashSet<String> hashSet = new HashSet<>();

        for (VosDatabase vosDatabase : databases) {

            StringBuffer stringBuffer = new StringBuffer("");
            stringBuffer.append(vosDatabase.getName());
            stringBuffer.append("\n");
            hashSet.add(stringBuffer.toString());
        }
        Future<Integer> future = null;
        for (MirrorHost mirrorHost : usersFileSystem.getMirrorHosts())
        {
            future = LinwinVOS.executorService.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    String[] strings = mirrorHost.sendCommand("list database").split("\n");
                    for (String i : strings)
                    {
                        StringBuffer stringBuffer = new StringBuffer("");
                        stringBuffer.append(i);
                        stringBuffer.append("\n");
                        hashSet.add(stringBuffer.toString());
                    }
                    return 0;
                }
            });
        }
        try{
            future.get();
        }catch (Exception exception){}
        StringBuffer list = new StringBuffer("");
        for (String i : hashSet)
        {
            list.append(i);
        }
        return list.toString();
    }
}
