package dreamstars.muthaka.com.admindreamstarsshg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Peter Muthaka on 8/24/2015.
 */
public class SelectAchievement extends Activity {

    Button add,view;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectachievement);

        add = (Button) findViewById(R.id.add_achiev);
        view = (Button) findViewById(R.id.start_achiev);

        // Add achievement click event
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Launching Add Achievement Activity
                Intent i = new Intent(getApplicationContext(), AddAchievement.class);
                startActivity(i);

            }
        });

        // View achievement click event
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Launching View Achievement Activity
                Intent i = new Intent(getApplicationContext(), Achievement.class);
                startActivity(i);

            }
        });


    }
}
