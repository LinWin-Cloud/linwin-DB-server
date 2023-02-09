package remote;

import Data.Json;
import java.io.File;

public class Loader {
    public void loadUser() {
        File file = new File("../../../../opt/LinwinRemote/RemoteData");
        if (file.exists() && file.isDirectory())
        {
            File[] listDir = file.listFiles();
            for (File target : listDir)
            {
                if (target.isDirectory())
                {
                    File userCONFIG = new File(target.getAbsoluteFile()+"/user.json");
                    File userData = new File(target.getAbsoluteFile()+"/Data");

                    boolean user = userCONFIG.isFile() && userCONFIG.exists();
                    boolean data = userData.isDirectory() && userData.exists();
                    if (user && data)
                    {
                        String key = Json.readJson(userCONFIG.getAbsolutePath(),"Key");
                        Users users = new Users();
                        users.setKey(key);
                        users.setMirrorName(target.getName());
                    }
                    else 
                    {
                        continue;
                    }
                }
            }
        }
        else
        {
            System.out.println("ERROR TO LOAD THE REMOTE DATA , CAN NOT FIND REMOTE DATA PATH OR TARGET IS A FILE");
            System.exit(0);
        }
    }
}
