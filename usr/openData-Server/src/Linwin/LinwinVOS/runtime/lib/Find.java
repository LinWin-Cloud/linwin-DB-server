package LinwinVOS.runtime.lib;

import LinwinVOS.FileSystem.VosDatabase;
import LinwinVOS.LinwinVOS;
import LinwinVOS.Mirror.MirrorHost;
import LinwinVOS.Users.UsersFileSystem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.*;

public class Find {
    public String find(String command,String user)
    {
        String[] getCommand = command.split(" ");

        if (getCommand.length >= 3) {
            String findType = getCommand[1];
            String findIndex = "";

            if (getCommand.length == 3) {
                findIndex = getCommand[2];
            }else {
                StringBuffer stringBuffer = new StringBuffer("");
                for (int j = 0 ; j < getCommand.length ; j++) {
                    //System.out.println(j+" "+getCommand[j]);
                    if (j >= 2 ) {
                        stringBuffer.append(getCommand[j]);
                        stringBuffer.append(" ");
                    }
                }
                findIndex = stringBuffer.toString().substring(0,stringBuffer.toString().length()-1);
            }
            if (findType.equals("database")) {
                UsersFileSystem usersFileSystem = LinwinVOS.FileSystem.get(user);
                HashSet<VosDatabase> databases = usersFileSystem.getDatabase();

                ListDatabase listDatabase = new ListDatabase();
                String[] strings = listDatabase.listDatabase(user).split("\n");
                HashSet<String> hashSet = new HashSet<>(Arrays.asList(strings));

                StringBuffer stringBuffer = new StringBuffer("");
                for (String i : hashSet)
                {
                    int s = i.indexOf(findIndex);
                    if (s != -1) {
                        stringBuffer.append(i);
                        stringBuffer.append("\n");
                    }
                }
                return stringBuffer.toString();

            }else if (findType.equals("data")) {
                StringBuffer stringBuffer = new StringBuffer("");

                UsersFileSystem usersFileSystem = LinwinVOS.FileSystem.get(user);
                HashSet<VosDatabase> databases = usersFileSystem.getDatabase();
                Future<Integer> future = null;
                for (VosDatabase vosDatabase : databases) {
                    String index = findIndex;
                    future = LinwinVOS.executorService.submit(new Callable<Integer>() {
                        @Override
                        public Integer call() throws Exception {
                            stringBuffer.append(vosDatabase.findData(index));
                            return 0;
                        }
                    });
                }
                /**
                 * Find the remote data.
                 */
                Future<Integer> integerFuture = null;
                for (MirrorHost mirrorHost : LinwinVOS.FileSystem.get(user).getMirrorHosts())
                {
                    String Index = findIndex;
                    integerFuture = LinwinVOS.executorService.submit(new Callable<Integer>() {
                        @Override
                        public Integer call() throws Exception {
                            String message = mirrorHost.sendCommand("findData "+Index);
                            String[] findData = message.split("\n");
                            for (String i : findData)
                            {
                                stringBuffer.append(i);
                                stringBuffer.append("\n");
                            }
                            return 0;
                        }
                    });
                }
                try {
                    integerFuture.get();
                }catch (Exception exception){}
                try{
                    future.get();
                }catch (Exception exception){}

                if (stringBuffer.toString().replace("\n","").equals("")) {
                    return " Error : No Result!";
                }

                return stringBuffer.toString();
            }else {
                return "Can Not Find The Target {Error='Send Type Error'}";
            }
        }else {
            return "Command Value Error!";
        }
    }
}
