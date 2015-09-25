package system;


import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
/**
 * Created by Denver on 06.09.2015.
 */
public class GetLocation  implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    protected Context context;
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLocation;
    protected boolean isAvailable = false;

    public GetLocation(Context _context){
        context = _context;
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public double GetLatitude() {if (mLocation == null) return -1; else return mLocation.getLatitude();}

    public double GetLongitude() {if (mLocation == null) return -1; else return mLocation.getLongitude();}

    public boolean IsAvailable() {return isAvailable;}

    @Override
    public void onConnected(Bundle bundle) {
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLocation == null) {
            Log.i("get-location", "Connection failed");
        }
        else isAvailable = true;
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(context, "Connection suspended", Toast.LENGTH_LONG).show();
        Log.i("get-location", "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(context, "Connection failed", Toast.LENGTH_LONG).show();
        Log.i("get-location", "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
        mGoogleApiClient.connect();
    }
}
