package services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import container.ContainerSignInResponse;
import system.ActionHttp;
import system.RemControlApplication;


public class AuthorizationIntentService extends IntentService {



    public AuthorizationIntentService() {
        super("AuthorizationIntentService");
    }

    RemControlApplication remControlApplication;


    @Override
    protected void onHandleIntent(Intent intent) {
        remControlApplication = (RemControlApplication) getApplicationContext();

        if (remControlApplication.checkIsSavedUser()) {
            ActionHttp sh = new ActionHttp();
            ContainerSignInResponse signInResponse = sh.authorizationRequest(remControlApplication.getUser().getEmail(),
                    remControlApplication.getUser().getPassword(), this);

            if(!signInResponse.isError){

                remControlApplication.setAppealSents(signInResponse.appealList);
                return;
            }

        }

    }


}
