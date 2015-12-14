package container;

/**
 * @author yurabuhaenko
 * Class-container to hold all user information
 */
public class User {


    private String name;
    private String email;
    private String surname;
    private String password;

    /**
     * default constructor
     */
    public User(){
        this.surname = "";
        this.name = "";
        this.email = "";
        this.password = "";
    }

    /**
     * constructor without password
     * @param name
     * @param email
     * @param surname
     */
    public User(String name, String email, String surname){
        this.surname = surname;
        this.name = name;
        this.email = email;
    }

    /**
     * constructor for all fields
     * @param name
     * @param email
     * @param surname
     * @param password
     */
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
