package system;

/**
 * Created by Denver on 06.09.2015.
 */

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class ServiceServerHandler {

    static String response = null;
    private final String URL = "http://1551.gov.ua/mobile/";

    public ServiceServerHandler() {

    }



    public String makeServiceCall(List<NameValuePair> params){
        return this.makeServiceCall(params, null);
    }


    public String makeServiceCall(List<NameValuePair> params, String apiKey) {
        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            // Checking http request method type

            HttpPost httpPost = new HttpPost(URL);
            // adding post params



            if (params != null) {
                httpPost.setEntity(new UrlEncodedFormEntity(params));
            }

            httpResponse = httpClient.execute(httpPost);

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
}