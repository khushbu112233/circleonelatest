package com.circle8.circleOne.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.GroupAdapter;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.GroupModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.circle8.circleOne.Utils.Utility.POST2;
import static com.circle8.circleOne.Utils.Utility.convertInputStreamToString;

public class GroupTag extends AppCompatActivity
{
    private TextView textView;
    private ImageView imgCards, imgConnect, imgEvents, imgProfile, imgLogo;
    private int actionBarHeight;
    ListView lstGroupTag;
    TextView txtGroup;
    public static List<GroupModel> allTaggs;
    private GroupAdapter customAdapter;
    ImageView imgAdd;
    String GroupName, GroupDesc;

    private LoginSession session;
    private String profile_id ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_tag);

        final ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);
        getSupportActionBar().setShowHideAnimationEnabled(false);
        lstGroupTag = (ListView) findViewById(R.id.lstGroupTag);
        txtGroup = (TextView) findViewById(R.id.txtGroup);
        textView = (TextView) findViewById(R.id.mytext);
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        imgCards = (ImageView) findViewById(R.id.imgCards);
        imgConnect = (ImageView) findViewById(R.id.imgConnect);
        imgEvents = (ImageView) findViewById(R.id.imgEvents);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        imgAdd = (ImageView) findViewById(R.id.imgAdd);
        textView.setText("Group Tag");
        ImageView drawer = (ImageView) findViewById(R.id.drawer);
        drawer.setVisibility(View.GONE);
        imgLogo.setImageResource(R.drawable.ic_keyboard_arrow_left_black_24dp);
        allTaggs = new ArrayList<>();
        //cardCount = db.getActiveNFCCount();
        new HttpAsyncTaskGroup().execute(Utility.BASE_URL+"Group/Fetch");

        session = new LoginSession(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        profile_id = user.get(LoginSession.KEY_PROFILEID);

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater factory = LayoutInflater.from(GroupTag.this);

                LinearLayout layout = new LinearLayout(GroupTag.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText titleBox = new EditText(GroupTag.this);
                titleBox.setHint("Group Name");
                layout.addView(titleBox);

                final EditText descriptionBox = new EditText(GroupTag.this);
                descriptionBox.setHint("Description");
                layout.addView(descriptionBox);

                //   dialog.setView(layout);


//text_entry is an Layout XML file containing two text field to display in alert dialog
                final AlertDialog.Builder alert = new AlertDialog.Builder(GroupTag.this);
                alert.setTitle("Create Group").setView(layout).setPositiveButton("Create",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                GroupName = titleBox.getText().toString();
                                GroupDesc = descriptionBox.getText().toString();

                                if (GroupName.equals("")){
                                    Toast.makeText(getApplicationContext(), "Enter circle name", Toast.LENGTH_LONG).show();
                                }
                                else if (GroupDesc.equals("")){
                                    Toast.makeText(getApplicationContext(), "Enter circle description", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    new HttpAsyncTaskGroupCreate().execute(Utility.BASE_URL+"Group/Create");
                                }
                            }
                        }).setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
     /*
     * User clicked cancel so do some stuff
     */
                                dialog.cancel();

                            }
                        });
                alert.show();
            }
        });

        lstGroupTag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                LayoutInflater factory = LayoutInflater.from(GroupTag.this);

                LinearLayout layout = new LinearLayout(GroupTag.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText titleBox = new EditText(GroupTag.this);
                titleBox.setHint("Group Name");
                layout.addView(titleBox);

                final EditText descriptionBox = new EditText(GroupTag.this);
                descriptionBox.setHint("Description");
                layout.addView(descriptionBox);

             //   dialog.setView(layout);


//text_entry is an Layout XML file containing two text field to display in alert dialog
                final AlertDialog.Builder alert = new AlertDialog.Builder(GroupTag.this);
                alert.setTitle("Add Group").setView(layout).setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

//                                Toast.makeText(getApplicationContext(), allTaggs.get(position).getGroup_Name(), Toast.LENGTH_LONG).show();

                              //  Log.i("AlertDialog", "TextEntry 1 Entered " + input1.getText().toString());
                              //  Log.i("AlertDialog", "TextEntry 2 Entered " + input2.getText().toString());
    /* User clicked OK so do some stuff */
                            }
                        }).setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
     /*
     * User clicked cancel so do some stuff
     */
                                dialog.cancel();

                            }
                        });
                alert.show();
            }
        });

        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), CardsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imgCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(getApplicationContext(), CardsActivity.class);

                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("viewpager_position", 0);

                startActivity(go);
                finish();
            }
        });

        imgConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(getApplicationContext(), CardsActivity.class);

                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("viewpager_position", 1);

                startActivity(go);
                finish();
            }
        });

        imgEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(getApplicationContext(), CardsActivity.class);

                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("viewpager_position", 2);

                startActivity(go);
                finish();
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(getApplicationContext(), CardsActivity.class);

                // you pass the position you want the viewpager to show in the extra,
                // please don't forget to define and initialize the position variable
                // properly
                go.putExtra("viewpager_position", 3);

                startActivity(go);
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        Utility.freeMemory();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utility.freeMemory();
    }


    private class HttpAsyncTaskGroupCreate extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(GroupTag.this);
            dialog.setMessage("Creating group...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("GroupDesc", GroupDesc);
                jsonObject.accumulate("GroupName", GroupName);
                jsonObject.accumulate("ProfileId", profile_id);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return POST2(urls[0],jsonObject);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    String Success = jsonObject.getString("Success").toString();
                    if (Success.equals("1")) {
                        Toast.makeText(getApplicationContext(), "Circle created..", Toast.LENGTH_LONG).show();
                        allTaggs.clear();
                        new HttpAsyncTaskGroup().execute(Utility.BASE_URL+"Group/Fetch");
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Circle not created..", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Not able to create circle..", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private class HttpAsyncTaskGroup extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(GroupTag.this);
            dialog.setMessage("Fetching groups...");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
            //  nfcModel = new ArrayList<>();
            //   allTags = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("ProfileId", profile_id);
                jsonObject.accumulate("numofrecords", "10");
                jsonObject.accumulate("pageno", "1");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return POST2(urls[0],jsonObject);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("Groups");
                    //Toast.makeText(getContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();

                    if (jsonArray.length() == 0) {
                        lstGroupTag.setVisibility(View.GONE);
                        txtGroup.setVisibility(View.VISIBLE);
                    } else {
                        lstGroupTag.setVisibility(View.VISIBLE);
                        txtGroup.setVisibility(View.GONE);
                    }

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();

                        GroupModel nfcModelTag = new GroupModel();
                        nfcModelTag.setGroup_ID(object.getString("group_ID"));
                        nfcModelTag.setGroup_Name(object.getString("group_Name"));
                        //  Toast.makeText(getContext(), object.getString("Testimonial_Text"), Toast.LENGTH_LONG).show();
                        allTaggs.add(nfcModelTag);
                    }
                    customAdapter = new GroupAdapter(GroupTag.this, allTaggs);
                    lstGroupTag.setAdapter(customAdapter);
                    // new ArrayAdapter<>(getApplicationContext(),R.layout.mytextview, array)
                } else {
                    Toast.makeText(getApplicationContext(), "Not able to load circles..", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}