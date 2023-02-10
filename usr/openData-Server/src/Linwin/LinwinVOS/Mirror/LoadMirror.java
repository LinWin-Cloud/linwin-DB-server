package LinwinVOS.Mirror;

import LinwinVOS.LinwinVOS;
import LinwinVOS.data.Json;

import javax.naming.ldap.SortKey;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadMirror
{
    public void load()
    {
        File file = new File("../../config/Distributed/");
        if (file.isDirectory() && file.exists())
        {
            for (File target : file.listFiles())
            {
                if (target.isDirectory())
                {
                    continue;
                }
                String getRemoteHost = Json.readJson(target.getAbsolutePath(),"Remote");
                String getKey = Json.readJson(target.getAbsolutePath(),"Key");

                if (getRemoteHost == null || getKey == null)
                {
                    System.out.println("CONFIG FILE ERROR: "+target.getName());
                    continue;
                }
                else
                {
                    LoadMirror.loadReal_Mirror(
                            getRemoteHost,getKey,LoadMirror.getFrontName(
                                    target.getName()));
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
    private static void loadReal_Mirror(String remote,String key,String name) {
        /**
         * First, we must Confirm the remote host and key to connect.
         * Then, the Software can connect and share the data.
         */
        try
        {
            URL url = new URL(remote+"/?");
            HttpURLConnection urlConnection =

                    MirrorHost mirrorHost = new MirrorHost();
            mirrorHost.setName(name);
            mirrorHost.setKey(key);
            mirrorHost.setRemote(remote);
            LinwinVOS.mirrorHostHashSet.add(mirrorHost);
        }
        catch (Exception exception)
        {
            System.out.println("");
            return;
        }
    }
}
