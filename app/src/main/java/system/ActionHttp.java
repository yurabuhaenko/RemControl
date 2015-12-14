package system;

/**
 * @author SergPohh
 * class to perform requests by
 * @see system.ServiceServerHandler
 * and get results from
 * @see system.ParserXML
 */
import android.content.Context;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.*;

import container.ContainerSignInResponse;

public class ActionHttp {
    public ActionHttp(){}


    /**
     * making authorization request
     *
     * @param email user email to sign in request
     * @param password user password
     * @param cont activity which perform request context
     * @return parsed server response in
     * @see ContainerSignInResponse
     */
    public static ContainerSignInResponse authorizationRequest(String email, String password, Context cont){


        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("Request", "SignIn"));
        pairs.add(new BasicNameValuePair("AppVersion", "1"));
        pairs.add(new BasicNameValuePair("BranchVersion", "1"));
        pairs.add(new BasicNameValuePair("OS", "Android"));
        pairs.add(new BasicNameValuePair("Email", email));
        pairs.add(new BasicNameValuePair("Password", password));


        ServiceServerHandler service = new ServiceServerHandler();
        String response = service.makeServiceCall(pairs,cont);

        return ParserXML.getEntityFromAutorizationRequest(response);
    }

    /**
     * get house id request
     * @param lang langitude
     * @param lat latitude
     * @param cont activity which perform request context
     * @return house id
     */
    public static int getNearestAddressesByCoordinates(String lang, String lat, Context cont){
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("Request", "NearestAddresses"));
        pairs.add(new BasicNameValuePair("AppVersion", "1"));
        pairs.add(new BasicNameValuePair("BranchVersion", "1"));
        pairs.add(new BasicNameValuePair("OS", "Android"));
        pairs.add(new BasicNameValuePair("Lng", lang));
        pairs.add(new BasicNameValuePair("Lat", lat));
        ServiceServerHandler service = new ServiceServerHandler();
        String response = service.makeServiceCall(pairs,cont);

        return ParserXML.getHouseIdFromGetNearestAddressesByCoordinatesRequest(response);
    }

    /**
     * New appeal sent request
     * @param houseId house id
     * @param lat latitude
     * @param lang langitude
     * @param appealText text of appeal
     * @param cont activity which perform request context
     * @return if sending was success
     */
    public static boolean newAppealSent(int houseId, String lat, String lang, String appealText,Context cont){
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("Request", "NewAppeal"));
        pairs.add(new BasicNameValuePair("AppVersion", "1"));
        pairs.add(new BasicNameValuePair("BranchVersion", "1"));
        pairs.add(new BasicNameValuePair("OS", "Android"));
        pairs.add(new BasicNameValuePair("NeedAddress", "1"));
        pairs.add(new BasicNameValuePair("AdressHouseId", Integer.toString(houseId)));
        pairs.add(new BasicNameValuePair("AdressFlat", ""));
        pairs.add(new BasicNameValuePair("lat", lat));
        pairs.add(new BasicNameValuePair("lng", lang));
        pairs.add(new BasicNameValuePair("AppealType", "2"));
        pairs.add(new BasicNameValuePair("AppealTheme", ""));
        pairs.add(new BasicNameValuePair("AppealText", appealText));
        pairs.add(new BasicNameValuePair("AppealPublic", "1"));

        ServiceServerHandler service = new ServiceServerHandler();
        //String response = service.makeServiceCall(pairs,cont);
        if (ParserXML.getAppeapIdFromNewAppealRequest("qqq") != -1) {  ///////chande arg to response!!!!!!!!!!!
            return true;
        }else{
            return false;
        }

    }


}