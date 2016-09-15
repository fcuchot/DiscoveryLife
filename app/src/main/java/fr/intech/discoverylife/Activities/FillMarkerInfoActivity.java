package fr.intech.discoverylife.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import fr.intech.discoverylife.R;

public class FillMarkerInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_marker_info);
        Bundle bundle = getIntent().getExtras();
//        private static final String TAG = "MyActivity";
//        Log.i(TAG,)
        if(bundle.getString("Lat")!= null)
        {
  EditText lat = (EditText) findViewById(R.id.Lat);
           lat.setText(bundle.getString("Lat"));
        }

        final EditText name = (EditText) findViewById(R.id.personName);

        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Write Preferences
                SharedPreferences.Editor editor = PreferenceManager
                        .getDefaultSharedPreferences(getApplicationContext())
                        .edit();
                editor.putString("Name", name.getText().toString());
                //Save async
                editor.apply();
                //Launch intent with some data inside a Bundle
                //Intent i = new Intent(MainActivity.this, DetailsActivity.class);
                Intent i = new Intent(FillMarkerInfoActivity.this, MapsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", "First App");
                i.putExtras(bundle);
                startActivity(i);
            }
        });

    }

}
