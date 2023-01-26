package main;

import java.io.*;
import java.net.Socket;

public class Connect {
    public static Boolean connect(String[] args) {
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
            return Connect.sendMessage(command,remote,port,user,passwd);
        }catch (Exception exception) {
            return false;
        }
    }
    public static Boolean sendMessage(String getType,String remote,int port,String user,String passwd) {
        try{
            Socket socket = new Socket(remote,port);
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);

            passwd = Md5Util_tool.md5(passwd);
            String message = "Logon="+user+"?Passwd="+passwd+"?Command="+getType;
            //System.out.println(message);
            printWriter.println(message);
            printWriter.flush();
            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                if (line.equals("Send Message Error") || line.equals("Passwd Or UserName Error!")) {
                    return false;
                }
            }
            return true;
        }catch (Exception exception){
            return false;
        }
    }
}
