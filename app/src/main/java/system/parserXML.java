package system;


import javax.xml.parsers.*;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xml.sax.InputSource;
import org.w3c.dom.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 21.09.15.
 */


public class parserXML {

   /* public static List<NameValuePair> getEntityFromAutorizationRequest(){

        List<NameValuePair> pairsTasks = new ArrayList<NameValuePair>();
        pairsTasks.add(new BasicNameValuePair("text", mTaskWithUsersSettedList.get(i).getTaskText()));
    }*/




    private static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "?";
    }


}
