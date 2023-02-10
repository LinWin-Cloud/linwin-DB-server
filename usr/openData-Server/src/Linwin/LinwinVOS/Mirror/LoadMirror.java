package LinwinVOS.Mirror;

import LinwinVOS.LinwinVOS;
import LinwinVOS.data.Json;
import java.io.File;

public class LoadMirror
{
    public void load()
    {
        File file = new File("../../config/Distributed/");
        if (file.isDirectory() && file.exists())
        {
            for (File target : file.listFiles())
            {
                String getRemoteHost = Json.readJson(target.getAbsolutePath(),"Remote");
                String getKey = Json.readJson(target.getAbsolutePath(),"Key");
                String getSave = Json.readJson(target.getAbsolutePath(),"Save");

                if (getRemoteHost == null || getKey == null || getSave == null)
                {
                    System.out.println("CONFIG FILE ERROR");
                    System.exit(0);
                }
                else
                {
                    MirrorHost mirrorHost = new MirrorHost();
                    mirrorHost.setName(LoadMirror.getFrontName(target.getName()));
                    mirrorHost.setKey(getKey);

                    LinwinVOS.mirrorHostHashSet.add(mirrorHost);
                }
            }
        }
        else
        {
            System.out.println("CAN NOT FIND CONFIG DIRECTORY: Distributed");
            System.exit(0);
        }
    }
    public static String getFrontName(String str) {
        try
        {
            return str.substring(0,str.lastIndexOf("."));
        }
        catch (Exception exception){
            return str;
        }
    }
    private static void loadReal_Mirror(String remote,String key) {

    }
}
