package com.circle8.circleOne.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle8.circleOne.Adapter.SubscriptionAdapter;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.SubscriptionModel;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.exception.AuthenticationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.circle8.circleOne.Utils.Utility.POST2;

public class SubscriptionActivity extends AppCompatActivity
{
    private ListView listView ;
    private RelativeLayout  rlListView ;
    private ArrayList<SubscriptionModel> subscriptionModelArrayList ;
    private SubscriptionAdapter subscriptionAdapter ;
    Stripe stripe;
    Card card;
    Token tok;
    String strToken;
    AlertDialog alertDialog;
    ImageView imgBack, ivAlphaImg ;
    private RelativeLayout rlProgressDialog ;
    private TextView tvProgressing ;
    private ImageView ivConnecting1, ivConnecting2, ivConnecting3 ;
    int amount;
    private String PackageName, SubscriptionID;
    String UserId = "";
    private LoginSession session;
    int width, height;
    String numberOnCard, nameOnCard, exYearOnCard, exMonthOnCard, cvvOnCard, mobileNoOnCard ;
    String email;
    String PlanId = "", final_packageID = "";
    String default_PackageId = "";
    String key = "";
    public static String Package_Name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        WindowManager manager = (WindowManager) getSystemService(Activity.WINDOW_SERVICE);
        width = manager.getDefaultDisplay().getWidth();
        height = manager.getDefaultDisplay().getHeight();

