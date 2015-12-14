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
 * @author SergPohh
 * Class with methods for parsing XML responses from server
 * Each Request has own parser method
 */
public class ParserXML {

    /**
     * Method to parse data from XML response from authorization request
     * @param xmlString string with XML response from server
     * @return ContainerSignInResponse object with all data from response
     * @see ContainerSignInResponse
     */
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

    /**
     * Method for getting all user data from sign in response
     * @param profile jsonObject which contains user data
     * @return User container object with parsed data
     * @see User
     */
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

    /**
     * Method for getting all appeals  from sign in response
     * @param objectAppeals  jsonObject which contains appeals
     * @return List of all Appeals from response
     * @see Appeal
     * @throws JSONException
     */
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

    /**
     * Method for getting all comments from sign in response
     * @param app jsonObject which contains comments
     * @return list of Comments from response
     * @see Comment
     * @throws JSONException
     */
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

    /**
     * Method for getting all photos url from sign in response
     * @param phts sonObject which contains photos urls
     * @return list of string photos urls
     * @throws JSONException
     */
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



    /**
     * Method to parse data from XML response from getting house id request
     * @param xmlString string with XML response from server
     * @return int id of house from server response
     */
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

    /**
     * Method to parse data from XML response from creating new appeal request
     * @param xmlString
     * @return id of new created appeal or -1 if error appear
     */
    public static int getAppeapIdFromNewAppealRequest(String xmlString){

        JSONObject jsonObj = null;

        try {
           // jsonObj = XML.toJSONObject(xmlString);
            jsonObj = XML.toJSONObject(strPr);

            if(jsonObj.has("Response")) {
                JSONObject jsonObjResponse = jsonObj.getJSONObject("Response");


                if (jsonObjResponse.has("Error")) {

                    return -1;

                } else {
                    if (jsonObjResponse.has("NewAppeal")){

                        jsonObj = null;
                        jsonObj = jsonObjResponse.getJSONObject("NewAppeal");
                        String strId = jsonObj.getString("NewAppealId");
                        return Integer.parseInt(strId);

                    }
                }
            }

        } catch (JSONException e) {
            Log.e("JSON exception", e.getMessage());
            e.printStackTrace();
        }

        return -1;
    }

