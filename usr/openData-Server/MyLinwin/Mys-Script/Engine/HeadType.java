package Engine;

public class HeadType {
    private String user;
    private String passwd;
    private String remote;
    private String port;

    public String getUser() {
        return this.user;
    }
    public String getPasswd() {
        return this.passwd;
    }
    public String getRemote() {
        return this.remote;
    }
    public String getPort() {
        return this.port;
    }
    public void dealHeadType(String path) {
        String remote = Json.readJson(path,"remote");
        String port = Json.readJson(path,"port");
        String user = Json.readJson(path,"user");
        String passwd = Json.readJson(path,"passwd");

        this.port = port;
        this.passwd = passwd;
        this.remote = remote;
        this.user = user;
    }
}
