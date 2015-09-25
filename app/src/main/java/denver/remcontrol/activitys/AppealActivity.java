package denver.remcontrol.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import system.PostponedAppeals;
import denver.remcontrol.R;
import system.ActionHttp;
import system.DataBaseAction;
import system.GetLocation;
import system.InternetConnectionChecker;
import system.RemControlApplication;


public class AppealActivity extends NavigationDrawerActivity {


    private PostponedAppeals appeal;

    ImageView mImageView1;
    ImageView mImageView2;
    ImageView mImageView3;
    ImageView mImageView4;
    ImageView mImageView5;
    EditText editText;
    GetLocation getLocation;
    String mCurrentPhotoPath;
    String mCurrentPhotoPathFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appeal);
        postCreate(savedInstanceState,R.layout.activity_appeal);

        RemControlApplication remControlApplication = (RemControlApplication)getApplicationContext();
       /* if(!remControlApplication.checkIsSavedUser()){
            Intent intent = new Intent(AppealActivity.this, LoginActivity.class);
            startActivity(intent);
        }*/



        appeal = new PostponedAppeals(0, Integer.toString(0), Integer.toString(0), "", null,null,null,null,null);
        mImageView1 = (ImageView)findViewById(R.id.imageViewPhoto1);
        mImageView2 = (ImageView)findViewById(R.id.imageViewPhoto2);
        mImageView3 = (ImageView)findViewById(R.id.imageViewPhoto3);
        mImageView4 = (ImageView)findViewById(R.id.imageViewPhoto4);
        mImageView5 = (ImageView)findViewById(R.id.imageViewPhoto5);
        editText = (EditText)findViewById(R.id.editTextEnterAppealText);
    }

    @Override
    public void onStart(){
        super.onStart();
        getLocation = new GetLocation(this);
    }


    private int curPhotoNumb = 0;

    public void onClickButtonAddPhoto(View view){
        if(appeal.getPhoto1() == null) {
            curPhotoNumb = 1;
            dispatchTakePictureIntent();
        }else{if(appeal.getPhoto2() == null) {
            curPhotoNumb = 2;
            dispatchTakePictureIntent();
        }else{if(appeal.getPhoto3() == null) {
            curPhotoNumb = 3;
            dispatchTakePictureIntent();
        }else{if(appeal.getPhoto4() == null) {
            curPhotoNumb = 4;
            dispatchTakePictureIntent();
        }else{if(appeal.getPhoto5() == null) {
            curPhotoNumb = 5;
            dispatchTakePictureIntent();
        }else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Максимальна доступно 5 фото", Toast.LENGTH_LONG);
            toast.show();
        }
        }}}}


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        setPic(curPhotoNumb);

    }

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {


        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Errror", Toast.LENGTH_SHORT);
                toast.show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }

    }



    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPathFile = "file:" + image.getAbsolutePath();
        mCurrentPhotoPath = image.getAbsolutePath();
        setImageByCurrentNumber(curPhotoNumb, mCurrentPhotoPath);
        return image;
    }

    private void setPic(int curPhotoNumb) {
        ImageView mImageView = getViewByCurrentNumber(curPhotoNumb);
        mImageView.getLayoutParams().width = (int) getResources().getDimension(R.dimen.imageview_width);

        int targetW = mImageView.getLayoutParams().width;
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImageView.setImageBitmap(bitmap);
    }

    private ImageView getViewByCurrentNumber(int curPhotoNumb){
        switch (curPhotoNumb){
            case 1:
                return mImageView1;
           // break;
            case 2:
                return mImageView2;
            //break;
            case 3:
                return mImageView3;
            //break;
            case 4:
                return mImageView4;
            //break;
            case 5:
                return mImageView5;
           // break;
        }
        return mImageView1;
    }

    private void setImageByCurrentNumber(int curPhotoNumb, String mPhotoPath){
        switch (curPhotoNumb){
            case 1:
                appeal.setPhoto1(mPhotoPath);
                break;
            case 2:
                appeal.setPhoto2(mPhotoPath);
                break;
            case 3:
                appeal.setPhoto3(mPhotoPath);
                break;
            case 4:
                appeal.setPhoto4(mPhotoPath);
                break;
            case 5:
                appeal.setPhoto5(mPhotoPath);
                break;
        }
    }









    public void onClickSent(View view){
        appeal.setCOMENT(editText.getText().toString());
        DataBaseAction dataBaseAction = new DataBaseAction(AppealActivity.this);

        if(appeal.getCoordinateX()!= "0" && appeal.getCoordinateY()!="0"){
            dataBaseAction.add(appeal);

            if(InternetConnectionChecker.isNetworkConnected(this)){



                new Thread(new Runnable() {
                    //Thread to stop network calls on the UI thread
                    public void run() {

                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("Request","SignIn"));
                            params.add(new BasicNameValuePair("AppVersion","1.0"));
                            params.add(new BasicNameValuePair("BranchVersion","4"));
                            params.add(new BasicNameValuePair("OS","android"));
                            params.add(new BasicNameValuePair("Email","geluxilo@inboxdesign.me"));
                            params.add(new BasicNameValuePair("Password","00000000"));
                            ActionHttp actionHttp = new ActionHttp();
                            String response = actionHttp.baseRequest(params);

                            if (response!=null) {
                                Log.e("Tag", "Could not get HTML: " + response);
                            }

                            params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("Request","GetAllAppeals"));
                            params.add(new BasicNameValuePair("AppVersion","1.0"));
                            params.add(new BasicNameValuePair("BranchVersion","4"));
                            params.add(new BasicNameValuePair("OS","android"));
                            actionHttp = new ActionHttp();
                            response = actionHttp.baseRequest(params);

                            if (response!=null) {
                                Log.e("Tag", "Could not get HTML: " + response);
                            }
                    }
                }).start();

/*
                Intent newIntent = new Intent(AppealActivity.this, AppealActivity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);*/

            }else{
                Toast.makeText(this, "Відсутнє з'єднання з інтернетом. Ваше звернення буде збережено в відкладені", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "Запустіть локацію перед збереженням", Toast.LENGTH_LONG).show();
        }
    }

    public void onClickGetLocation(View view){
        //getLocation = new GetLocation(this);

            if (getLocation.IsAvailable()){
                String latitude = Double.toString(getLocation.GetLatitude());
                String longitude = Double.toString(getLocation.GetLongitude());

                appeal.setCoordinateX(latitude);
                appeal.setCoordinateY(longitude);
                Toast.makeText(this, "X:" + latitude + " Y:" + longitude, Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, "Сервіс локації недоступний", Toast.LENGTH_LONG).show();
            }

    }

    public void onClickSaveToPostponed(View view){
        DataBaseAction dataBaseAction = new DataBaseAction(AppealActivity.this);
        appeal.setCOMENT(editText.getText().toString());
        if(appeal.getCoordinateX()!= "0" && appeal.getCoordinateY()!="0"){
            dataBaseAction.add(appeal);
            Intent newIntent = new Intent(AppealActivity.this, AppealActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);
        }else{
            Toast.makeText(this, "Запустіть локацію перед збереженням", Toast.LENGTH_LONG).show();
        }

    }



}
