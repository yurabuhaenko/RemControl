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
 * Created by Denver on 05.09.2015.
 */

public class RemControlApplication extends Application {

    private User user;
    private SharedPreferences sPref;
    private ArrayList<Appeal> appealSents;

    public  String cookie;



    @Override
    public void onCreate() {
        super.onCreate();

        if (checkIsSavedUser() == true){
            user = loadUser();
        }

        cookie = null;

    }

    public void setAppealSents(ArrayList<Appeal> appealSents){
        this.appealSents = appealSents;
    }
    public ArrayList<Appeal> getAppealSents(){return appealSents;}

    public void setUser(User user) {
        this.user = user;
    }
    public User getUser(){return user;}
    public void deleteUser(){user = null;}




    //////////////////////preferences saving

    private static final String PREFERENCES_USER = "userPref";
    private static final String SAVED_USER_EMAIL = "userEmail";
    private static final String SAVED_USER_PASSWORD = "userPassword";
    private static final String SAVED_USER_NAME = "userName";
    private static final String SAVED_USER_SURNAME = "userSurname";


    public void saveUser() {
        sPref = getSharedPreferences(PREFERENCES_USER, MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_USER_EMAIL, user.getEmail());
        ed.putString(SAVED_USER_PASSWORD, user.getPassword());
        ed.putString(SAVED_USER_NAME, user.getName());
        ed.putString(SAVED_USER_SURNAME, user.getSurname());
        ed.commit();
    }

    public User loadUser() {
        sPref = getSharedPreferences(PREFERENCES_USER, MODE_PRIVATE);
        String email = sPref.getString(SAVED_USER_EMAIL, "");
        String password = sPref.getString(SAVED_USER_PASSWORD, "");
        String name = sPref.getString(SAVED_USER_NAME, "");
        String surname = sPref.getString(SAVED_USER_SURNAME, "");
        User user = new User(name, email, surname, password);
        return user;
    }

