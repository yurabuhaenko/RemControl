package container;

/**
 * Created by Denver on 05.09.2015.
 */
public class User {


    private String name;
    private String email;
    private String surname;
    private String password;


    public User(){
        this.surname = "";
        this.name = "";
        this.email = "";
        this.password = "";
    }

    public User(String name, String email, String surname){
        this.surname = surname;
        this.name = name;
        this.email = email;
    }

    public User(String name, String email, String surname, String password){
        this.surname = surname;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() { return name;  }

    public String getSurname() {
        return surname;
    }

    public String getPassword(){return password;}

    public void setName(String name){this.name = name;}

    public void setEmail(String email){this.email = email;}

    public void setSurname(String surname){this.surname = surname;}

    public void setPassword(String password){this.password = password;}

}
