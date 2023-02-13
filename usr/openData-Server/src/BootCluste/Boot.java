public class Boot {
    public static void main(String[] args) {
        try{
            System.out.println("Boot LinwinSoft Data Cluster Service");
            Runtime.getRuntime().exec("/opt/linwin-console/bootCluster.sh");
            System.out.println("=== Successful ===");
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
}