        listView = (ListView)findViewById(R.id.listView);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        ivAlphaImg = (ImageView) findViewById(R.id.ivAlphaImg);
        subscriptionModelArrayList = new ArrayList<>();
        session = new LoginSession(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        // name
        UserId = user.get(LoginSession.KEY_USERID);
        email = user.get(LoginSession.KEY_EMAIL);
        rlProgressDialog = (RelativeLayout)findViewById(R.id.rlProgressDialog);
        tvProgressing = (TextView)findViewById(R.id.txtProgressing);
        ivConnecting1 = (ImageView)findViewById(R.id.imgConnecting1) ;
        ivConnecting2 = (ImageView)findViewById(R.id.imgConnecting2) ;
        ivConnecting3 = (ImageView)findViewById(R.id.imgConnecting3) ;

        rlListView = (RelativeLayout)findViewById(R.id.rlListView);

//        tvPay = (TextView)findViewById(R.id.tvPay);
//        tvCancel = (TextView)findViewById(R.id.tvCancel);

        new HttpAsyncTaskGetUserSubscription().execute(Utility.BASE_URL+"Subscription/GetUserSubscription");
        //   new HttpAsyncTask().execute(Utility.BASE_URL+"Subscription/GetPackageList");

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String package_Name = subscriptionModelArrayList.get(position).getPackageName();
                String packageId = subscriptionModelArrayList.get(position).getPackageID();
                String contacts_limit = subscriptionModelArrayList.get(position).getConnectionLimit();
                String groups_limit = subscriptionModelArrayList.get(position).getGroupLimit();
                String month_connect_limit = subscriptionModelArrayList.get(position).getMonthlyConnectionLimit();
                String left_connection = subscriptionModelArrayList.get(position).getLetf_connection();
                // String amountt = subscriptionModelArrayList.get(position).getPrice();
                PackageName = subscriptionModelArrayList.get(position).getPackageName();
                //  SubscriptionID = subscriptionModelArrayList.get(position).getPackageID();
                PlanId = subscriptionModelArrayList.get(position).getPackage_plan_id();

                if (packageId.equalsIgnoreCase(default_PackageId))
                {
                    Toast.makeText(getApplicationContext(), "You cannot cancel Basic package", Toast.LENGTH_LONG).show();
                }
                else if (packageId.equalsIgnoreCase("1"))
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(SubscriptionActivity.this, R.style.Blue_AlertDialog);
                    alert.setMessage("Do you want to Cancel your previous Subscription?");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do your work here.
                            dialog.dismiss();
                            new HttpAsyncTaskCancelSub().execute("http://circle8.asia/Checkout/Cancelsubscription");
                        }
                    });
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.show();

                }else if (!packageId.equalsIgnoreCase(default_PackageId)) {
                    if (default_PackageId.equals("1")) {
                        //key = "firstpay";
                        ivAlphaImg.setVisibility(View.VISIBLE);

                        int price = Integer.parseInt(subscriptionModelArrayList.get(position).getPrice());
                        amount = price * 100;

                        try {
                            stripe = new Stripe("pk_live_d0uXEesOC2Qg5919ul4t7Ocl");
                        } catch (AuthenticationException e) {
                            e.printStackTrace();
                        }

                        alertDialog = new AlertDialog.Builder(SubscriptionActivity.this).create();
                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View dialogView = inflater.inflate(R.layout.stripe_payment_screen, null);

                        LinearLayout llCardValues = (LinearLayout) dialogView.findViewById(R.id.llCardValues);
                        LinearLayout llPackageDetails = (LinearLayout) dialogView.findViewById(R.id.llPackageDetails);

                        TextView tvPackageName = (TextView) dialogView.findViewById(R.id.tvPackageName);
                        TextView tvConnect_Group = (TextView) dialogView.findViewById(R.id.tvContact_Group);
                        TextView tvConnection = (TextView) dialogView.findViewById(R.id.tvConnection);
                        TextView tvAmount = (TextView) dialogView.findViewById(R.id.tvAmount);
                        TextView tvLeftConnection = (TextView) dialogView.findViewById(R.id.tvLeftConnection);
                        RelativeLayout rlLeftConnection = (RelativeLayout) dialogView.findViewById(R.id.rlLeftConnection);
                        ImageView ivVisa = (ImageView) dialogView.findViewById(R.id.ivVisa);
                        ImageView ivMasterCard = (ImageView) dialogView.findViewById(R.id.ivMasterCard);
                        ImageView ivAmex = (ImageView) dialogView.findViewById(R.id.ivAmex);
                        final EditText etCardNumber = (EditText) dialogView.findViewById(R.id.etCardNumber);
                        final EditText etCardHolderName = (EditText) dialogView.findViewById(R.id.etCardHolderName);
                        final EditText etExMonth = (EditText) dialogView.findViewById(R.id.etExMonth);
                        final EditText etExYear = (EditText) dialogView.findViewById(R.id.etExYear);
                        final EditText etSecurityCode = (EditText) dialogView.findViewById(R.id.etSecurityCode);
                        final EditText etMobileNumber = (EditText) dialogView.findViewById(R.id.etMobileNumber);
                        TextView tvPay = (TextView) dialogView.findViewById(R.id.tvPay);
                        TextView tvCancel = (TextView) dialogView.findViewById(R.id.tvCancel);

                        tvPackageName.setText(package_Name);
                        tvConnect_Group.setText(contacts_limit + " contacts, up to " + groups_limit + " circles,");
                        tvConnection.setText("up to " + month_connect_limit + " connections per month.");
                        tvAmount.setText("SGD $" + price);


                        tvPay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                numberOnCard = etCardNumber.getText().toString();
                                nameOnCard = etCardHolderName.getText().toString();
                                exYearOnCard = etExYear.getText().toString();
                                exMonthOnCard = etExMonth.getText().toString();
                                cvvOnCard = etSecurityCode.getText().toString();
                                mobileNoOnCard = etMobileNumber.getText().toString();

                                if (numberOnCard.isEmpty()) {
                                    Toast.makeText(SubscriptionActivity.this, "Enter Card No.", Toast.LENGTH_SHORT).show();
                                } else if (nameOnCard.isEmpty()) {
                                    Toast.makeText(SubscriptionActivity.this, "Enter Holder Name", Toast.LENGTH_SHORT).show();
                                } else if (exMonthOnCard.isEmpty()) {
                                    Toast.makeText(SubscriptionActivity.this, "Enter Expiry Month", Toast.LENGTH_SHORT).show();
                                } else if (exYearOnCard.isEmpty()) {
                                    Toast.makeText(SubscriptionActivity.this, "Enter Expiry Year", Toast.LENGTH_SHORT).show();
                                } else if (cvvOnCard.isEmpty()) {
                                    Toast.makeText(SubscriptionActivity.this, "Enter CVV No.", Toast.LENGTH_SHORT).show();
                                } else if (mobileNoOnCard.isEmpty()) {
                                    Toast.makeText(SubscriptionActivity.this, "Enter Mobile No.", Toast.LENGTH_SHORT).show();
                                } else {
                                    cardPayment();
                                    alertDialog.dismiss();
                                    ivAlphaImg.setVisibility(View.GONE);
                                }
                            }
                        });

                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                                ivAlphaImg.setVisibility(View.GONE);
                            }
                        });

                        alertDialog.setView(dialogView);
                        alertDialog.setCancelable(false);
                        alertDialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);
