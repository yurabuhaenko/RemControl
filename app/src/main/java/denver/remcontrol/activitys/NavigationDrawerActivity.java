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


public class NavigationDrawerActivity extends AppCompatActivity {

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
        setHeaderUser();
        setMenuForUser();


        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);
        }

        setupNavigationDrawerContent(navigationView);


        if(!remControlApplication.checkIsSavedUser()){
           Intent intent = new Intent(NavigationDrawerActivity.this, LoginActivity.class);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setMenuForUser();
        setHeaderUser();
    }



    public void setHeaderUser(){
        textViewUserName.setText("Лукьяненко Геннадий");
        textViewUserEmail.setText("geluxilo@inboxdesign.me");
        /*if (remControlApplication.checkIsSavedUser() == true){
            textViewUserName.setText(remControlApplication.getUser().getName());
            textViewUserEmail.setText(remControlApplication.getUser().getEmail());
        }
        else{
            textViewUserName.setText(R.string.need_authorization_error);
            textViewUserEmail.setText("");
        }*/
    }




    public void setMenuForUser(){
        if(remControlApplication.checkIsSavedUser() == false){
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public void setMenuItemChecked(int id){
        for(int i = 0; i < navigationView.getMenu().size(); ++i) {
            if(navigationView.getMenu().getItem(i).getItemId()==id){
                navigationView.getMenu().getItem(i).setChecked(true);
            }else{
                navigationView.getMenu().getItem(i).setChecked(false);
            }
        }
    }


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

                                drawerLayout.closeDrawer(GravityCompat.START);


                                return true;
                            case R.id.item_navigation_drawer_history:
                                intent = new Intent(NavigationDrawerActivity.this, AppealSentHistoryActivity.class);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                startActivity(intent);

                                return true;

                            case R.id.item_navigation_drawer_settings:

                                drawerLayout.closeDrawer(GravityCompat.START);


                                return true;

                            case R.id.item_navigation_drawer_about_project:

                                drawerLayout.closeDrawer(GravityCompat.START);


                                return true;

                            case R.id.item_navigation_drawer_help:

                                drawerLayout.closeDrawer(GravityCompat.START);


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