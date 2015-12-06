package system;

/**
 * Created by Denver on 06.09.2015.
 */
import android.content.Context;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.*;

import container.ContainerSignInResponse;

public class ActionHttp {
    public ActionHttp(){}

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
        String response = service.makeServiceCall(pairs,cont);

        return true;
    }


}