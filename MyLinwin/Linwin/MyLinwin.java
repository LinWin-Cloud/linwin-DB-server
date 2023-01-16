import LinwinVOS.data.*;
import LinwinVOS.FileSystem.VosDatabse;
import LinwinVOS.LinwinVOS;
import LinwinVOS.Users.logon;
import LinwinVOS.runtime.MydbEngine;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MyLinwin {
    public static int ServicePort;
    private static int bootNumber = 0;
    public static LinwinVOS linwinVOS = new LinwinVOS();
    public static void main(String[] args)
    {
        /**
         * Load all the config files and start.
         */
        MyLinwin.LoadFiles();
        MyLinwin.RuntimeThread();
        System.out.println(" [Information] Boot Linwin Data Service!");
        System.out.println(" [Config] Start Service Port="+MyLinwin.ServicePort);
        MyLinwin.getServerSocketBoot();
        try {
            ServerSocket serverSocket = new ServerSocket(MyLinwin.ServicePort);
            for (int i = 0 ; i < 16 ; i++) {
                Thread ServiceThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            while (true) {
                                Socket socket = serverSocket.accept();
                                MyLinwin.MyLinwin_Service(socket);
                            }
                        }catch (Exception exception){
                            exception.printStackTrace();
                        }
                    }
                });
                ServiceThread.start();
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
    public static void RuntimeThread() {
        Thread runtime = new Thread(new Runnable() {
            @Override
            public void run() {
                linwinVOS.BootSystem();
            }
        });
        runtime.start();
    }
    public static void getServerSocketBoot() {
        try{
            System.out.println(" [Information] Try to start the Port for the Linwin Data Server");
            MyLinwin.bootNumber = MyLinwin.bootNumber + 1;
            ServerSocket serverSocket = new ServerSocket(MyLinwin.ServicePort);
            serverSocket.close();
        }catch (Exception exception){
            try {
                System.out.println(" [ERROR] ERROR BOOT Linwin Data Service Number="+MyLinwin.bootNumber+" | ERROR="+exception.getMessage());
                if (MyLinwin.bootNumber > 9) {
                    System.exit(0);
                }
                Thread.sleep(400);
                MyLinwin.getServerSocketBoot();
            }catch (Exception exception1){
                exception1.printStackTrace();
            }
        }
    }
    public static void LoadFiles() {
        try{
            if (VosDatabse.getRealFileExists("../../config/service/Service.json")) {
                String port = Json.readJson("../../config/service/Service.json","Service-Port");
                MyLinwin.ServicePort = Integer.valueOf(port);
            }else {
                System.out.println("[ERROR] DO NOT FIND CONFIG FILE");
            }
        }catch (Exception exception){
            System.out.println("[ERROR] "+exception.getMessage());
        }
    }
    public static void MyLinwin_Service(Socket socket) {
        try{
            System.out.println("[Message] Connect="+socket.getInetAddress());
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String getRequests = bufferedReader.readLine();
            String text_1 = "Logon=";
            String text_2 = "?Passwd=";
            String text_3 = "?Command=";
            int s_logon = getRequests.indexOf(text_1);
            int s_passwd = getRequests.indexOf(text_2);
            int s_command = getRequests.indexOf(text_3);
            if (s_logon != -1 && s_passwd != -1 && s_command != -1 && s_passwd > s_logon && s_passwd < s_command) {
                String logonUser = getRequests.substring(s_logon+text_1.length(),s_passwd);
                String md5_passwd = getRequests.substring(s_passwd+text_2.length(),s_command);
                String command = getRequests.substring(s_command+text_3.length(),getRequests.length());
                if (logon.Logon_LinwinVOS(logonUser,md5_passwd)) {
                    outputStream.write("Logon Successful!".getBytes());
                    outputStream.flush();

                    MydbEngine mydbEngine = new MydbEngine();
                    mydbEngine.setUser(logonUser);
                    mydbEngine.execLdbScript(command);
                    if (mydbEngine.getReturn() == null) {
                        outputStream.write("Error Command and Script".getBytes());
                        outputStream.flush();
                        socket.close();
                    }
                    outputStream.write(mydbEngine.getReturn().getBytes());
                    socket.close();
                    outputStream.close();
                }else {
                    outputStream.write("Passwd Or UserName Error!".getBytes());
                    outputStream.flush();
                    socket.close();
                }
            }else {
                outputStream.write("Send Message Error".getBytes());
                outputStream.flush();
                outputStream.close();
                inputStream.close();
                socket.close();
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
}
