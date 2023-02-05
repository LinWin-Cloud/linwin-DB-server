public class boot {
    public static void main(String[] args) {
        try{
            System.out.println("Boot linwinSoft openData Server");
            Runtime.getRuntime().exec("/opt/linwin-console/bootLinwinSQL.sh");
            System.out.println("=== Successful ===");
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
}
