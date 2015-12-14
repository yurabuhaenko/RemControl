package system;

import android.app.Application;
import android.content.SharedPreferences;

import org.apache.http.cookie.Cookie;

import java.util.ArrayList;
import java.util.List;

import container.Appeal;
import container.PostponedAppeal;
import container.User;

/**
 * Class which holds and save information of user and postponed appeals
 * It can be accessed from each activity by "(RemControlApplication)getApplicationContext();"
 * @author yurabuhaenko
 */

public class RemControlApplication extends Application {

    private User user;
    private SharedPreferences sPref;
    private ArrayList<Appeal> appealSents;

    public  String cookie;


    private ArrayList<PostponedAppeal> postponedAppeals;


    /**
     * default on create method
     * loading user from shared preferences
     * loading saved postponed appeals
     */
    @Override
    public void onCreate() {
        super.onCreate();

        if (checkIsSavedUser() == true){
            user = loadUser();
        }

        cookie = null;
        //deleteAllPostponedAppeals();
        postponedAppeals = new ArrayList<PostponedAppeal>();
        loadSavedAppeals();
    }

    /**
     * @param appealSents which must be setted
     */
    public void setAppealSents(ArrayList<Appeal> appealSents){
        this.appealSents = appealSents;
    }

    public ArrayList<Appeal> getAppealSents(){return appealSents;}

    public void setUser(User user) {
        this.user = user;
    }
    public User getUser(){return user;}

    /**
     * set user object null
     */
    public void deleteUser(){user = null;}


    public ArrayList<PostponedAppeal> getPostponedAppeals() {
        return postponedAppeals;
    }


    public void setPostponedAppeals(ArrayList<PostponedAppeal> postponedAppeals) {
        this.postponedAppeals = postponedAppeals;
    }




    //////////////////////preferences saving

    private static final String PREFERENCES_USER = "userPref";
    private static final String SAVED_USER_EMAIL = "userEmail";
    private static final String SAVED_USER_PASSWORD = "userPassword";
    private static final String SAVED_USER_NAME = "userName";
    private static final String SAVED_USER_SURNAME = "userSurname";

    /**
     * save all fields from object user to shared preferences
     */
    public void saveUser() {
        sPref = getSharedPreferences(PREFERENCES_USER, MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_USER_EMAIL, user.getEmail());
        ed.putString(SAVED_USER_PASSWORD, user.getPassword());
        ed.putString(SAVED_USER_NAME, user.getName());
        ed.putString(SAVED_USER_SURNAME, user.getSurname());
        ed.commit();
    }

    /**
     * Method loads all user fields from saved shared preferences
     * @return User object loaded from shared preferences
     */
    public User loadUser() {
        sPref = getSharedPreferences(PREFERENCES_USER, MODE_PRIVATE);
        String email = sPref.getString(SAVED_USER_EMAIL, "");
        String password = sPref.getString(SAVED_USER_PASSWORD, "");
        String name = sPref.getString(SAVED_USER_NAME, "");
        String surname = sPref.getString(SAVED_USER_SURNAME, "");
        User user = new User(name, email, surname, password);
        return user;
    }

