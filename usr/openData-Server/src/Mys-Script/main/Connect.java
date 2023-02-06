package main;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

import Engine.Function.Function;

public class Connect {
    private String getRunMessage = "";
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
            int i = 0 ;
            StringBuffer stringBuffer = new StringBuffer("");
            while ((line = bufferedReader.readLine()) != null)
            {
                i = i + 1;
                stringBuffer.append(" (--> ");
                stringBuffer.append(line);
                stringBuffer.append("\n");
            }
            this.getRunMessage = stringBuffer.toString();
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();

            if ((i - 1) == 0) {
                return false;
            }else {
                return true;
            }
        }catch (Exception exception){
            return false;
        }
    }
    public String getMessage() {
        return this.getRunMessage;
    }
}