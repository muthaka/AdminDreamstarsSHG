package dreamstars.muthaka.com.admindreamstarsshg;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Peter Muthaka on 8/24/2015.
 */
public class Members extends ListActivity {



    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> membersList;


    // url to get all members list
    private static String url_all_contributions = "http://petsamod.site40.net/dreamstartsSHG/Admin/get_all_member.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MEMBERS = "registration";
    private static final String TAG_RID = "rid";
    private static final String TAG_NAME = "name";
    private static final String TAG_ID = "id";
    private static final String TAG_PHONE = "phone";

    // members JSONArray
    JSONArray members = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.members);


        // Hashmap for ListView
        membersList = new ArrayList<HashMap<String, String>>();

        // Loading members in Background Thread
        new LoadAllContribution().execute();

        // Get listview
        ListView lv = getListView();

        // on seleting single product
        // launching Edit Product Screen
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String rid = ((TextView) view.findViewById(R.id.rid)).getText()
                        .toString();

                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        EditMembers.class);
                // sending pid to next activity
                in.putExtra(TAG_RID, rid);

                // starting new activity and expecting some response back
                startActivityForResult(in, 100);
            }
        });


    }

    /**
     * Background Async Task to Load all members by making HTTP Request
     * */
    class LoadAllContribution extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Members.this);
            pDialog.setMessage("Loading Members. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All members from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_contributions, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Members: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // contribution found
                    // Getting Array of contribution
                    members = json.getJSONArray(TAG_MEMBERS);

                    // looping through All members
                    for (int i = 0; i < members.length(); i++) {
                        JSONObject c = members.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_RID);
                        String name = c.getString(TAG_NAME);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_RID, id);
                        map.put(TAG_NAME, name);

                        // adding HashList to ArrayList
                        membersList.add(map);
                    }
                } else {
                    // no members found
                    // Launch Start Activity
                    Intent i = new Intent(getApplicationContext(),
                            Start.class);
                    // Closing all previous activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
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
            // dismiss the dialog after getting all members
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            Members.this, membersList,
                            R.layout.list_members, new String[] { TAG_RID,
                            TAG_NAME},
                            new int[] { R.id.rid, R.id.name });
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }

    }
}
