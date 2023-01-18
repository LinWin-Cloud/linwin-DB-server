import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientShell {
    public static void main(String[] args) {
        try{
            //ClientShell.sendMessage("find database 1");
            //ClientShell.sendMessage("list database");
            ClientShell.sendMessage("find data hello");

        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    public static void sendMessage(String getType) {
        try{
            long start = System.currentTimeMillis();
            Socket socket = new Socket("127.0.0.1",8888);
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);

            String user = "root";
            String passwd = Md5Util_tool.md5("123456");
            String message = "Logon="+user+"?Passwd="+passwd+"?Command="+getType;
            //System.out.println(message);
            printWriter.println(message);
            printWriter.flush();
            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                System.out.println(" - "+line+"");
            }
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