    public static String strPr = "<Response><NewAppeal><NewAppealId>29391</NewAppealId><Appeals><Appeal id=\"29391\" DistrictId=\"7\" AdressStreet=\"вулиця Лодигіна\" AdressHouse=\"10\" AdressFlat=\"-\" lng=\"30.52\" lat=\"50.45\" AppealType=\"1\" AppealText=\"Test  mobile api   1449411924\" AppealStatus=\"До відома\" RepConf=\"0\" AppealPublic=\"1\" manager=\"\" CallId=\"0\" DateCreate=\"1449411144\" CallCode=\"\" CallZTopic=\"Очікує на обробку\" Color=\"1\"><Photos/><Comments/></Appeal><Appeal id=\"29390\" DistrictId=\"7\" AdressStreet=\"вулиця Лодигіна\" AdressHouse=\"10\" AdressFlat=\"-\" lng=\"30.52\" lat=\"50.45\" AppealType=\"1\" AppealText=\"Test  mobile api   1449411909\" AppealStatus=\"До відома\" RepConf=\"0\" AppealPublic=\"1\" manager=\"\" CallId=\"0\" DateCreate=\"1449411142\" CallCode=\"\" CallZTopic=\"Очікує на обробку\" Color=\"1\"><Photos/><Comments/></Appeal><Appeal id=\"29389\" DistrictId=\"7\" AdressStreet=\"вулиця Лодигіна\" AdressHouse=\"10\" AdressFlat=\"-\" lng=\"30.52\" lat=\"50.45\" AppealType=\"1\" AppealText=\"Test  mobile api   1449411915\" AppealStatus=\"До відома\" RepConf=\"0\" AppealPublic=\"1\" manager=\"\" CallId=\"0\" DateCreate=\"1449411141\" CallCode=\"\" CallZTopic=\"Очікує на обробку\" Color=\"1\"><Photos/><Comments/></Appeal><Appeal id=\"29388\" DistrictId=\"7\" AdressStreet=\"вулиця Лодигіна\" AdressHouse=\"10\" AdressFlat=\"-\" lng=\"30.52\" lat=\"50.45\" AppealType=\"1\" AppealText=\"Test  mobile api   1449139583\" AppealStatus=\"До відома\" RepConf=\"0\" AppealPublic=\"1\" manager=\"\" CallId=\"0\" DateCreate=\"1449137549\" CallCode=\"\" CallZTopic=\"Очікує на обробку\" Color=\"1\"><Photos/><Comments/></Appeal><Appeal id=\"29386\" DistrictId=\"7\" AdressStreet=\"вулиця Лодигіна\" AdressHouse=\"10\" AdressFlat=\"-\" lng=\"30.52\" lat=\"50.45\" AppealType=\"1\" AppealText=\"Test  mobile api   1447079856\" AppealStatus=\"До відома\" RepConf=\"0\" AppealPublic=\"1\" manager=\"\" CallId=\"0\" DateCreate=\"1447078306\" CallCode=\"\" CallZTopic=\"Очікує на обробку\" Color=\"1\"><Photos/><Comments/></Appeal><Appeal id=\"29385\" DistrictId=\"7\" AdressStreet=\"вулиця Лодигіна\" AdressHouse=\"10\" AdressFlat=\"-\" lng=\"30.52\" lat=\"50.45\" AppealType=\"1\" AppealText=\"Test  mobile api   1447001337\" AppealStatus=\"До відома\" RepConf=\"0\" AppealPublic=\"1\" manager=\"\" CallId=\"0\" DateCreate=\"1446999085\" CallCode=\"\" CallZTopic=\"Очікує на обробку\" Color=\"1\"><Photos/><Comments/></Appeal><Appeal id=\"29384\" DistrictId=\"7\" AdressStreet=\"вулиця Лодигіна\" AdressHouse=\"10\" AdressFlat=\"-\" lng=\"30.52\" lat=\"50.45\" AppealType=\"1\" AppealText=\"Test  mobile api   1447001340\" AppealStatus=\"До відома\" RepConf=\"0\" AppealPublic=\"1\" manager=\"\" CallId=\"0\" DateCreate=\"1446999071\" CallCode=\"\" CallZTopic=\"Очікує на обробку\" Color=\"1\"><Photos/><Comments/></Appeal><Appeal id=\"29383\" DistrictId=\"7\" AdressStreet=\"вулиця Лодигіна\" AdressHouse=\"10\" AdressFlat=\"-\" lng=\"30.52\" lat=\"50.45\" AppealType=\"1\" AppealText=\"Test  mobile api   1446385951\" AppealStatus=\"До відома\" RepConf=\"0\" AppealPublic=\"1\" manager=\"\" CallId=\"0\" DateCreate=\"1446383491\" CallCode=\"\" CallZTopic=\"Очікує на обробку\" Color=\"1\"><Photos/><Comments/></Appeal><Appeal id=\"29382\" DistrictId=\"7\" AdressStreet=\"вулиця Лодигіна\" AdressHouse=\"10\" AdressFlat=\"-\" lng=\"30.52\" lat=\"50.45\" AppealType=\"1\" AppealText=\"Test  mobile api   1446385893\" AppealStatus=\"До відома\" RepConf=\"0\" AppealPublic=\"1\" manager=\"\" CallId=\"0\" DateCreate=\"1446383518\" CallCode=\"\" CallZTopic=\"Очікує на обробку\" Color=\"1\"><Photos/><Comments/></Appeal><Appeal id=\"29381\" DistrictId=\"7\" AdressStreet=\"вулиця Лодигіна\" AdressHouse=\"10\" AdressFlat=\"-\" lng=\"30.52\" lat=\"50.45\" AppealType=\"1\" AppealText=\"Test  mobile api   1443285544\" AppealStatus=\"До відома\" RepConf=\"0\" AppealPublic=\"1\" manager=\"\" CallId=\"0\" DateCreate=\"1443283780\" CallCode=\"\" CallZTopic=\"Очікує на обробку\" Color=\"1\"><Photos/><Comments/></Appeal><Appeal id=\"29380\" DistrictId=\"7\" AdressStreet=\"вулиця Лодигіна\" AdressHouse=\"10\" AdressFlat=\"-\" lng=\"30.52\" lat=\"50.45\" AppealType=\"1\" AppealText=\"Test  mobile api   1441493438\" AppealStatus=\"До відома\" RepConf=\"0\" AppealPublic=\"1\" manager=\"\" CallId=\"0\" DateCreate=\"1441490989\" CallCode=\"\" CallZTopic=\"Очікує на обробку\" Color=\"1\"><Photos/><Comments/></Appeal><Appeal id=\"29379\" DistrictId=\"7\" AdressStreet=\"вулиця Лодигіна\" AdressHouse=\"10\" AdressFlat=\"-\" lng=\"30.52\" lat=\"50.45\" AppealType=\"1\" AppealText=\"Test  mobile api   1441493436\" AppealStatus=\"До відома\" RepConf=\"0\" AppealPublic=\"1\" manager=\"\" CallId=\"0\" DateCreate=\"1441490987\" CallCode=\"\" CallZTopic=\"Очікує на обробку\" Color=\"1\"><Photos/><Comments/></Appeal><Appeal id=\"29378\" DistrictId=\"7\" AdressStreet=\"вулиця Лодигіна\" AdressHouse=\"10\" AdressFlat=\"-\" lng=\"30.52\" lat=\"50.45\" AppealType=\"1\" AppealText=\"Test  mobile api   1441487357\" AppealStatus=\"До відома\" RepConf=\"0\" AppealPublic=\"1\" manager=\"\" CallId=\"0\" DateCreate=\"1441487380\" CallCode=\"\" CallZTopic=\"Очікує на обробку\" Color=\"1\"><Photos/><Comments/></Appeal><Appeal id=\"29377\" DistrictId=\"7\" AdressStreet=\"вулиця Лодигіна\" AdressHouse=\"10\" AdressFlat=\"-\" lng=\"30.52\" lat=\"50.45\" AppealType=\"1\" AppealText=\"Test  mobile api   1441480201\" AppealStatus=\"До відома\" RepConf=\"0\" AppealPublic=\"1\" manager=\"\" CallId=\"0\" DateCreate=\"1441480141\" CallCode=\"\" CallZTopic=\"Очікує на обробку\" Color=\"1\"><Photos/><Comments/></Appeal><Appeal id=\"29376\" DistrictId=\"7\" AdressStreet=\"вулиця Лодигіна\" AdressHouse=\"10\" AdressFlat=\"-\" lng=\"30.52\" lat=\"50.45\" AppealType=\"1\" AppealText=\"Test  mobile api   1441466858\" AppealStatus=\"До відома\" RepConf=\"0\" AppealPublic=\"1\" manager=\"\" CallId=\"0\" DateCreate=\"1441465746\" CallCode=\"\" CallZTopic=\"Очікує на обробку\" Color=\"1\"><Photos/><Comments/></Appeal><Appeal id=\"29375\" DistrictId=\"7\" AdressStreet=\"вулиця Лодигіна\" AdressHouse=\"10\" AdressFlat=\"-\" lng=\"30.52\" lat=\"50.45\" AppealType=\"1\" AppealText=\"Test  mobile api   1441466688\" AppealStatus=\"До відома\" RepConf=\"0\" AppealPublic=\"1\" manager=\"\" CallId=\"0\" DateCreate=\"1441465741\" CallCode=\"\" CallZTopic=\"Очікує на обробку\" Color=\"1\"><Photos/><Comments/></Appeal><Appeal id=\"29374\" DistrictId=\"7\" AdressStreet=\"вулиця Лодигіна\" AdressHouse=\"10\" AdressFlat=\"-\" lng=\"30.52\" lat=\"50.45\" AppealType=\"1\" AppealText=\"Test  mobile api   1441466647\" AppealStatus=\"До відома\" RepConf=\"0\" AppealPublic=\"1\" manager=\"\" CallId=\"0\" DateCreate=\"1441465747\" CallCode=\"\" CallZTopic=\"Очікує на обробку\" Color=\"1\"><Photos/><Comments/></Appeal><Appeal id=\"29373\" DistrictId=\"7\" AdressStreet=\"вулиця Лодигіна\" AdressHouse=\"10\" AdressFlat=\"-\" lng=\"30.52\" lat=\"50.45\" AppealType=\"1\" AppealText=\"Test  mobile api   1441466581\" AppealStatus=\"До відома\" RepConf=\"0\" AppealPublic=\"1\" manager=\"\" CallId=\"0\" DateCreate=\"1441465748\" CallCode=\"\" CallZTopic=\"Очікує на обробку\" Color=\"1\"><Photos/><Comments/></Appeal><Appeal id=\"29372\" DistrictId=\"7\" AdressStreet=\"вулиця Лодигіна\" AdressHouse=\"10\" AdressFlat=\"-\" lng=\"30.52\" lat=\"50.45\" AppealType=\"1\" AppealText=\"Test  mobile api   1441464663\" AppealStatus=\"До відома\" RepConf=\"0\" AppealPublic=\"1\" manager=\"\" CallId=\"0\" DateCreate=\"1441462169\" CallCode=\"\" CallZTopic=\"Очікує на обробку\" Color=\"1\"><Photos/><Comments/></Appeal><Appeal id=\"29371\" DistrictId=\"7\" AdressStreet=\"вулиця Лодигіна\" AdressHouse=\"10\" AdressFlat=\"-\" lng=\"30.52\" lat=\"50.45\" AppealType=\"1\" AppealText=\"Test  mobile api   1441464512\" AppealStatus=\"До відома\" RepConf=\"0\" AppealPublic=\"1\" manager=\"\" CallId=\"0\" DateCreate=\"1441462192\" CallCode=\"\" CallZTopic=\"Очікує на обробку\" Color=\"1\"><Photos/><Comments/></Appeal><Appeal id=\"29370\" DistrictId=\"7\" AdressStreet=\"вулиця Лодигіна\" AdressHouse=\"10\" AdressFlat=\"-\" lng=\"30.52\" lat=\"50.45\" AppealType=\"1\" AppealText=\"Test  mobile api   1441443676\" AppealStatus=\"До відома\" RepConf=\"0\" AppealPublic=\"1\" manager=\"\" CallId=\"0\" DateCreate=\"1441444198\" CallCode=\"\" CallZTopic=\"Очікує на обробку\" Color=\"1\"><Photos/><Comments/></Appeal><Appeal id=\"29369\" DistrictId=\"2\" AdressStreet=\"вулиця академіка Горбунова\" AdressHouse=\"3\" AdressFlat=\"13\" lng=\"30.682291\" lat=\"50.430569\" AppealType=\"4\" AppealText=\"Test with address\n"+
            "Тестовое обращение с адресом.\" AppealStatus=\"До відома\" RepConf=\"0\" AppealPublic=\"1\" manager=\"\" CallId=\"0\" DateCreate=\"1439989556\" CallCode=\"\" CallZTopic=\"Очікує на обробку\" Color=\"1\"><Photos/><Comments/></Appeal><Appeal id=\"29368\" DistrictId=\"2\" AdressStreet=\"вулиця академіка Горбунова\" AdressHouse=\"3\" AdressFlat=\"13\" lng=\"30.682291\" lat=\"50.430569\" AppealType=\"4\" AppealText=\"Test with address\n"+
            "Тестовое обращение с адресом.\" AppealStatus=\"До відома\" RepConf=\"0\" AppealPublic=\"1\" manager=\"\" CallId=\"0\" DateCreate=\"1439989460\" CallCode=\"\" CallZTopic=\"Очікує на обробку\" Color=\"1\"><Photos/><Comments/></Appeal><Appeal id=\"29367\" DistrictId=\"0\" AdressStreet=\"\" AdressHouse=\"\" AdressFlat=\"\" lng=\"0\" lat=\"0\" AppealType=\"1\" AppealText=\"TEst\" AppealStatus=\"До відома\" RepConf=\"0\" AppealPublic=\"1\" manager=\"\" CallId=\"0\" DateCreate=\"1439985686\" CallCode=\"\" CallZTopic=\"Очікує на обробку\" Color=\"1\"><Photos/><Comments/></Appeal></Appeals></NewAppeal></Response>";



}