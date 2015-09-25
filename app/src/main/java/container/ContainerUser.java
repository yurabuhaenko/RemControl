package container;

/**
 * Created by Denver on 05.09.2015.
 */
public class ContainerUser {


    private String name;
    private String email;
    private String password;

    public ContainerUser(String name, String email, String password){
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getName() { return name;  }

    public String getPassword() {
        return password;
    }

}
