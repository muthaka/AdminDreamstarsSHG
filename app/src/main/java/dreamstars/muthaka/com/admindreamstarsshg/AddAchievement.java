package dreamstars.muthaka.com.admindreamstarsshg;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter Muthaka on 8/24/2015.
 */
public class AddAchievement extends Activity {
    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText inputName;
    EditText inputDate;


    // url to create new product
    private static String url_create_product = "http://petsamod.site40.net/dreamstartsSHG/Admin/create_achievement.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_achievement);

        // Edit Text
        inputName = (EditText) findViewById(R.id.inputName);
        inputDate = (EditText) findViewById(R.id.inputDate);


        // Create button
        Button btnCreateAchievement = (Button) findViewById(R.id.btnadd_achievement);

        // button click event
        btnCreateAchievement.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new Achievement in background thread
                new CreateNewAchievement().execute();
            }
        });


    }

    /**
     * Background Async Task to Create new Achievement
     * */
    class CreateNewAchievement extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddAchievement.this);
            pDialog.setMessage("Creating Achievement..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating Achievement
         * */
        protected String doInBackground(String... args) {
            String name = inputName.getText().toString();
            String ddate = inputDate.getText().toString();


            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("ddate", ddate));


            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_product,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created Achievement
                    Intent i = new Intent(getApplicationContext(), Achievement.class);
                    startActivity(i);

                    // closing this screen
                    finish();
                } else {
                    // failed to create Achievement
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }
}
