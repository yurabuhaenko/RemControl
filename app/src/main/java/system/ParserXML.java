package system;


import android.util.Log;

import javax.xml.parsers.*;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.w3c.dom.*;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import org.json.XML;

import container.*;
import container.Comment;

/**
 * Created by root on 21.09.15.
 */


public class ParserXML {


    public static ContainerSignInResponse getEntityFromAutorizationRequest(String xmlString) {
        ContainerSignInResponse signInResponse = new ContainerSignInResponse();

        JSONObject jsonObj = null;

        try {
            jsonObj = XML.toJSONObject(xmlString);


            if(jsonObj.has("Response")) {
                JSONObject jsonObjResponse = jsonObj.getJSONObject("Response");
                jsonObj = null;

                if (jsonObjResponse.has("Error")) {

                    jsonObj = jsonObjResponse.getJSONObject("Error");
                    signInResponse.isError = true;
                    signInResponse.code = jsonObj.getString("code");
                    signInResponse.message = jsonObj.getString("message");

                } else {
                    signInResponse.isError = false;

                    jsonObj = jsonObjResponse.getJSONObject("SignIn");
                    JSONObject profile = jsonObj.getJSONObject("Profile");
                    JSONObject appeals = jsonObj.getJSONObject("Appeals");

                    signInResponse.user = getUserFromJSONObject(profile);
                    signInResponse.appealList = getAllAppealsFromJSONObject(appeals);
                }
            }else{
                JSONObject jsonObjResponse = jsonObj.getJSONObject("Responce");
                jsonObj = null;

                if (jsonObjResponse.has("Error")) {

                    jsonObj = jsonObjResponse.getJSONObject("Error");
                    signInResponse.isError = true;
                    signInResponse.code = jsonObj.getString("code");
                    signInResponse.message = jsonObj.getString("message");

                } else {
                    signInResponse.isError = false;

                    jsonObj = jsonObjResponse.getJSONObject("SignIn");
                    JSONObject profile = jsonObj.getJSONObject("Profile");
                    JSONObject appeals = jsonObj.getJSONObject("Appeals");

                    signInResponse.user = getUserFromJSONObject(profile);
                    signInResponse.appealList = getAllAppealsFromJSONObject(appeals);
                }
            }

        } catch (JSONException e) {
            Log.e("JSON exception", e.getMessage());
            e.printStackTrace();
        }


        return signInResponse;
    }
//////////////////////////pairsTasks.add(new BasicNameValuePair("Surname", myParser.getAttributeValue(null,"Surname")))


