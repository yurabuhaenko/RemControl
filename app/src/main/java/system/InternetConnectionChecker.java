package system;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author SergPohh
 * class to check if internet cnnection available
 */
public class InternetConnectionChecker {
    /**
     * check if internet cnnection available
     * @param context context of activity which perform checking
     * @return true if internet cnnection available, false when is not
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            return false;
        } else
            return true;
    }
}
