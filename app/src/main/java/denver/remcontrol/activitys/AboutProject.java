package denver.remcontrol.activitys;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import denver.remcontrol.R;


/**
 * @author SergPohh
 * Activity which display info about project & developers
 * extends
 * @see NavigationDrawerActivity
 */
public class AboutProject extends NavigationDrawerActivity {


    /**
     * Default on create method where initializing links & email links
     * @param savedInstanceState
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postCreate(savedInstanceState, R.layout.activity_about_project);


        TextView t2 = (TextView) findViewById(R.id.textViewHref1551);
        t2.setMovementMethod(LinkMovementMethod.getInstance());


        TextView feedback = (TextView) findViewById(R.id.textViewEmailYur);
        feedback.setText(Html.fromHtml("<a href=\"mailto:yura.buhaenko@gmail.com\">yura.buhaenko@gmail.com</a>"));
        feedback.setMovementMethod(LinkMovementMethod.getInstance());

        TextView feedback2 = (TextView) findViewById(R.id.textViewEmailPoh);
        feedback2.setText(Html.fromHtml("<a href=\"mailto:Psadeas170894@gmail.com\">Psadeas170894@gmail.com</a>"));
        feedback2.setMovementMethod(LinkMovementMethod.getInstance());

    }


}
