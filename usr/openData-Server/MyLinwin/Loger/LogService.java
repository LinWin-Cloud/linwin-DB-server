
import java.io.FileWriter;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;

public class LogService {
  private int NewFileWrite_Time = 1000 * 60 * 60;
  private FileWriter fileWriter;
  public String fileName;
  public String setNAME;
  public String path;
  public LogService() {
  }
  public void print(String message) {
    PrintStream printStream = new PrintStream(System.out);
    printStream.println(message);
  }
  public void printInfo(String message,String type) {
    this.print("{"+type+"} Message= "+message+" ;["+this.getNowTime()+"]");
  }
  public String getNowTime() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd.HH:mm:ss");
    java.util.Date date = new Date(System.currentTimeMillis());
    return simpleDateFormat.format(date);
  }
  public Boolean outLog(String logMessage) {
    try{
      File file = new File(this.fileName);
      if (!file.exists() || !file.isFile()) {
        file.createNewFile();
      }
      this.fileWriter.write("{"+this.getNowTime()+"} "+logMessage+"\n");
      this.fileWriter.flush();
      return true;
    }catch (Exception exception){
      exception.printStackTrace();
      return false;
    }
  }
  public void setAutoNewLog(int time) {
    this.NewFileWrite_Time = time;
  }
  public void setPath(String path) {
    this.path = path;
  }
  public void setFileName(String fileName) {
    this.fileName = this.path + "/" + fileName + this.getNowTime() + ".log";
    this.setNAME = fileName;
  }
  public void run() {
    try{
      Thread logFileWriteThread = new Thread(this.WriteRunnable());
      logFileWriteThread.start();

    }catch (Exception exception){
      exception.printStackTrace();
    }
  }
  private Runnable WriteRunnable() {
    while (true) {
      try{
        if (this.fileWriter == null){
          try{
            this.fileName = this.path + "/" + this.setNAME + this.getNowTime() + ".log";
            this.fileWriter = new FileWriter(this.fileName,true);
            continue;
          }catch (Exception exception){
            exception.printStackTrace();
          }
        }
        this.fileName = this.path + "/" + this.setNAME + this.getNowTime() + ".log";
        this.fileWriter = new FileWriter(this.fileName,true);
        Thread.sleep(this.NewFileWrite_Time);
      }catch (Exception exception){
        exception.printStackTrace();
        System.out.println("[ERR] Config Error!");
        System.exit(0);
      }
    }
  }
}