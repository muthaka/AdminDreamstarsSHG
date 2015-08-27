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
public class AddContribution extends Activity {
    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText inputName;
    EditText inputAmount;
    EditText inputFine;
    EditText inputDate;

    // url to create new Contribution
    private static String url_create_product = "http://petsamod.site40.net/dreamstartsSHG/Admin/create_contribution.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contribution);

        // Edit Text
        inputName = (EditText) findViewById(R.id.inputName);
        inputAmount = (EditText) findViewById(R.id.inputAmount);
        inputFine = (EditText) findViewById(R.id.inputFine);
        inputDate = (EditText) findViewById(R.id.inputDate);

        // Create button
        Button btnCreateContribution = (Button) findViewById(R.id.btnadd_contribution);

        // button click event
        btnCreateContribution.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new Contribution in background thread
                new CreateNewContribution().execute();
            }
        });
    }

    /**
     * Background Async Task to Create new Contribution
     * */
    class CreateNewContribution extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddContribution.this);
            pDialog.setMessage("Creating Contribution..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating Contribution
         * */
        protected String doInBackground(String... args) {
            String name = inputName.getText().toString();
            String amount = inputAmount.getText().toString();
            String fine = inputFine.getText().toString();
            String ddate = inputDate.getText().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("amount", amount));
            params.add(new BasicNameValuePair("fine", fine));
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
                    // successfully created Contribution
                    Intent i = new Intent(getApplicationContext(), Contribution.class);
                    startActivity(i);

                    // closing this screen
                    finish();
                } else {
                    // failed to create Contribution
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
