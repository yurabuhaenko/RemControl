package system;

import android.app.Application;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import container.ContainerAppealSent;
import container.ContainerUser;

/**
 * Created by Denver on 05.09.2015.
 */

public class RemControlApplication extends Application {

    private ContainerUser user;
    private SharedPreferences sPref;
    private List<ContainerAppealSent> appealSents;

    private String cookiesStr;


    @Override
    public void onCreate() {
        super.onCreate();

        if (checkIsSavedUser() == true){
            user = loadUser();
        }

        cookiesStr = null;

    }

    public void setAppealSents(List<ContainerAppealSent> appealSents){
        this.appealSents = appealSents;
    }
    public List<ContainerAppealSent> getAppealSents(){return appealSents;}

    public void setUser(ContainerUser user) {
        this.user = user;
    }
    public ContainerUser getUser(){return user;}




    //////////////////////preferences saving

    private static final String PREFERENCES_USER = "userPref";
    private static final String SAVED_USER_EMAIL = "userEmail";
    private static final String SAVED_USER_PASSWORD = "userPassword";
    private static final String SAVED_USER_NAME = "userName";


    public void saveUser() {
        sPref = getSharedPreferences(PREFERENCES_USER, MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_USER_EMAIL, user.getEmail());
        ed.putString(SAVED_USER_PASSWORD, user.getPassword());
        ed.putString(SAVED_USER_NAME, user.getName());
        ed.commit();
    }

    public ContainerUser loadUser() {
        sPref = getSharedPreferences(PREFERENCES_USER, MODE_PRIVATE);
        String email = sPref.getString(SAVED_USER_EMAIL, "");
        String password = sPref.getString(SAVED_USER_PASSWORD, "");
        String name = sPref.getString(SAVED_USER_NAME, "");

        ContainerUser user = new ContainerUser(name, email, password);
        return user;
    }

    public boolean checkIsSavedUser() {
        sPref = getSharedPreferences(PREFERENCES_USER, MODE_PRIVATE);
        String chk = sPref.getString(SAVED_USER_EMAIL, "");
        if (chk != null && chk != "") {
            return true;
        } else {
            return false;
        }
    }

    private void deleteUserFromSaved(){
        sPref = getSharedPreferences(PREFERENCES_USER, MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_USER_EMAIL, "");
        ed.putString(SAVED_USER_PASSWORD, "");
        ed.putString(SAVED_USER_NAME, "");
        ed.commit();
    }



}
