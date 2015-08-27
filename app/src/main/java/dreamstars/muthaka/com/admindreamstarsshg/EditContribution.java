package dreamstars.muthaka.com.admindreamstarsshg;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter Muthaka on 8/24/2015.
 */
public class EditContribution extends Activity {

    EditText txtName;
    EditText txtAmount;
    EditText txtFine;
    EditText txtDate;
    Button btnSave;
    Button btnDelete;

    String aid;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    // single contribution url
    private static final String url_contribution_details = "http://petsamod.site40.net/dreamstartsSHG/Admin/get_contribution.php";

    // url to update contribution
    private static final String url_update_contribution = "http://petsamod.site40.net/dreamstartsSHG/Admin/update_contribution.php";

    // url to delete contribution
    private static final String url_delete_contribution = "http://petsamod.site40.net/dreamstartsSHG/Admin/delete_contribution.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "account";
    private static final String TAG_AID = "aid";
    private static final String TAG_NAME = "name";
    private static final String TAG_AMOUNT = "amount";
    private static final String TAG_FINE = "fine";
    private static final String TAG_DATE = "ddate";

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_contribution);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // save button
        btnSave = (Button) findViewById(R.id.btnSave_contribution);
        btnDelete = (Button) findViewById(R.id.btnDelete_contribution);

        // getting contribution from intent
        Intent i = getIntent();

        // getting contribution id (aid) from intent
        aid = i.getStringExtra(TAG_AID);

        // Getting complete contribution details in background thread
        new GetContributionDetails().execute();

        // save button click event
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // starting background task to update contribution
                new SaveContributionDetails().execute();
            }
        });

        // Delete button click event
        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // deleting contribution in background thread
                new DeleteContribution().execute();
            }
        });
    }

    /**
     * Background Async Task to Get complete contribution details
     * */
    class GetContributionDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditContribution.this);
            pDialog.setMessage("Loading contribution details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting contribution details in background thread
         * */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("aid", aid));

                        // getting Contribution details by making HTTP request
                        // Note that Contribution details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_contribution_details, "GET", params);

                        // check your log for json response
                        Log.d("Single Account Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received contribution details
                            JSONArray productObj = json
                                    .getJSONArray(TAG_PRODUCT); // JSON Array

                            // get first contribution object from JSON Array
                            JSONObject product = productObj.getJSONObject(0);

                            // contribution with this aid found
                            // Edit Text
                            txtName = (EditText) findViewById(R.id.inputName);
                            txtAmount = (EditText) findViewById(R.id.inputAmount);
                            txtFine = (EditText) findViewById(R.id.inputFine);
                            txtDate = (EditText) findViewById(R.id.inputDate);

                            // display contribution data in EditText
                            txtName.setText(product.getString(TAG_NAME));
                            txtAmount.setText(product.getString(TAG_AMOUNT));
                            txtFine.setText(product.getString(TAG_FINE));
                            txtDate.setText(product.getString(TAG_DATE));

                        }else{
                            // product with aid not found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
        }
    }

    /**
     * Background Async Task to  Save contribution Details
     * */
    class SaveContributionDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditContribution.this);
            pDialog.setMessage("Saving product ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Saving contribution
         * */
        protected String doInBackground(String... args) {

            // getting updated data from EditTexts
            String name = txtName.getText().toString();
            String amount = txtAmount.getText().toString();
            String fine = txtFine.getText().toString();
            String ddate = txtDate.getText().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_AID, aid));
            params.add(new BasicNameValuePair(TAG_NAME, name));
            params.add(new BasicNameValuePair(TAG_AMOUNT, amount));
            params.add(new BasicNameValuePair(TAG_FINE, fine));
            params.add(new BasicNameValuePair(TAG_DATE, ddate));

            // sending modified data through http request
            // Notice that update Contribution url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_contribution,
                    "POST", params);

            // check json success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully updated
                    Intent i = getIntent();
                    // send result code 100 to notify about contribution update
                    setResult(100, i);
                    finish();
                } else {
                    // failed to update contribution
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
            // dismiss the dialog once Contribution uupdated
            pDialog.dismiss();
        }
    }

    /*****************************************************************
     * Background Async Task to Delete Contribution
     * */
    class DeleteContribution extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditContribution.this);
            pDialog.setMessage("Deleting Contribution...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Deleting contribution
         * */
        protected String doInBackground(String... args) {

            // Check for success tag
            int success;
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("aid", aid));

                // getting Contribution details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        url_delete_contribution, "POST", params);

                // check your log for json response
                Log.d("Delete Contribution", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // contribution successfully deleted
                    // notify previous activity by sending code 100
                    Intent i = getIntent();
                    // send result code 100 to notify about contribution deletion
                    setResult(100, i);
                    finish();
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
            // dismiss the dialog once Contribution deleted
            pDialog.dismiss();

        }

    }
}