    public boolean checkIsSavedUser() {
        sPref = getSharedPreferences(PREFERENCES_USER, MODE_PRIVATE);
        String chk = sPref.getString(SAVED_USER_EMAIL, "");
        if (chk != null && !chk.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public void deleteUserFromSaved(){
        sPref = getSharedPreferences(PREFERENCES_USER, MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_USER_EMAIL, "");
        ed.putString(SAVED_USER_PASSWORD, "");
        ed.putString(SAVED_USER_NAME, "");
        ed.putString(SAVED_USER_SURNAME, "");
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



    public ArrayList<PostponedAppeal> getSavedAppeals(){
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
            listAppeals.add(new PostponedAppeal(text, lang, lon, datetime, photo1, photo2, photo3, photo4, photo5));
        }


        return listAppeals;
    }


    public void addAppealToSaved(PostponedAppeal appeal){
        sPref = getSharedPreferences(PREFERENCES_POSTPONDED_APPEALS, MODE_PRIVATE);
        int numbOfSaved = sPref.getInt(NUMBER_OF_SAVED_APPEALS, 0);

        SharedPreferences.Editor ed = sPref.edit();

        ed.putString(SAVED_TEXT + Integer.toString(numbOfSaved), appeal.getText());
        ed.putString(SAVED_LONGITUDE + Integer.toString(numbOfSaved),appeal.getLon());
        ed.putString(SAVED_LATITUDE + Integer.toString(numbOfSaved), appeal.getLat());
        ed.putString(SAVED_DATETIME + Integer.toString(numbOfSaved), appeal.getDatetime());
        ed.putString(SAVED_PHOTO1 + Integer.toString(numbOfSaved), appeal.getPhoto1());
        ed.putString(SAVED_PHOTO2 + Integer.toString(numbOfSaved), appeal.getPhoto2());
        ed.putString(SAVED_PHOTO3 + Integer.toString(numbOfSaved), appeal.getPhoto3());
        ed.putString(SAVED_PHOTO4 + Integer.toString(numbOfSaved), appeal.getPhoto4());
        ed.putString(SAVED_PHOTO5 + Integer.toString(numbOfSaved), appeal.getPhoto5());

        ed.putInt(NUMBER_OF_SAVED_APPEALS, numbOfSaved+1);
        ed.commit();


    }


    public void deleteAllSavedAppeals(){
        sPref = getSharedPreferences(PREFERENCES_POSTPONDED_APPEALS, MODE_PRIVATE);
        int numbOfSaved = sPref.getInt(NUMBER_OF_SAVED_APPEALS, 0);
        SharedPreferences.Editor ed = sPref.edit();
        for (int i = 0; i < numbOfSaved;++i){
            ed.putString(SAVED_TEXT + Integer.toString(i), "");
            ed.putString(SAVED_LONGITUDE + Integer.toString(i), "");
            ed.putString(SAVED_LATITUDE + Integer.toString(i), "");
            ed.putString(SAVED_DATETIME + Integer.toString(i), "");
            ed.putString(SAVED_PHOTO1 + Integer.toString(i), "");
            ed.putString(SAVED_PHOTO2 + Integer.toString(i), "");
            ed.putString(SAVED_PHOTO3 + Integer.toString(i), "");
            ed.putString(SAVED_PHOTO4 + Integer.toString(i), "");
            ed.putString(SAVED_PHOTO5 + Integer.toString(i), "");
        }
        ed.putInt(NUMBER_OF_SAVED_APPEALS, 0);
        ed.commit();
    }


    public void deleteAppealByNumber(int number){
        sPref = getSharedPreferences(PREFERENCES_POSTPONDED_APPEALS, MODE_PRIVATE);
        int numbOfSaved = sPref.getInt(NUMBER_OF_SAVED_APPEALS, 0);

        if (numbOfSaved > 1 && number < numbOfSaved) {
            SharedPreferences.Editor ed = sPref.edit();
            for (int i = number; i < numbOfSaved-1; ++i){
                String text = sPref.getString(SAVED_TEXT + Integer.toString(i+1), "");
                String lon = sPref.getString(SAVED_LONGITUDE + Integer.toString(i+1), "");
                String lang = sPref.getString(SAVED_LATITUDE + Integer.toString(i + 1), "");
                String datetime = sPref.getString(SAVED_DATETIME + Integer.toString(i + 1), "");
                String photo1 = sPref.getString(SAVED_PHOTO1 + Integer.toString(i + 1), "");
                String photo2 = sPref.getString(SAVED_PHOTO2 + Integer.toString(i + 1), "");
                String photo3 = sPref.getString(SAVED_PHOTO3 + Integer.toString(i + 1), "");
                String photo4 = sPref.getString(SAVED_PHOTO4 + Integer.toString(i + 1), "");
                String photo5 = sPref.getString(SAVED_PHOTO5 + Integer.toString(i + 1), "");

                ed.putString(SAVED_TEXT + Integer.toString(i), text);
                ed.putString(SAVED_LONGITUDE + Integer.toString(i), lon);
                ed.putString(SAVED_LATITUDE + Integer.toString(i), lang);
                ed.putString(SAVED_DATETIME + Integer.toString(i), datetime);
                ed.putString(SAVED_PHOTO1 + Integer.toString(i), photo1);
                ed.putString(SAVED_PHOTO2 + Integer.toString(i), photo2);
                ed.putString(SAVED_PHOTO3 + Integer.toString(i), photo3);
                ed.putString(SAVED_PHOTO4 + Integer.toString(i), photo4);
                ed.putString(SAVED_PHOTO5 + Integer.toString(i), photo5);
            }
            ed.putInt(NUMBER_OF_SAVED_APPEALS, numbOfSaved-1);
            ed.commit();
        }
        if(numbOfSaved == 1){
            SharedPreferences.Editor ed = sPref.edit();
            ed.putString(SAVED_TEXT + Integer.toString(0), "");
            ed.putString(SAVED_LONGITUDE + Integer.toString(0), "");
            ed.putString(SAVED_LATITUDE + Integer.toString(0), "");
            ed.putString(SAVED_DATETIME + Integer.toString(0), "");
            ed.putString(SAVED_PHOTO1 + Integer.toString(0), "");
            ed.putString(SAVED_PHOTO2 + Integer.toString(0), "");
            ed.putString(SAVED_PHOTO3 + Integer.toString(0), "");
            ed.putString(SAVED_PHOTO4 + Integer.toString(0), "");
            ed.putString(SAVED_PHOTO5 + Integer.toString(0), "");
            ed.putInt(NUMBER_OF_SAVED_APPEALS, 0);
            ed.commit();
        }
    }




}
