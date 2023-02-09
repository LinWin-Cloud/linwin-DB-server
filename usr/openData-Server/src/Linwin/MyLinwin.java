
import LinwinVOS.FileSystem.VosDatabase;
import LogService.LogService;
import LinwinVOS.data.*;
import LinwinVOS.LinwinVOS;
import LinwinVOS.Users.logon;
import LinwinVOS.runtime.MydbEngine;
import ThreadSocket.ThreadSocket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MyLinwin {
    public static String logPath;
    public static int ServicePort;
    private static int bootNumber = 0;
    public static int updateLog = 1000 * 60 * 60;
    public static LogService logService = new LogService();
    private static ThreadSocket IO_Socket = new ThreadSocket();
    public static LinwinVOS linwinVOS = new LinwinVOS();
    public static int safeConnection = 500;
    public static HashMap<String,Integer> safeVisit = new HashMap<String, Integer>();
    public static void main(String[] args)
    {
        /**
         * Load all the config files and start.
         */
        Init init = new Init();

        MyLinwin.LoadFiles();
        MyLinwin.RuntimeThread(logService);
        init.loadLogModule();

        System.out.println(" [Information] Boot Linwin Data Service!");
        System.out.println(" [Config] Start Service Port="+MyLinwin.ServicePort);
        System.out.println(" [Info ] Boot Successful!");
        MyLinwin.getServerSocketBoot();

        Thread HTTP_SERVICE = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(MyLinwin.ServicePort);
                    ExecutorService executorService = Executors.newFixedThreadPool(1000);
                    while (true) {
                        MyLinwin.HTTP_SERVICE(executorService,serverSocket);
                    }
                }catch (Exception exception){
                    exception.printStackTrace();
                }
            }
        });
        HTTP_SERVICE.start();
    }
    public static void HTTP_SERVICE(ExecutorService executorService,ServerSocket serverSocket) throws Exception {
        Socket socket = serverSocket.accept();
        executorService.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                MyLinwin.HTTP_DEAL(socket,executorService);
                return 0;
            }
        });
    }
    public static void HTTP_DEAL(Socket socket,ExecutorService executorService) {
        try{
            Integer integer = MyLinwin.safeVisit.get(socket.getInetAddress().toString());
            if (integer == null) {
                MyLinwin.safeVisit.put(socket.getInetAddress().toString(),1);
            }else {
                int s = integer;
                if (s >= MyLinwin.safeConnection) {
                    socket.close();
                    return;
                }
                String getIP = socket.getInetAddress().toString();
                MyLinwin.safeVisit.put(getIP,MyLinwin.safeVisit.get(getIP)+1);
            }

            MyLinwin.MyLinwin_Service(socket,executorService);
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
    public static void RuntimeThread(LogService logService) {
        Thread runtime = new Thread(new Runnable() {
            @Override
            public void run() {
                linwinVOS.BootSystem();
            }
        });
        runtime.start();

        String ioSocket = "false";

        Thread safeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try{
                        Thread.sleep(1000*15);
                        MyLinwin.safeVisit.clear();
                    }catch (Exception exception){
                        exception.printStackTrace();
                    }
                }
            }
        });
        safeThread.start();


        //outData.start();
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
            if (VosDatabase.getRealFileExists("../../config/service/Service.json")) {
                String port = Json.readJson("../../config/service/Service.json","Service-Port");
                String logPath = Json.readJson("../../config/service/Service.json","Log-Path");
                String logUpdate = Json.readJson("../../config/service/Service.json","Update-Log");
                String safeConnection = Json.readJson("../../config/service/Service.json","Connection Frequency");

                MyLinwin.ServicePort = Integer.valueOf(port);
                MyLinwin.logPath = logPath;
                MyLinwin.updateLog = Integer.valueOf(logUpdate);
                MyLinwin.safeConnection = Integer.valueOf(safeConnection);

            }else {
                System.out.println("[ERROR] DO NOT FIND CONFIG FILE OF CONFIG FILE ERROR!");
            }
        }catch (Exception exception){
            System.out.println("[ERROR] "+exception.getMessage());
            System.out.println("[ERROR] Config Error!");
            System.out.println("[Info] Boot Failed!");
            System.exit(0);
        }
    }
    public static void MyLinwin_Service(Socket socket,ExecutorService executorService) {
        try{
            //System.out.println("[Message] Connect="+socket.getInetAddress());
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);
            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String getRequests = bufferedReader.readLine();
            if (getRequests == null) {
                socket.close();
            }else {
                getRequests = java.net.URLDecoder.decode(getRequests);
                getRequests = getRequests.substring(getRequests.indexOf(" ")+1,getRequests.lastIndexOf("HTTP/")-1);

                String text_1 = "Logon=";
                String text_2 = "?Passwd=";
                String text_3 = "?Command=";
                int s_logon = getRequests.indexOf(text_1);
                int s_passwd = getRequests.indexOf(text_2);
                int s_command = getRequests.indexOf(text_3);
               // System.out.println(s_logon+" "+s_command+" "+s_passwd);
                if (s_logon != -1 && s_passwd != -1 && s_command != -1 && s_passwd > s_logon && s_passwd < s_command) {
                    String logonUser = getRequests.substring(s_logon+text_1.length(),s_passwd);
                    String md5_passwd = getRequests.substring(s_passwd+text_2.length(),s_command);
                    String command = getRequests.substring(s_command+text_3.length());
                    //System.out.println(logonUser+" "+md5_passwd+" "+command);
                    MyLinwin.logService.outLog(" LOGIN="+logonUser+"  Command="+command+"  IP="+socket.getInetAddress());

                    if (logon.Logon_LinwinVOS(logonUser,md5_passwd)) {

                        printWriter.println("HTTP/1.1 200 OK");
                        printWriter.println("Access-Control-Allow-Headers: *");
                        printWriter.println("Access-Control-Allow-Origin: *");
                        printWriter.println("Content-Type: text/plain");
                        printWriter.println();
                        printWriter.flush();

                        MydbEngine mydbEngine = new MydbEngine();
                        mydbEngine.setUser(logonUser);
                        mydbEngine.setExecutorService(executorService);
                        mydbEngine.execLdbScript(command,logonUser);

                        if (mydbEngine.getReturn() == null) {
                            printWriter.println("Error Command and Script");
                            outputStream.flush();
                            socket.close();
                        }else {
                            printWriter.println(mydbEngine.getReturn());
                            printWriter.flush();
                            socket.close();
                            printWriter.close();
                        }
                    }else {
                        printWriter.println("HTTP/1.1 400 OK");
                        printWriter.println("Access-Control-Allow-Headers: *");
                        printWriter.println("Access-Control-Allow-Origin: *");
                        printWriter.println("Content-Type: text/plain");
                        printWriter.println();
                        printWriter.flush();
                        printWriter.println("Passwd Or UserName Error!");
                        printWriter.flush();
                        socket.close();
                    }
                }else {
                    printWriter.println("HTTP/1.1 400 OK");
                    printWriter.println("Access-Control-Allow-Headers: *");
                    printWriter.println("Access-Control-Allow-Origin: *");
                    printWriter.println("Content-Type: text/plain");
                    printWriter.println();
                    printWriter.println("Send Message Error");
                    printWriter.flush();
                    printWriter.close();
                    inputStream.close();
                    socket.close();
                }
            }
        }catch (Exception exception){
            //MyLinwin.logService.outLog("[ERR] "+exception.getMessage());
            exception.printStackTrace();
        }
    }
}
