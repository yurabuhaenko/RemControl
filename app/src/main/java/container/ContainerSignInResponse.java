package container;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yurabuhaenko
 *
 * Class-container for holding all information after parcing sign in responce
 */
public class ContainerSignInResponse {

    public boolean isError = true;
    public String message = "";
    public String code = "";

    public User user;
    public ArrayList<Appeal> appealList;



}