    /**
     * Check if was saved user in shared preferences
     * @return true is user was saved in shared preferences
     */
    public boolean checkIsSavedUser() {
        sPref = getSharedPreferences(PREFERENCES_USER, MODE_PRIVATE);
        String chk = sPref.getString(SAVED_USER_EMAIL, "");
        if (chk != null && !chk.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method celears all user information saved in shared preferences
     */
    public void deleteUserFromSaved(){
        sPref = getSharedPreferences(PREFERENCES_USER, MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.clear();
        ed.commit();
    }

    ////////////////////////////////////////////
    ///////////saving postponded////////////////
    ////////////////////////////////////////////




    private static final String PREFERENCES_POSTPONDED_APPEALS  = "postpondPref";
    private static final String NUMBER_OF_SAVED_APPEALS = "numberOfSavedAppeals";
    private static final String SAVED_TEXT = "appealText";
    private static final String SAVED_LONGITUDE = "appealLong";
    private static final String SAVED_LATITUDE = "appealLat";
    private static final String SAVED_DATETIME= "DATETIME";
    private static final String SAVED_PHOTO1 = "PHOTO1";
    private static final String SAVED_PHOTO2 = "PHOTO2";
    private static final String SAVED_PHOTO3 = "PHOTO3";
    private static final String SAVED_PHOTO4 = "PHOTO4";
    private static final String SAVED_PHOTO5 = "PHOTO5";


    /**
     * Loading all saved appeals from shared preferences and save it on postponedAppeals
     */
    private void loadSavedAppeals(){
        sPref = getSharedPreferences(PREFERENCES_POSTPONDED_APPEALS, MODE_PRIVATE);

        int numbOfSaved = sPref.getInt(NUMBER_OF_SAVED_APPEALS, 0);

        ArrayList<PostponedAppeal> listAppeals = new ArrayList<PostponedAppeal>();

        for(int i = 0; i < numbOfSaved; ++i){
            String text = sPref.getString(SAVED_TEXT + Integer.toString(i), "");
            String lon = sPref.getString(SAVED_LONGITUDE + Integer.toString(i), "");
            String lang = sPref.getString(SAVED_LATITUDE + Integer.toString(i), "");
            String datetime = sPref.getString(SAVED_DATETIME + Integer.toString(i), "");
            String photo1 = sPref.getString(SAVED_PHOTO1 + Integer.toString(i), "");
            String photo2 = sPref.getString(SAVED_PHOTO2 + Integer.toString(i), "");
            String photo3 = sPref.getString(SAVED_PHOTO3 + Integer.toString(i), "");
            String photo4 = sPref.getString(SAVED_PHOTO4 + Integer.toString(i), "");
            String photo5 = sPref.getString(SAVED_PHOTO5 + Integer.toString(i), "");
            postponedAppeals.add(new PostponedAppeal(text, lang, lon, datetime, photo1, photo2, photo3, photo4, photo5));
        }
    }

    /**
     * Save all postponed appeals to shared preferences from postponedAppeals
     */
    public void savePostponedAppeals(){
        sPref = getSharedPreferences(PREFERENCES_POSTPONDED_APPEALS, MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.clear();
        ed.commit();

        ed = sPref.edit();
        ed.putInt(NUMBER_OF_SAVED_APPEALS, postponedAppeals.size());

        for(int i = 0; i < postponedAppeals.size(); ++i) {
            ed.putString(SAVED_TEXT + Integer.toString(i), postponedAppeals.get(i).getText());
            ed.putString(SAVED_LONGITUDE + Integer.toString(i), postponedAppeals.get(i).getLon());
            ed.putString(SAVED_LATITUDE + Integer.toString(i), postponedAppeals.get(i).getLat());
            ed.putString(SAVED_DATETIME + Integer.toString(i), postponedAppeals.get(i).getDatetime());
            ed.putString(SAVED_PHOTO1 + Integer.toString(i), postponedAppeals.get(i).getPhoto1());
            ed.putString(SAVED_PHOTO2 + Integer.toString(i), postponedAppeals.get(i).getPhoto2());
            ed.putString(SAVED_PHOTO3 + Integer.toString(i), postponedAppeals.get(i).getPhoto3());
            ed.putString(SAVED_PHOTO4 + Integer.toString(i), postponedAppeals.get(i).getPhoto4());
            ed.putString(SAVED_PHOTO5 + Integer.toString(i), postponedAppeals.get(i).getPhoto5());
        }
        ed.commit();


    }

    /**
     * Clears all postponed appeals from shared preferences
     */
    public void deleteAllPostponedAppeals(){
        sPref = getSharedPreferences(PREFERENCES_POSTPONDED_APPEALS, MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.clear();
        ed.commit();
    }




}
