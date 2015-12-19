package denver.remcontrol.activitys;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
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


/**
 * @author yurabuhaenko
 *
 * Main application activity. Here created appeal, added photo. Oportunity to send it on server or save to postponed
 *extends
 * @see NavigationDrawerActivity
 */
public class AppealActivity extends NavigationDrawerActivity {

    /**
     * container for current appeal
     */
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


    /**
     * default on create method, executes when activity and view created. Initializing vars and views
     * chk if no register user - redirect to
     * @link LoginActivity
     * @param savedInstanceState
     */
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

    /**
     * default on start method, executes before activity stars.
     * initializing  location service
     */
    @Override
    public void onStart(){
        super.onStart();
        getLocation = new GetLocation(this);
    }

    /**
     * number of current photo (1 to 5) in which new photo will be setted
     */
    private int curPhotoNumb = 0;


    /**
     * curPhotoNumb sets & dispatchTakePictureIntent() executes
     * if photos number == 5 error shows
     */
    public void onClickButtonAddPhotoFromCamera(){
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



    /**
     * curPhotoNumb sets & dispatchGetPhotoFromGalleryIntent() executes
     * if photos number == 5 error shows
     */
    public void onClickButtonAddFromGalery(){
        if(appeal.getPhoto1() == null) {
            curPhotoNumb = 1;
            dispatchGetPhotoFromGalleryIntent();
        }else{if(appeal.getPhoto2() == null) {
            curPhotoNumb = 2;
            dispatchGetPhotoFromGalleryIntent();
        }else{if(appeal.getPhoto3() == null) {
            curPhotoNumb = 3;
            dispatchGetPhotoFromGalleryIntent();
        }else{if(appeal.getPhoto4() == null) {
            curPhotoNumb = 4;
            dispatchGetPhotoFromGalleryIntent();
        }else{if(appeal.getPhoto5() == null) {
            curPhotoNumb = 5;
            dispatchGetPhotoFromGalleryIntent();
        }else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Максимальна доступно 5 фото", Toast.LENGTH_LONG);
            toast.show();
        }
        }}}}
    }


    /**
     * On click for add photo button.
     * Make dialog with button:
     *  - from camera photo executes onClickButtonAddPhotoFromCamera();
     *  - from gallery photo executes onClickButtonAddFromGalery();
     *  - cancel
     *  @see AlertDialog
     */

    public void onClickButtonAddPhoto(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("")
                .setItems(getResources().getStringArray(R.array.array_buttons_set_photos), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            onClickButtonAddPhotoFromCamera();
                        }else{
                            onClickButtonAddFromGalery();
                        }
                    }
                }).setNegativeButton("Відміна", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        builder.create();
        builder.setCancelable(true);
        builder.show();

    }


    /**
     * Make intent to default gallery app
     * take path to photo, sent it on intent of onResultActivity
     * onResultActivity starts (default gallery application)
     */

    private void dispatchGetPhotoFromGalleryIntent(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }


    /**
     * default metod, executes when on result activity closed and send result
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data!=null) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                mCurrentPhotoPath = getPath(this, selectedImageUri);
                setPic(curPhotoNumb);
            }
            if (requestCode == REQUEST_TAKE_PHOTO) {
                setPic(curPhotoNumb);
            }
        }
    }

    static final int SELECT_PICTURE = 2;
    static final int REQUEST_TAKE_PHOTO = 1;


    /**
     * take empty image file, sent it on intent of onResultActivity
     * onResultActivity starts (default camera application)
     */
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




    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }




    /**
     * creating empty image on default file storage and current time as unique name
     * set on
     * @return empty image file
     * @throws IOException
     */
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

        return image;
    }


    /**
     * set photo path on appeal, set photo on view by number
     * resize photo bitmap to required size
     * @param curPhotoNumb
     */
    private void setPic(int curPhotoNumb) {
        appeal.addPhotoByNumber(curPhotoNumb, mCurrentPhotoPath);
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

    /**
     * @param curPhotoNumb
     * @return photo view element by current number of photo
     */

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


    /**
     *
     * @param curPhotoNumb
     * @return floating action button view element by current number of photo
     */
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


    /**
     *
     * @param curPhotoNumb
     * @return int id of photo view  element
     */
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


    /**
     * on click method. Find for which photo it was executed & executes deletePhotoFromMemory() for this photo
     * @param view
     */

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


    /**
     * deleting photo from memory & appeal, hides layout of image
     * @param photoNumber
     */

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


    /**
     * on click metod to execute sending appeal into server
     * check if length of appeal text more than 20 symbols & is coordinates
     * check if internet connection available
     * @param view
     */

    public void onClickSent(View view){
        getLocation();
        if (editText.getText().toString().length() > 20) {
            if (appeal.getLat() != "0" && appeal.getLon() != "0") {

                if (InternetConnectionChecker.isNetworkConnected(this)) {

                    showProgress(true);
                    SentAppeal sentAppeal = new SentAppeal(appeal.getLat(), appeal.getLon(),
                            editText.getText().toString(), AppealActivity.this);
                    sentAppeal.execute((Void) null);

                }else {
                    Toast.makeText(this, "Помилка! \n Відсутнє з’єднання з інтернетом. Ви можете " +
                            "зберегти звернення у відкладені", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(this, "Помилка! \n Неможливо визначити координати", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "Помилка! \n Поле зі зверненням порожнє або менше 20 символів", Toast.LENGTH_LONG).show();
        }
    }


    /**
     *
     * get coordinates & set it to appeal
     */

    public void getLocation(){
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

    /**
     * on click method.
     * Check is text lenght more than 20 symbols or one or more photos available. Toast error
     * save appeal to
     * @see RemControlApplication
     * and save into Shared Preferences
     * @param view
     */
    public void onClickSaveToPostponed(View view){
        getLocation();
        if (editText.getText().toString().length() > 20 || appeal.isPhotos()) {
            if (appeal.getLat() != "0" && appeal.getLon() != "0") {

                appeal.setText(editText.getText().toString());
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
            } else {
                Toast.makeText(this, "Помилка! \n Неможливо визначити координати", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "Помилка! \n Для збереження потрібен текст більше 20 символів або наявне фото!", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * show or hide progress bar
     * @param show if true - progress bar shown, activity layout hides; if false - progress bar hides, activity layout shown;
     */
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




    /**
     * @author SergPohh
     * asynchronic class to sent appeal on server
     */

    public class SentAppeal extends AsyncTask<Void, Void, Boolean> {

        int houseId = -1;
        final String lat;
        final String lang;
        final String appealText;
        final Context cont;


        /**
         * Constructor with params of appeal which will be saved
         * @param lat
         * @param lang
         * @param appealText
         * @param cont
         */
        SentAppeal(String lat, String lang, String appealText, Context cont) {
            this.lat = lat;
            this.lang = lang;
            this.appealText = appealText;
            this.cont = cont;
        }


        /**
         * At first get house id by coordinates
         * At second sent appeal to server
         * @param params
         * @return true if sending was success
         */

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


        /**
         * If sending was successful make toast with success massage, clean activities stack and start new AppealActivity
         * If sending was fail make toast with error massage
         * @param success result of doInBackground()
         */
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
