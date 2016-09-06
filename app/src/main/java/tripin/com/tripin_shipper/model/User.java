package tripin.com.tripin_shipper.model;

/**
 * Created by Android on 05/08/16.
 */
public class User {
    private String username;
    private String password;
    private String user_id;
    private String outh_name;
    private String access_token;

public User()
{
this.username = username;
    this.access_token = access_token;
    this.user_id = user_id;
}
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOuth_name() {
        return outh_name;
    }

    public void setOuth_name(String outh_name) {
        this.outh_name = outh_name;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String setAccess_token(String access_token) {
        this.access_token = access_token;
        return access_token;
    }

}
