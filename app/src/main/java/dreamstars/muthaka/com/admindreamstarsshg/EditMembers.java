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
public class EditMembers extends Activity {

    EditText txtName;
    EditText txtId;
    EditText txtPhone;
    Button btnSave;
    Button btnDelete;

    String rid;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    // single member url
    private static final String url_member_detials = "http://petsamod.site40.net/dreamstartsSHG/Admin/get_members.php";

    // url to update member
    private static final String url_update_member = "http://petsamod.site40.net/dreamstartsSHG/Admin/update_member.php";

    // url to delete member
    private static final String url_delete_member = "http://petsamod.site40.net/dreamstartsSHG/Admin/delete_member.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MEMBER = "registration";
    private static final String TAG_RID = "rid";
    private static final String TAG_NAME = "name";
    private static final String TAG_ID = "id";
    private static final String TAG_PHONE = "phone";

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_members);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // save button
        btnSave = (Button) findViewById(R.id.btnSave_member);
        btnDelete = (Button) findViewById(R.id.btnDelete_member);

        // getting member details from intent
        Intent i = getIntent();

        // getting member id (rid) from intent
        rid = i.getStringExtra(TAG_RID);

        // Getting complete member details in background thread
        new GetMemberDetails().execute();

        // save button click event
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // starting background task to update member
                new SaveMemberDetails().execute();
            }
        });

        // Delete button click event
        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // deleting member in background thread
                new DeleteMember().execute();
            }
        });
    }

    /**
     * Background Async Task to Get complete member details
     * */
    class GetMemberDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditMembers.this);
            pDialog.setMessage("Loading Members details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting Member details in background thread
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
                        params.add(new BasicNameValuePair("rid", rid));

                        // getting member details by making HTTP request
                        // Note that member details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_member_detials, "GET", params);

                        // check your log for json response
                        Log.d("Single Product Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received member details
                            JSONArray productObj = json
                                    .getJSONArray(TAG_MEMBER); // JSON Array

                            // get first member object from JSON Array
                            JSONObject product = productObj.getJSONObject(0);

                            // member with this rid found
                            // Edit Text
                            txtName = (EditText) findViewById(R.id.inputName);
                            txtId = (EditText) findViewById(R.id.inputId);
                            txtPhone = (EditText) findViewById(R.id.inputPhone);

                            // display member data in EditText
                            txtName.setText(product.getString(TAG_NAME));
                            txtId.setText(product.getString(TAG_ID));
                            txtPhone.setText(product.getString(TAG_PHONE));

                        }else{
                            // Member with rid not found
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
     * Background Async Task to  Save Member Details
     * */
    class SaveMemberDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditMembers.this);
            pDialog.setMessage("Saving product ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Saving Member
         * */
        protected String doInBackground(String... args) {

            // getting updated data from EditTexts
            String name = txtName.getText().toString();
            String id = txtId.getText().toString();
            String phone = txtPhone.getText().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_RID, rid));
            params.add(new BasicNameValuePair(TAG_NAME, name));
            params.add(new BasicNameValuePair(TAG_ID, id));
            params.add(new BasicNameValuePair(TAG_PHONE, phone));

            // sending modified data through http request
            // Notice that update member url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_member,
                    "POST", params);

            // check json success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully updated
                    Intent i = getIntent();
                    // send result code 100 to notify about member update
                    setResult(100, i);
                    finish();
                } else {
                    // failed to update member
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
            // dismiss the dialog once member uupdated
            pDialog.dismiss();
        }
    }

    /*****************************************************************
     * Background Async Task to Delete member
     * */
    class DeleteMember extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditMembers.this);
            pDialog.setMessage("Deleting Member...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Deleting member
         * */
        protected String doInBackground(String... args) {

            // Check for success tag
            int success;
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("rid", rid));

                // getting member details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        url_delete_member, "POST", params);

                // check your log for json response
                Log.d("Delete Product", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // member successfully deleted
                    // notify previous activity by sending code 100
                    Intent i = getIntent();
                    // send result code 100 to notify about member deletion
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
            // dismiss the dialog once member deleted
            pDialog.dismiss();

        }

    }
}
