import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientShell {
    public static void main(String[] args) {
        try{
            String remote = args[0];
            int port = Integer.valueOf(args[1]);
            String user = args[2];
            String
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    public static void sendMessage(String getType,String remote,int port,String user,String passwd) {
        try{
            long start = System.currentTimeMillis();
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
            int i = 0 ;
            while ((line = bufferedReader.readLine()) != null)
            {
                i = i + 1;
                System.out.println(" - "+line+"");
            }
            System.out.println("Result Number: "+(i-1));
            System.out.println("=============================");
            bufferedReader.close();
            inputStream.close();
            socket.close();
            System.out.println();

            long end = System.currentTimeMillis();
            System.out.println(end-start+"ms");
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
}
