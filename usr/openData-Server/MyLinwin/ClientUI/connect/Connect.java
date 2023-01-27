package connect;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

public class Connect {
    private String Message;
    public Boolean connect(String[] args) {
        try{
            String remote = args[0];
            int port = Integer.valueOf(args[1]);
            String user = args[2];
            String passwd = args[3];
            String command = "";
            //System.out.println(args.length);
            for (int i = 0 ; i < args.length ; i++) {
                if (i >= 4) {
                    command = command + " " + args[i];
                    continue;
                }
            }
            return this.sendMessage(command,remote,port,user,passwd);
        }catch (Exception exception) {
            return false;
        }
    }
    public Boolean sendMessage(String getType,String remote,int port,String user,String passwd) {
        try{
            long start = System.currentTimeMillis();
            URL url = new URL("http://"+remote+":"+port+"/?Logon="+user+"?Passwd="+ Md5Util_tool.md5(passwd)+"?Command="+getType);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuffer stringBuffer = new StringBuffer("");

            int i = 0 ;
            while ((line = bufferedReader.readLine()) != null)
            {
                i = i + 1;
                if (line.equals("Error Command and Script") || line.equals("Passwd Or UserName Error!")) {
                    return false;
                }
                stringBuffer.append(line);
                stringBuffer.append("\n");
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            this.Message = stringBuffer.toString();

            long end = System.currentTimeMillis();
            System.out.println(end-start+"ms");
        }catch (Exception exception){
            return false;
        }
    }
}
