package services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import container.ContainerSignInResponse;
import system.ActionHttp;
import system.InternetConnectionChecker;
import system.RemControlApplication;

/**
 * @author SergPohh
 * service when application started and send request to get sesion from server
 */
public class AuthorizationIntentService extends IntentService {



    public AuthorizationIntentService() {
        super("AuthorizationIntentService");
    }

    RemControlApplication remControlApplication;

    /**
     * default executed method
     * sent auth request if logined user
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        remControlApplication = (RemControlApplication) getApplicationContext();

        if(InternetConnectionChecker.isNetworkConnected(this)) {
            if (remControlApplication.checkIsSavedUser()) {
                ActionHttp sh = new ActionHttp();
                ContainerSignInResponse signInResponse = sh.authorizationRequest(remControlApplication.getUser().getEmail(),
                        remControlApplication.getUser().getPassword(), this);

                if (!signInResponse.isError) {

                    remControlApplication.setAppealSents(signInResponse.appealList);
                    return;
                }

            }
        }

    }


}
