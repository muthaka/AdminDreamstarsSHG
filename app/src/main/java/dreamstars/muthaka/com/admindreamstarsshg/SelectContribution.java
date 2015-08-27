package dreamstars.muthaka.com.admindreamstarsshg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Peter Muthaka on 8/24/2015.
 */
public class SelectContribution extends Activity {

    Button add,view;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectcontribution);

        add = (Button) findViewById(R.id.add_contrib);
        view = (Button) findViewById(R.id.view_contrib);

        // add contribution click event
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Launching Add Contribution Activity
                Intent i = new Intent(getApplicationContext(), AddContribution.class);
                startActivity(i);

            }
        });

        // view contribution click event
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Launching View Contribution Activity
                Intent i = new Intent(getApplicationContext(), Contribution.class);
                startActivity(i);

            }
        });

    }
}
