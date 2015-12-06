package system;

/**
 * Created by Denver on 06.09.2015.
 */

import android.content.Context;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.CookieStore;
import java.util.List;

import container.ContainerSignInResponse;

public class ServiceServerHandler {

    static String response = null;
    private final String URL = "http://1551.gov.ua/mobile/";

    public ServiceServerHandler() {

    }



    public String makeServiceCall(List<NameValuePair> params, Context cont){

        try {
            RemControlApplication remControlApplication = (RemControlApplication)cont.getApplicationContext();
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            // Checking http request method type

            HttpPost httpPost = new HttpPost(URL);
            // adding post params

           // httpClient.getCookieStore().

            if (params != null) {
                httpPost.setEntity(new UrlEncodedFormEntity(params));
            }
            if(remControlApplication.cookie != null && !remControlApplication.cookie.equals("")) {
                //String str = remControlApplication.cookiejar.get(0).toString();
                httpPost.addHeader("Cookie", remControlApplication.cookie);
            }else {
                String cookie = "PHPSESSID=" + "4cfudqn0b3q8mkab9cca3k0ro3" + ";";
                remControlApplication.cookie = cookie;
                httpPost.addHeader("Cookie", cookie);
            }

            httpResponse = httpClient.execute(httpPost);

            Header[] httpHead = httpResponse.getAllHeaders();

            for(int i = 0; i < httpHead.length; ++i) {
                if(httpHead[i].getName().equals("Set-Cookie")){
                    remControlApplication.cookie = httpHead[i].getValue();
                    break;
                }
            }
            //remControlApplication.cookiejar = cookiejar;


            httpEntity = httpResponse.getEntity();


            response = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;

    }

    public static HttpEntity makeServiceCallForEntity(String url){
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpget = new HttpPost(url);

        HttpResponse response;
        try {
            response = httpclient.execute(httpget);
            if (response.getStatusLine().getStatusCode() == 200) {
                // Get hold of the response entity
                return response.getEntity();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }

}