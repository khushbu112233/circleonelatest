package com.circle8.circleOne.Activity;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.databinding.ActivityCardVerificationBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static com.circle8.circleOne.Utils.Utility.CustomProgressDialog;
import static com.circle8.circleOne.Utils.Utility.POST2;
import static com.circle8.circleOne.Utils.Utility.dismissProgress;

public class CardVerificationActivity extends AppCompatActivity implements View.OnClickListener
{
    public String secretKey = "1234567890234561";
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mIntentFilters;
    private String[][] mNFCTechLists;
    ArrayList<String> arrayNFC ;
    String ProfileId = "", CardCode = "";
    ActivityCardVerificationBinding mActivityCardVerificationBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mActivityCardVerificationBinding = DataBindingUtil.setContentView(this,R.layout.activity_card_verification);
        Utility.freeMemory();
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter != null) {
            //txtNoGroup.setText("Read an NFC tag");
        } else {
            mActivityCardVerificationBinding.txtNoGroup.setText("Your current mobile device is not NFC-enabled. \nPlease login via an NFC-enabled device to unlock your card.");
            mActivityCardVerificationBinding.ivAddCard.setVisibility(View.GONE);
        }

        // create an intent with tag data and deliver to this activity
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // set an intent filter for all MIME data
        IntentFilter ndefIntent = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefIntent.addDataType("*/*");
            mIntentFilters = new IntentFilter[] { ndefIntent };
        } catch (Exception e) {
            Log.e("TagDispatch", e.toString());
        }

        mNFCTechLists = new String[][] { new String[] { NfcF.class.getName() } };

        mActivityCardVerificationBinding.imgBackCard.setOnClickListener(this);

//        new HttpAsyncActivateNFC().execute(Utility.BASE_URL+"NFCSecurity/ActivateNFC");
    }


    public static final byte[] MIME_TEXT = "application/com.circle8.circleOne".getBytes();
    @Override
    public void onNewIntent(Intent intent) {
        String action = intent.getAction();
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        String s = "";

        // parse through all NDEF messages and their records and pick text type only
        Parcelable[] data = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        if (data != null) {
            try {
                Utility.freeMemory();
                arrayNFC = new ArrayList<>();
                for (int i = 0; i < data.length; i++) {
                    NdefRecord[] recs = ((NdefMessage)data[i]).getRecords();
                    for (int j = 0; j < recs.length; j++) {
                        if (recs[j].getTnf() == NdefRecord.TNF_MIME_MEDIA &&
                                Arrays.equals(recs[j].getType(), MIME_TEXT)) {

                            byte[] payload = recs[j].getPayload();
                            String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
                            int langCodeLen = payload[0] & 0077;

                            s += ("\n" +
                                    new String(payload, langCodeLen + 1,
                                            payload.length - langCodeLen - 1, textEncoding) );
                            String s1 = new String(payload, langCodeLen + 1,
                                    payload.length - langCodeLen - 1, textEncoding);
                            String decryptstr = decrypt(s1, secretKey);
                            arrayNFC.add(decryptstr);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("TagDispatch", e.toString());
            }

        }
        //Toast.makeText(getApplicationContext(), arrayNFC.toString(), Toast.LENGTH_LONG).show();
        if (arrayNFC.size() == 1){
            Utility.freeMemory();
            mActivityCardVerificationBinding.txtNoGroup.setText("Your Card is already verified..");
            mActivityCardVerificationBinding.ivAddCard.setVisibility(View.GONE);
        }
        else if (arrayNFC.size() == 2){
            Utility.freeMemory();
            ProfileId = arrayNFC.get(0).toString();
            CardCode = arrayNFC.get(1).toString();
           // Toast.makeText(getApplicationContext(), ProfileId + " " + CardCode, Toast.LENGTH_LONG).show();
            new HttpAsyncActivateNFC().execute(Utility.BASE_URL+"NFCSecurity/ActivateNFC");
        }
        else {
            Utility.freeMemory();
            mActivityCardVerificationBinding.txtNoGroup.setText("Please use only CircleOne NFC-Card for unlock");
            mActivityCardVerificationBinding.ivAddCard.setVisibility(View.GONE);
        }
        //txtNoGroup.setText(s);
    }

    public String decrypt(String value, String key)
            throws GeneralSecurityException, IOException {
        Utility.freeMemory();
        byte[] value_bytes = Base64.decode(value, 0);
        byte[] key_bytes = getKeyBytes(key);
        return new String(decrypt(value_bytes, key_bytes, key_bytes), "UTF-8");
    }

    public byte[] decrypt(byte[] ArrayOfByte1, byte[] ArrayOfByte2, byte[] ArrayOfByte3)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        // setup AES cipher in CBC mode with PKCS #5 padding
        Cipher localCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        Utility.freeMemory();
        // decrypt
        localCipher.init(2, new SecretKeySpec(ArrayOfByte2, "AES"), new IvParameterSpec(ArrayOfByte3));
        return localCipher.doFinal(ArrayOfByte1);
    }

    private byte[] getKeyBytes(String paramString)
            throws UnsupportedEncodingException {
        Utility.freeMemory();
        byte[] arrayOfByte1 = new byte[16];
        byte[] arrayOfByte2 = paramString.getBytes("UTF-8");
        System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, Math.min(arrayOfByte2.length, arrayOfByte1.length));
        return arrayOfByte1;
    }




    @Override
    public void onResume() {
        super.onResume();
        Utility.freeMemory();
        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, mNFCTechLists);
    }

    @Override
    public void onPause() {
        super.onPause();
        Utility.freeMemory();
        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }


    @Override
    public void onClick(View v)
    {
        if ( v == mActivityCardVerificationBinding.imgBackCard)
        {
            Utility.freeMemory();
            finish();
        }
        if ( v == mActivityCardVerificationBinding.ivAddCard)
        {
            Utility.freeMemory();
        }
    }

    private class HttpAsyncActivateNFC extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
           /* dialog = new ProgressDialog(MerchantDetailActivity.this);
            dialog.setMessage("Get Details...");
            dialog.show();
            dialog.setCancelable(false);*/

            String loading = "Activating NFC card" ;
            CustomProgressDialog(loading, CardVerificationActivity.this);
        }

        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("card_code", CardCode);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return POST2(urls[0],jsonObject);
        }

        @Override
        protected void onPostExecute(String result)
        {
            Utility.freeMemory();
//            dialog.dismiss();
            dismissProgress();
          //  Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try
            {
                if (result != null)
                {
                    JSONObject jsonObject = new JSONObject(result);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    String card_code = jsonObject.getString("card_code");
                    if (success.equalsIgnoreCase("1")){
                        Toast.makeText(getApplicationContext(), "Your NFC card is unlock", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
