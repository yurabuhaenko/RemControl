package system;

/**
 * Created by Denver on 06.09.2015.
 */
import org.apache.http.NameValuePair;

import java.util.*;

public class ActionHttp {
    public ActionHttp(){}

    public String baseRequest(List<NameValuePair> params){
        ServiceServerHandler service = new ServiceServerHandler();
        String response = service.makeServiceCall(params);

        return response;
    }








}