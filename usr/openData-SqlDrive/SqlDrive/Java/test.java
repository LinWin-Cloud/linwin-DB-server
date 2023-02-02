import sqlDrive.SqlDo;
import sqlDrive.SqlDrive;

public class test {
    public static void main(String[] args) {
        SqlDrive sqlDrive = new SqlDrive("http://127.0.0.1:8888");
        sqlDrive.setPasswd("123456");
        sqlDrive.setUserName("root");
        SqlDo sqlDo = new SqlDo(sqlDrive);
        String[] a = sqlDo.listDatabase();
        for (String i : a) {
            System.out.println(" - "+i);
        }
    }
}
