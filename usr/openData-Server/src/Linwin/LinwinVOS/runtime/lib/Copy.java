package LinwinVOS.runtime.lib;

import LinwinVOS.FileSystem.Data;
import LinwinVOS.FileSystem.VosDatabase;
import LinwinVOS.LinwinVOS;
import LinwinVOS.Mirror.MirrorHost;
import LinwinVOS.Users.UsersFileSystem;
import LinwinVOS.outPut.OutPutFileSystem;
import LinwinVOS.runtime.Func;

public class Copy {
    public static boolean isRemote_resource = false;
    public static boolean isRemote_target = false;
    public static MirrorHost sourceHost = null;
    public static MirrorHost targetHost = null;
    public String copy(String user,String command) {
        try{
            String[] split = command.split(" ");
            String resource = split[1];
            String target = split[2];

            resource = resource.substring(resource.indexOf("'")+1,resource.lastIndexOf("'"));
            target = target.substring(target.indexOf("'")+1,target.lastIndexOf("'"));

            UsersFileSystem usersFileSystem = LinwinVOS.FileSystem.get(user);
            VosDatabase database = usersFileSystem.get(resource);


            for (MirrorHost mirrorHost : LinwinVOS.FileSystem.get(user).getMirrorHosts())
            {
                if (mirrorHost.sendCommand("existdb "+resource).replace("\n","").equals("true")) {
                    Copy.isRemote_resource = true;
                    Copy.sourceHost = mirrorHost;
                }
                if (mirrorHost.sendCommand("existdb "+target).replace("\n","").equals("true")) {
                    Copy.isRemote_target = true;
                    Copy.targetHost = mirrorHost;
                }
            }
            //System.out.println(Copy.isRemote_resource+" "+ Copy.isRemote_target);
            if (Copy.isRemote_resource && Copy.isRemote_target)
            {
                try
                {
                    StringBuffer stringBuffer = new StringBuffer("");
                    String[] dataList = sourceHost.sendCommand("view "+resource).split("\n");
                    for (String i : dataList)
                    {
                        //System.out.println(i);
                        String[] splitData = i.split("---");
                        String name = splitData[0];
                        String value = splitData[1];
                        String note = splitData[5];

                        String send = "create data '"+name+"' setting('"+value+"','"+note+"') in "+target;
                        stringBuffer.append(send);
                        stringBuffer.append("/n");
                    }
                    boolean success = false;

                    String getMes = Copy.targetHost.sendCommand(stringBuffer.toString());
                    if (getMes.replace("\n","").indexOf("Create Successful!") != -1) {
                        success = true;
                    }

                    if (success) {
                        return "Successful!\n";
                    }else {
                        return "Copy error!";
                    }
                }catch (Exception exception){
                    //exception.printStackTrace();
                    return "Send Message Error";
                }
            }
            else if (Copy.isRemote_target && !Copy.isRemote_resource)
            {
                StringBuffer stringBuffer = new StringBuffer("");
                VosDatabase resourceDatabase = usersFileSystem.get(resource);
                if (resourceDatabase == null) {
                    return "Can not find resource database.";
                }
                for (Data data : resourceDatabase.getListData())
                {
                    String send = "create data '"+data.getName()+"' setting('"+data.getValue()+"','"+data.getNote()+"') in "+target;
                    stringBuffer.append(send);
                    stringBuffer.append("/n");
                }
                Copy.targetHost.sendCommand(stringBuffer.toString());
                return "Copy Successful!\n";
            }
            else if (Copy.isRemote_resource && !Copy.isRemote_target)
            {
                try {
                    StringBuffer stringBuffer = new StringBuffer("");
                    String mes = Copy.sourceHost.sendCommand("view "+resource);
                    String[] splitMessage = mes.split("---");

                    String name = splitMessage[0];
                    String value = splitMessage[1];

                    return "Copy Successful!\n";
                }
                catch (Exception exception) {
                    return "Copy error!";
                }
            }
            else {
                if (database == null) {
                    return "Can not find resource database!";
                }else {
                    VosDatabase TargetCopy = usersFileSystem.get(target);
                    if (TargetCopy == null) {
                        VosDatabase vosDatabase = database;
                        vosDatabase.setName(target);
                        vosDatabase.setCreateTime(Func.getNowTime());
                        vosDatabase.setSavePath("/"+user+"/"+target);
                        vosDatabase.setModificationTime(Func.getNowTime());
                        usersFileSystem.putDatabase(target,vosDatabase);

                        OutPutFileSystem.writeDatabase(vosDatabase.getName(),user);
                        return "Copy Successful!\n";
                    }
                    else {
                        for (Data data : database.getListData()) {
                            data.setModificationTime(Func.getNowTime());
                            TargetCopy.putData(data.getName(),data);
                        }
                        usersFileSystem.putDatabase(target,TargetCopy);
                        OutPutFileSystem.writeDatabase(TargetCopy.getName(),user);
                        return "Copy Successful!\n";
                    }
                }
            }
        }catch (Exception exception){
            return "Command syntax error!";
        }
    }
}