//                alertDialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//                alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                        alertDialog.show();
                        Window window = alertDialog.getWindow();
                        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    }
                    else {
                        new HttpAsyncTaskUpdateSub().execute("http://circle8.asia/Checkout/Updatesubscription");
                    }
                }
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

    public void cardPayment()
    {
        card = new Card
                (
                        numberOnCard,
                        Integer.valueOf(exMonthOnCard),
                        Integer.valueOf(exYearOnCard),
                        cvvOnCard
                );

        card.setCurrency("sgd");
        card.setName(nameOnCard);

        stripe.createToken(card, "pk_live_d0uXEesOC2Qg5919ul4t7Ocl", new TokenCallback() {
            public void onSuccess(Token token) {
                // TODO: Send Token information to your backend to initiate a charge
                Toast.makeText(getApplicationContext(), "Token created: " + token.getId(), Toast.LENGTH_LONG).show();
                tok = token;
                strToken = token.getId();
                //  new StripeCharge(token.getId()).execute();
                new HttpAsyncTokenTask().execute("http://circle8.asia/Checkout/PaywithSubscription");
                alertDialog.cancel();
                ivAlphaImg.setVisibility(View.GONE);
            }

            public void onError(Exception error) {
                Log.d("Stripe", error.getLocalizedMessage());
                Toast.makeText(getApplicationContext(), error.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }


    public void submitCard(View view)
    {
        // TODO: replace with your own test key

        card = new Card
                (
                        numberOnCard,
                        Integer.valueOf(exMonthOnCard),
                        Integer.valueOf(exYearOnCard),
                        cvvOnCard
                );

        card.setCurrency("sgd");
//        card.setName(cardHolderName.getText().toString());
        card.setName(nameOnCard);


        stripe.createToken(card, "pk_live_d0uXEesOC2Qg5919ul4t7Ocl", new TokenCallback() {
            public void onSuccess(Token token) {
                // TODO: Send Token information to your backend to initiate a charge
                Toast.makeText(getApplicationContext(), "Token created: " + token.getId(), Toast.LENGTH_LONG).show();
                tok = token;
                strToken = token.getId();
                //  new StripeCharge(token.getId()).execute();
                new HttpAsyncTokenTask().execute("http://circle8.asia/Checkout/PaywithSubscription");
                alertDialog.cancel();
            }

            public void onError(Exception error) {
                Log.d("Stripe", error.getLocalizedMessage());
                Toast.makeText(getApplicationContext(), error.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private class HttpAsyncTaskGetUserSubscription extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
             String loading = "Subscriptions" ;
            CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("UserId", UserId );
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return POST2(urls[0],jsonObject);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            rlProgressDialog.setVisibility(View.GONE);

            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    String success = jsonObject.getString("success");
                    JSONObject jsonArray = jsonObject.getJSONObject("UserSubscription");
//                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

                    if (success.equals("1")) {
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();
                        new HttpAsyncTask().execute(Utility.BASE_URL+"Subscription/GetPackageList");
                        // SubscriptionModel subscriptionModel = new SubscriptionModel();
                        default_PackageId = (jsonArray.getString("PackageID"));
                               SubscriptionID = (jsonArray.getString("Stripe_SubscriptionID"));
                        // subscriptionModelArrayList.add(subscriptionModel);

                        String PackageID = jsonArray.getString("PackageID");
                        Package_Name = jsonArray.getString("Package_Name");
                        String Package_Desc = jsonArray.getString("Package_Desc");
                        String Price = jsonArray.getString("Price");
                        String Package_Type = jsonArray.getString("Package_Type");
                        String Connection_Limit = jsonArray.getString("Connection_Limit");
                        String Group_Limit = jsonArray.getString("Group_Limit");
                        String Monthy_Connection_Limit = jsonArray.getString("Monthy_Connection_Limit");
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Not able to load Subscription..", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Not able to load Subscription..", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private class HttpAsyncTaskCancelSub extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
                 String loading = "Cancelling subscription" ;
            CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("Email", email );
                jsonObject.accumulate("subscriptionId", SubscriptionID );
                jsonObject.accumulate("Userid", Integer.parseInt(UserId) );

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return POST2(urls[0],jsonObject);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            rlProgressDialog.setVisibility(View.GONE);

            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    String message = jsonObject.getString("message");
                    if (Status.equals("Failed")) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "Previous Subscription cancelled Successfully..", Toast.LENGTH_LONG).show();
                        new HttpAsyncTask().execute(Utility.BASE_URL+"Subscription/GetPackageList");
                        new HttpAsyncTaskGetUserSubscription().execute(Utility.BASE_URL+"Subscription/GetUserSubscription");

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Not able to load Subscription..", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class HttpAsyncTaskUpdateSub extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
              String loading = "Updating subscription" ;
            CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("PlanId",PlanId);
                jsonObject.accumulate("subscriptionId",SubscriptionID);
                jsonObject.accumulate("Userid", Integer.parseInt(UserId) );

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return POST2(urls[0],jsonObject);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            rlProgressDialog.setVisibility(View.GONE);

            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    String message = jsonObject.getString("message");
                    if (Status.equals("Failed"))
                    {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        new HttpAsyncTask().execute(Utility.BASE_URL+"Subscription/GetPackageList");
                        new HttpAsyncTaskGetUserSubscription().execute(Utility.BASE_URL+"Subscription/GetUserSubscription");

                        Toast.makeText(getApplicationContext(), "Previous Subscription updated Successfully..", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Not able to load Subscription..", Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String loading = "Subscriptions" ;
            CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("UserId", UserId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return POST2(urls[0],jsonObject);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            rlProgressDialog.setVisibility(View.GONE);

            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("SubscriptionPackage_List");
//                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                    subscriptionModelArrayList.clear();
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        //  Toast.makeText(getContext(), object.getString("Card_Back"), Toast.LENGTH_LONG).show();

                        SubscriptionModel subscriptionModel = new SubscriptionModel();
                        subscriptionModel.setPackageID(object.getString("PackageID"));
                        subscriptionModel.setPackageName(object.getString("Package_Name"));
                        subscriptionModel.setPackageDesc(object.getString("Package_Desc"));
                        subscriptionModel.setPrice(object.getString("Price"));
                        subscriptionModel.setPackageType(object.getString("Package_Type"));
                        subscriptionModel.setConnectionLimit(object.getString("Connection_Limit"));
                        subscriptionModel.setGroupLimit(object.getString("Group_Limit"));
                        subscriptionModel.setMonthlyConnectionLimit(object.getString("Monthy_Connection_Limit"));
                        subscriptionModel.setLetf_connection(object.getString("Connections_Left"));
                        subscriptionModel.setPackage_plan_id(object.getString("package_plan_id"));
                        subscriptionModelArrayList.add(subscriptionModel);

                        String PackageID = object.getString("PackageID");
                        String Package_Name = object.getString("Package_Name");
                        String Package_Desc = object.getString("Package_Desc");
                        String Price = object.getString("Price");
                        String Package_Type = object.getString("Package_Type");
                        String Connection_Limit = object.getString("Connection_Limit");
                        String Group_Limit = object.getString("Group_Limit");
                        String Monthy_Connection_Limit = object.getString("Monthy_Connection_Limit");
                    }

                    subscriptionAdapter = new SubscriptionAdapter(SubscriptionActivity.this, subscriptionModelArrayList);
                    listView.setAdapter(subscriptionAdapter);
                    subscriptionAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(getApplicationContext(), "Not able to load Cards..", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private class HttpAsyncSubscriptTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            String loading = "Loading" ;
            CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls)
        {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("my_userid", UserId );
                jsonObject.accumulate("subscription_id", SubscriptionID );

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return POST2(urls[0],jsonObject);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            rlProgressDialog.setVisibility(View.GONE);

            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

            try
            {
                if(result == "")
                {
                    Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_LONG).show();
                }
                else
                {
                    JSONObject response = new JSONObject(result);
                    String message = response.getString("Message");
                    String success = response.getString("Success");
                    if (success.equals("1")){
                        Toast.makeText(getApplicationContext(), "Subscribed..", Toast.LENGTH_LONG).show();
                        new HttpAsyncTask().execute(Utility.BASE_URL+"Subscription/GetPackageList");
                        new HttpAsyncTaskGetUserSubscription().execute(Utility.BASE_URL+"Subscription/GetUserSubscription");

                    }else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }


    private class HttpAsyncTokenTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            String loading = "Loading" ;
            CustomProgressDialog(loading);
        }

        @Override
        protected String doInBackground(String... urls)
        {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("amt", amount );
                jsonObject.accumulate("currency", "sgd" );
                jsonObject.accumulate("source", strToken );
                jsonObject.accumulate("Userid", UserId );
                jsonObject.accumulate("Email", email );
                jsonObject.accumulate("Description", PackageName );
                jsonObject.accumulate("PlanId", PlanId );

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return POST2(urls[0],jsonObject);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
//            dialog.dismiss();
            rlProgressDialog.setVisibility(View.GONE);

            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

            try
            {
                if(result == "")
                {
                    Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_LONG).show();
                }
                else
                {
                    JSONObject response = new JSONObject(result);
                    String message = response.getString("message");
                    String success = response.getString("Status");
                    SubscriptionID = response.getString("STRIPE_SUBSCRIPTION_ID");
                    if (success.equals("success")){
                        Toast.makeText(getApplicationContext(), "Paied..", Toast.LENGTH_LONG).show();
                        new HttpAsyncSubscriptTask().execute(Utility.BASE_URL+"Subscription/AddUser");
                    }else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void CustomProgressDialog(final String loading)
    {
        rlProgressDialog.setVisibility(View.VISIBLE);
        tvProgressing.setText(loading+"...");

        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anticlockwise);
        ivConnecting1.startAnimation(anim);
        Animation anim1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.clockwise);
        ivConnecting2.startAnimation(anim1);

    }
}
