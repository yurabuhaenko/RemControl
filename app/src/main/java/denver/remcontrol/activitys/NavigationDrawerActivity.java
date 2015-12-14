package denver.remcontrol.activitys;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import denver.remcontrol.R;
import system.RemControlApplication;


/**
 * @author yurabuhaenko
 * Activity which must be extended by other activity. Add navigation bar to child activity
 */
public class NavigationDrawerActivity extends AppCompatActivity {

    /**
     * default on create method
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
    }

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBar actionBar;
    TextView textView;
    FrameLayout frameLayout;
    RelativeLayout fullLayout;
    NavigationView navigationView;

    RemControlApplication remControlApplication;

    TextView textViewUserName;
    TextView textViewUserEmail;

    /**
     * Metod which must be executed on child activity
     * @param savedInstanceState
     * @param ID id of full child activity layout
     * initialize action bar view elements
     */
    public void postCreate(Bundle savedInstanceState, int ID) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        remControlApplication = (RemControlApplication)getApplicationContext();
        textViewUserName = (TextView) findViewById(R.id.text_view_header_user_name);
        textViewUserEmail = (TextView) findViewById(R.id.text_view_header_user_email);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ViewGroup inclusionViewGroup = (ViewGroup)findViewById(R.id.inclusionlayout);
        View child1 = LayoutInflater.from(this).inflate(
                ID, null);
        inclusionViewGroup.addView(child1);

        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        //setHeaderUser();
        //setMenuForUser();


        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);
        }

        setupNavigationDrawerContent(navigationView);


        if(!remControlApplication.checkIsSavedUser()){
           Intent intent = new Intent(NavigationDrawerActivity.this, LoginActivity.class);
        }

    }


    /**
     * default on resume method
     * execute methods to show action bar for user specific
     */
    @Override
    protected void onResume() {
        super.onResume();

        setMenuForUser();
        setHeaderUser();
    }


    /**
     * sets user name and mail on action bar header or massage of need authorization
     */
    public void setHeaderUser(){

        if (remControlApplication.checkIsSavedUser()){
            textViewUserName.setText(remControlApplication.getUser().getName());
            textViewUserEmail.setText(remControlApplication.getUser().getEmail());
        }
        else{
            textViewUserName.setText(R.string.need_authorization_error);
            textViewUserEmail.setText("");
        }
    }


    /**
     * set items on action bar according to user (authorized or guest)
     */
    public void setMenuForUser(){
        if(remControlApplication.checkIsSavedUser()){
            navigationView.getMenu().setGroupVisible(R.id.group_base_functional_menu, true);
            navigationView.getMenu().setGroupVisible(R.id.group_login, false);
            navigationView.getMenu().setGroupVisible(R.id.group_app_menu, true);
            return;

        }else{
            navigationView.getMenu().setGroupVisible(R.id.group_base_functional_menu, false);
            navigationView.getMenu().setGroupVisible(R.id.group_login, true);
            navigationView.getMenu().setGroupVisible(R.id.group_app_menu, true);
            return;
        }

    }


    /**
     * default activity method to create buttons on toolbar
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * set home button on toolbar to open navigation bar
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * method to perform user log out. Execute alert dialog, if positive:
     * dell user from
     * @see RemControlApplication
     * Shared Preferences
     * Set new menu for user
     */

    public void onClickSignOut(){
        AlertDialog.Builder ad;
        Context context;
        context = NavigationDrawerActivity.this;
        ad = new AlertDialog.Builder(context);
        ad.setTitle("Sign Out");
        ad.setMessage("You want to sign out?");
        ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                remControlApplication.deleteUserFromSaved();
                remControlApplication.deleteUser();
                setMenuForUser();
                setHeaderUser();
            }
        });
        ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                return;
            }
        });

        ad.setCancelable(true);
        ad.show();
    }


    /**
     * initialize on click for navigation bar items listeners
     * perform actions for each menu item
     * @param navigationView
     */

    private void setupNavigationDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // textView = (TextView) findViewById(R.id.textView);
                        Intent intent;
                        switch (menuItem.getItemId()) {

                            case R.id.item_navigation_drawer_create_appeal:
                                intent = new Intent(NavigationDrawerActivity.this, AppealActivity.class);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                startActivity(intent);
                                return true;

                            case R.id.item_navigation_drawer_postponed:

                                intent = new Intent(NavigationDrawerActivity.this, PostponedAppealsListActivity.class);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                startActivity(intent);

                                return true;

                            case R.id.item_navigation_drawer_history:
                                intent = new Intent(NavigationDrawerActivity.this, AppealSentHistoryActivity.class);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                startActivity(intent);

                                return true;

                            case R.id.item_navigation_drawer_settings:

                                drawerLayout.closeDrawer(GravityCompat.START);
                                onClickSignOut();

                                return true;

                            case R.id.item_navigation_drawer_about_project:
                                intent = new Intent(NavigationDrawerActivity.this, AboutProject.class);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                startActivity(intent);

                                return true;



                            case R.id.item_navigation_drawer_login:
                                intent = new Intent(NavigationDrawerActivity.this, LoginActivity.class);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                startActivity(intent);
                                return true;


                        }
                        return true;
                    }
                });
    }
}