package main;

import java.io.*;
import java.net.Socket;
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
            Socket socket = new Socket(remote,port);
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);

            passwd = Md5Util_tool.md5(passwd);
            String message = "Logon="+user+"?Passwd="+passwd+"?Command="+getType;
            //System.out.println(message);
            printWriter.println("GET "+message+" HTTP/1.1");
            printWriter.flush();
            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
           StringBuffer stringBuffer = new StringBuffer("");
            while ((line = bufferedReader.readLine()) != null)
            {
                if (line.equals("Send Message Error") || line.equals("Passwd Or UserName Error!")) {
                    stringBuffer.delete(0,stringBuffer.length());
                    stringBuffer.append("\n");
                    stringBuffer.append(line);
                    return false;
                }
                if (line.equals("Error Command and Script")) {
                    return false;
                }
                stringBuffer.append(" -- ");
                stringBuffer.append(line);
                stringBuffer.append("\n");
            }
            this.getRunMessage = stringBuffer.toString();
            return true;
        }catch (Exception exception){
            this.getRunMessage = "\n[ERR] "+exception.getMessage()+"\n";
            return false;
        }
    }
    public String getMessage() {
        return this.getRunMessage;
    }
}
