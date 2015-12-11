package denver.remcontrol.activitys;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import container.Appeal;
import container.PostponedAppeal;
import services.AuthorizationIntentService;
import denver.remcontrol.R;
import system.ActionHttp;
import system.GetLocation;
import system.InternetConnectionChecker;
import system.RemControlApplication;


public class AppealActivity extends NavigationDrawerActivity {


    private PostponedAppeal appeal;

    ImageView mImageView1;
    ImageView mImageView2;
    ImageView mImageView3;
    ImageView mImageView4;
    ImageView mImageView5;

    FloatingActionButton fabDelPhoto1;
    FloatingActionButton fabDelPhoto2;
    FloatingActionButton fabDelPhoto3;
    FloatingActionButton fabDelPhoto4;
    FloatingActionButton fabDelPhoto5;




    EditText editText;
    GetLocation getLocation;

    String mCurrentPhotoPath;
    String mCurrentPhotoPathFile;


    private View mProgressView;
    private View mFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appeal);
        postCreate(savedInstanceState,R.layout.activity_appeal);

        RemControlApplication remControlApplication = (RemControlApplication)getApplicationContext();
        if(!remControlApplication.checkIsSavedUser()){
            Intent intent = new Intent(AppealActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        Intent intent = new Intent(AppealActivity.this,AuthorizationIntentService.class);
        startService(intent);


        mFormView = findViewById(R.id.new_appeal_form);
        mProgressView = findViewById(R.id.new_appeal_progress);

        fabDelPhoto1 = (FloatingActionButton)findViewById(R.id.myFABImageViewPhoto1);
        fabDelPhoto2 = (FloatingActionButton)findViewById(R.id.myFABImageViewPhoto2);
        fabDelPhoto3 = (FloatingActionButton)findViewById(R.id.myFABImageViewPhoto3);
        fabDelPhoto4 = (FloatingActionButton)findViewById(R.id.myFABImageViewPhoto4);
        fabDelPhoto5 = (FloatingActionButton)findViewById(R.id.myFABImageViewPhoto5);


        appeal = new PostponedAppeal();
        mImageView1 = (ImageView)findViewById(R.id.imageViewPhoto1);
        mImageView2 = (ImageView)findViewById(R.id.imageViewPhoto2);
        mImageView3 = (ImageView)findViewById(R.id.imageViewPhoto3);
        mImageView4 = (ImageView)findViewById(R.id.imageViewPhoto4);
        mImageView5 = (ImageView)findViewById(R.id.imageViewPhoto5);
        editText = (EditText)findViewById(R.id.editTextEnterAppealText);

        getSupportActionBar().setTitle("Нове звернення");
    }

    private void setOnLongClickListenersOnImageToDelete(ImageView view){

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder ad;
                Context context;
                context = AppealActivity.this;
                ad = new AlertDialog.Builder(context);
                ad.setTitle("Delete selected photo");
                ad.setMessage("You want to delete this photos?");
                ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {

                    }
                });
                ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        return;
                    }
                });

                ad.setCancelable(true);
                ad.show();
                return false;
            }
        });

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

        appeal.addPhotoByNumber(curPhotoNumb, mCurrentPhotoPath);
        return image;
    }

    private void setPic(int curPhotoNumb) {
        RelativeLayout rel = (RelativeLayout)findViewById(getIdPhotoLayoutByNumb(curPhotoNumb));
        rel.setVisibility(View.VISIBLE);
        rel.getLayoutParams().height = (int) getResources().getDimension(R.dimen.imageview_height);
        rel.getLayoutParams().width = (int) getResources().getDimension(R.dimen.imageview_width);

        ImageView mImageView = getViewByCurrentNumber(curPhotoNumb);
       // FloatingActionButton fab = getFABByCurrentNumber(curPhotoNumb);
        //fab.setVisibility(View.VISIBLE);

        mImageView.getLayoutParams().width = (int) getResources().getDimension(R.dimen.imageview_width);
        mImageView.getLayoutParams().height = (int) getResources().getDimension(R.dimen.imageview_height);

        int targetW = mImageView.getLayoutParams().width;
        int targetH = mImageView.getLayoutParams().height;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

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

    private ImageView getFirstFreeImageView(){
        if(appeal.getPhoto1() == ""){return mImageView1;}
        if(appeal.getPhoto2() == ""){return mImageView2;}
        if(appeal.getPhoto3() == ""){return mImageView3;}
        if(appeal.getPhoto4() == ""){return mImageView4;}
        if(appeal.getPhoto5() == ""){return mImageView5;}
        return null;
    }

    private FloatingActionButton getFABByCurrentNumber(int curPhotoNumb){
        switch (curPhotoNumb){
            case 1:
                return fabDelPhoto1;
            // break;
            case 2:
                return fabDelPhoto2;
            //break;
            case 3:
                return fabDelPhoto3;
            //break;
            case 4:
                return fabDelPhoto4;
            //break;
            case 5:
                return fabDelPhoto5;
            // break;
        }
        return fabDelPhoto1;
    }

    private int getIdPhotoLayoutByNumb(int curPhotoNumb){
        switch (curPhotoNumb){
            case 1:
                return R.id.photoLayout1;
            // break;
            case 2:
                return R.id.photoLayout2;
            //break;
            case 3:
                return R.id.photoLayout3;
            //break;
            case 4:
                return R.id.photoLayout4;
            //break;
            case 5:
                return R.id.photoLayout5;
            // break;
        }
        return R.id.photoLayout1;
    }





    public void onClickDeletePhoto(View view){
        switch (view.getId()){
            case R.id.myFABImageViewPhoto1:
                fabDelPhoto1 = null;
                deletePhotoFromMemory(1);
            break;
            case R.id.myFABImageViewPhoto2:
                fabDelPhoto2 = null;
                deletePhotoFromMemory(2);
            break;
            case R.id.myFABImageViewPhoto3:
                fabDelPhoto3 = null;
                deletePhotoFromMemory(3);
            break;
            case R.id.myFABImageViewPhoto4:
                fabDelPhoto4 = null;
                deletePhotoFromMemory(4);
            break;
            case R.id.myFABImageViewPhoto5:
                fabDelPhoto5 = null;
                deletePhotoFromMemory(5);
            break;
        }

    }

    private void deletePhotoFromMemory(int photoNumber){

        RelativeLayout rel = (RelativeLayout)findViewById(getIdPhotoLayoutByNumb(photoNumber));
        rel.setVisibility(View.GONE);
        rel.getLayoutParams().height = 0;
        rel.getLayoutParams().width = 0;

        ImageView mImageView = getViewByCurrentNumber(photoNumber);
        FloatingActionButton fab = getFABByCurrentNumber(photoNumber);
        //fab.setVisibility(View.GONE);

        mImageView.getLayoutParams().height = 0;
        mImageView.getLayoutParams().width = 0;

        String dir = getFilesDir().getAbsolutePath();

        File f0 = new File(dir, appeal.getPhotoByNumber(photoNumber));
        boolean d0 = f0.delete();
        appeal.addPhotoByNumber(photoNumber, null);

    }




    public void onClickSent(View view){
        if (editText.getText().toString().length() < 20) {
            if (appeal.getLat() != "0" && appeal.getLon() != "0") {

                if (InternetConnectionChecker.isNetworkConnected(this)) {

                    showProgress(true);
                    SentAppeal sentAppeal = new SentAppeal(appeal.getLat(), appeal.getLon(),
                            editText.getText().toString(), AppealActivity.this);
                    sentAppeal.execute((Void) null);

                }
            } else {
                Toast.makeText(this, "Помилка! \n Неможливо визначити координати", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "Помилка! \n Поле зі зверненням порожнє або менше 20 символів", Toast.LENGTH_LONG).show();
        }
    }




    public void onClickGetLocation(View view){
        //getLocation = new GetLocation(this);

            if (getLocation.IsAvailable()){
                String latitude = Double.toString(getLocation.GetLatitude());
                String longitude = Double.toString(getLocation.GetLongitude());

                appeal.setLat(latitude);
                appeal.setLon(longitude);
                Toast.makeText(this, "X:" + latitude + " Y:" + longitude, Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, "Сервіс локації недоступний", Toast.LENGTH_LONG).show();
            }

    }

    public void onClickSaveToPostponed(View view){
        appeal.setText(editText.getText().toString());
        if(appeal.getText() != "") {

            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            appeal.setDatetime(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date()));

            remControlApplication.getPostponedAppeals().add(appeal);
            remControlApplication.savePostponedAppeals();

            Toast toast = Toast.makeText(getApplicationContext(),
                    "Збережено успішно!", Toast.LENGTH_SHORT);
            toast.show();
            Intent newIntent = new Intent(AppealActivity.this, AppealActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);
        }
    }



    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }



    public class SentAppeal extends AsyncTask<Void, Void, Boolean> {

        int houseId = -1;
        final String lat;
        final String lang;
        final String appealText;
        final Context cont;



        SentAppeal(String lat, String lang, String appealText, Context cont) {
            this.lat = lat;
            this.lang = lang;
            this.appealText = appealText;
            this.cont = cont;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            houseId  = ActionHttp.getNearestAddressesByCoordinates(appeal.getLat(), appeal.getLon(),
                    AppealActivity.this);

            if (houseId  == -1 &&  houseId == 0) {
                return false;
            }else{
                return ActionHttp.newAppealSent(houseId, lat, lang, appealText, cont);
            }

        }


        @Override
        protected void onPostExecute ( final Boolean success){
           // mAuthTask = null;
           showProgress(false);

            if (success) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Відправлено успішно!", Toast.LENGTH_SHORT);
                toast.show();
                Intent newIntent = new Intent(AppealActivity.this, AppealActivity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Виникла помилка, спробуйте ще раз або збережіть звернення локально", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }




}