    private static User getUserFromJSONObject(JSONObject profile) {
        String email, surname, name;
        User user = new User("", "", "");
        try {
            email = profile.getString("Email");
            surname = profile.getString("Surname");
            name = profile.getString("Name");
            user = new User(name, email, surname);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }


    private static ArrayList<Appeal> getAllAppealsFromJSONObject(JSONObject objectAppeals) throws JSONException {
        ArrayList<Appeal> appealsList = new ArrayList<>();

        String strCheck = objectAppeals.getString("Appeal");


        if(strCheck.charAt(0) == '[') {


            JSONArray jsonArrayAppeals = objectAppeals.getJSONArray("Appeal");


            for (int i = 0; i < jsonArrayAppeals.length(); ++i) {

                JSONObject jsonAppeal = jsonArrayAppeals.getJSONObject(i);

                ArrayList<String> photosUrl = new ArrayList<>();
                ArrayList<container.Comment> comments = new ArrayList<>();

                if (!(jsonAppeal.getString("Comments").length() == 0)) {
                    JSONObject app = jsonAppeal.getJSONObject("Comments");
                    comments = getCommentsFromJSONObject(app);
                }

                //JSONObject phts = jsonAppeal.getJSONObject("Photos");
                //if (jsonAppeal.has("Photos")) {
                if (!(jsonAppeal.getString("Photos").length() == 0)) {
                    JSONObject phts = jsonAppeal.getJSONObject("Photos");

                    photosUrl = getPhotosUrlFromJSONObject(phts);
                }


                appealsList.add(new Appeal(jsonAppeal.getInt("id"), jsonAppeal.getInt("DistrictId"), jsonAppeal.getString("AdressStreet"),
                        jsonAppeal.getString("AdressHouse"), jsonAppeal.getString("AppealText"), jsonAppeal.getString("AppealStatus"),
                        jsonAppeal.getInt("Color"), photosUrl, comments));


            }
        }else{

            objectAppeals = objectAppeals.getJSONObject("Appeal");

                ArrayList<String> photosUrl = new ArrayList<>();
                ArrayList<container.Comment> comments = new ArrayList<>();

                if (!(objectAppeals.getString("Comments").length() == 0)){
                    JSONObject app = objectAppeals.getJSONObject("Comments");
                    comments = getCommentsFromJSONObject(app);
                }


                //if (objectAppeals.has("Photos")) {
                if (!(objectAppeals.getString("Photos").length() == 0)) {
                    JSONObject phts = objectAppeals.getJSONObject("Photos");

                    photosUrl = getPhotosUrlFromJSONObject(phts);
                }


                appealsList.add(new Appeal(objectAppeals.getInt("id"), objectAppeals.getInt("DistrictId"), objectAppeals.getString("AdressStreet"),
                        objectAppeals.getString("AdressHouse"), objectAppeals.getString("AppealText"), objectAppeals.getString("AppealStatus"),
                        objectAppeals.getInt("Color"), photosUrl, comments));

        }

        return appealsList;
    }


    private static ArrayList<Comment> getCommentsFromJSONObject(JSONObject app ) throws JSONException {
        ArrayList<container.Comment> comments = new ArrayList<>();

        if(app.getString("Comment").contains("[")){
            JSONArray jsonComments = app.getJSONArray("Comment");

            for(int j = 0; j < jsonComments.length(); ++j){
                JSONObject comment = jsonComments.getJSONObject(j);

                String type = comment.getString("type");
                String text = comment.getString("text");
                String date = comment.getString("date");

                comments.add(new Comment(type,date,text));
            }
        }else {
            JSONObject comment = app.getJSONObject("Comment");

            String type = comment.getString("type");
            String text = comment.getString("text");
            String date = comment.getString("date");

            comments.add(new Comment(type,date,text));

        }
        return comments;
    }

    private static ArrayList<String> getPhotosUrlFromJSONObject(JSONObject phts) throws JSONException {
        ArrayList<String> photosUrl = new ArrayList<>();

        if(phts.getString("Photo").contains("[")){
            JSONArray jsonPhotos = phts.getJSONArray("Photo");

            for(int j = 0; j < jsonPhotos.length(); ++j){
                JSONObject photo = jsonPhotos.getJSONObject(j);

                String url = photo.getString("url");

                photosUrl.add(url);
            }
        }else{
            JSONObject photo = phts.getJSONObject("Photo");

            String url = photo.getString("url");

            photosUrl.add(url);
        }

        return photosUrl;
    }



    public static int getHouseIdFromGetNearestAddressesByCoordinatesRequest(String xmlString){
        ContainerSignInResponse signInResponse = new ContainerSignInResponse();

        JSONObject jsonObj = null;
        int adressId = -1;


        try {
            jsonObj = XML.toJSONObject(xmlString);


            if(jsonObj.has("Response")) {
                JSONObject jsonObjResponse = jsonObj.getJSONObject("Response");
                if(jsonObjResponse.has("NearestAddresses")){
                    jsonObj = jsonObjResponse.getJSONObject("NearestAddresses") ;
                    jsonObj = jsonObj.getJSONObject("House");
                    adressId = jsonObj.getInt("id");
                }
            }

        } catch (JSONException e) {
            Log.e("JSON exception", e.getMessage());
            e.printStackTrace();
        }
        return adressId;
    }



}