package denver.remcontrol.activitys;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

import container.ContainerAppealSent;
import denver.remcontrol.R;

public class ShowSentAppealActivity extends NavigationDrawerActivity {

    ContainerAppealSent appealSent;

    TextView textViewDistrict;
    TextView textViewAddress;
    TextView textViewText;
    TextView textViewStatus;
    LinearLayout linearLayoutStatusInActivityShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_sent_appeal);
        postCreate(savedInstanceState,R.layout.activity_show_sent_appeal );

        Bundle bn = new Bundle();
        Intent intent = getIntent();
        int pos = intent.getIntExtra("position", 0);

        appealSent = remControlApplication.getAppealSents().get(pos);

        textViewDistrict = (TextView) findViewById(R.id.textViewDistrictInActivityShow);
        textViewAddress = (TextView) findViewById(R.id.textViewAddressInActivityShow);
        textViewText = (TextView) findViewById(R.id.textViewTextInActivityShow);
        textViewStatus = (TextView) findViewById(R.id.textViewStatusInActivityShow);
        linearLayoutStatusInActivityShow = (LinearLayout) findViewById(R.id.linearLayoutStatusInActivityShow);

        textViewDistrict.setText(appealSent.getDistrict());
        textViewAddress.setText(appealSent.getAddress());
        textViewText.setText(appealSent.getAppealText());
        textViewStatus.setText(appealSent.getStatus());



        switch (appealSent.getStatusCode()){
            case 1:
                linearLayoutStatusInActivityShow.setBackgroundColor(getResources().getColor(R.color.md_deep_orange_300));
            case 2:
                linearLayoutStatusInActivityShow.setBackgroundColor(getResources().getColor(R.color.md_yellow_400));
            case 3:
                linearLayoutStatusInActivityShow.setBackgroundColor(getResources().getColor(R.color.md_light_green_A200));
        }

    }



}
