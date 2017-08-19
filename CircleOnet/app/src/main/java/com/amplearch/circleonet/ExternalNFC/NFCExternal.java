package com.amplearch.circleonet.ExternalNFC;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.text.AlphabeticIndex;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amplearch.circleonet.R;

import be.appfoundry.nfclibrary.activities.NfcActivity;
import be.appfoundry.nfclibrary.utilities.interfaces.NfcReadUtility;
import be.appfoundry.nfclibrary.utilities.sync.NfcReadUtilityImpl;


@SuppressLint({"ParserError", "ParserError"})

public class NFCExternal extends NfcActivity {

    NfcAdapter adapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    boolean writeMode;
    Tag mytag2;
    Button btnOk;
    NfcReadUtility mNfcReadUtility = new NfcReadUtilityImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcexternal);

        btnOk = (Button) findViewById(R.id.btnOk);

        adapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writeTagFilters = new IntentFilter[]{tagDetected};

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    // write(message.getText().toString(),mytag);
                    write("en000000001", "gfbhgfbg@bgfbg.dffdbv", mytag2);
                    Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();
                    // Toast.makeText(ctx2, ctx2.getString(R.string.ok_writing), Toast.LENGTH_LONG ).show();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (FormatException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void write(String text, String text2, Tag tag) throws IOException, FormatException {

        NdefRecord[] records = {createRecord(text), createRecord(text2)};
        NdefMessage message = new NdefMessage(records);
        // Get an instance of Ndef for the tag.
        Ndef ndef = Ndef.get(tag);

        // Enable I/O
        ndef.connect();
        // Write the message
        ndef.writeNdefMessage(message);

        // Close the connection
        ndef.close();
    }

    private NdefRecord createRecord(String text) throws UnsupportedEncodingException {
        String lang = "en";
        byte[] textBytes = text.getBytes();
        byte[] langBytes = lang.getBytes("US-ASCII");
        int langLength = langBytes.length;
        int textLength = textBytes.length;
        byte[] payload = new byte[1 + langLength + textLength];

        // set status byte (see NDEF spec for actual bits)
        payload[0] = (byte) langLength;

        // copy langbytes and textbytes into payload
        System.arraycopy(langBytes, 0, payload, 1, langLength);
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

        String mimeType = "application/com.amplearch.circleonet";

        byte[] mimeBytes = mimeType.getBytes(Charset.forName("UTF-8"));
        byte[] dataBytes = text.getBytes(Charset.forName("UTF-8"));
        byte[] id = new byte[0];

        // NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_EXTERNAL_TYPE,  NdefRecord.RTD_TEXT,  new byte[0], payload);
        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], payload);

        return recordNFC;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        mytag2 = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {

            String action = intent.getAction();
            if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
                Ndef ndef = Ndef.get(mytag2);
                if (ndef == null) {
                    // NDEF is not supported by this Tag.
                    return;
                }
                NdefMessage ndefMessage = ndef.getCachedNdefMessage();

                NdefRecord[] records = ndefMessage.getRecords();
                for (NdefRecord ndefRecord : records) {
                    //read each record
                    byte[] payload = ndefRecord.getPayload();

                    // Get the Text Encoding
                    String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

                    // Get the Language Code
                    int languageCodeLength = payload[0] & 0063;

                    // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
                    // e.g. "en"

                    // Get the Text
                    try {
                        String str = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
                        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }
            }



       /*     if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
                NdefMessage[] messages = null;
                Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_TAG);
                if (rawMsgs != null) {
                    Toast.makeText(getApplicationContext(), "rawmessage length = "+ rawMsgs.length, Toast.LENGTH_LONG);
                    messages = new NdefMessage[rawMsgs.length];
                    for (int i = 0; i < rawMsgs.length; i++) {
                        messages[i] = (NdefMessage) rawMsgs[i];
                    }
                }
                if (messages[0] != null) {
                    String result = "";
                    byte[] payload = messages[0].getRecords()[0].getPayload();
                    // this assumes that we get back am SOH followed by host/code
                    Toast.makeText(getApplicationContext(), "payload length = "+ payload.length, Toast.LENGTH_LONG);

                    for (int b = 1; b < payload.length; b++) { // skip SOH
                        result += (char) payload[b];
                    }
                    Toast.makeText(getApplicationContext(), "Tag Contains " + result, Toast.LENGTH_SHORT).show();
                }
            }*/
        }
    }


   /* void buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) {
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout content = mTagContent;
        // Clear out any old views in the content area, for example if you scan
        // two tags in a row.
        content.removeAllViews();
        // Parse the first message in the list
        // Build views for all of the sub records
        List<AlphabeticIndex.Record> records;
        try {
            records = new Message(msgs[0]);
            final int size = records.size();
            for (int i = 0; i < size; i++) {
                AlphabeticIndex.Record record = records.get(i);
                View view = getView(record, inflater, content, i);
                if(view != null) {
                    content.addView(view);
                }
                inflater.inflate(R.layout.tag_divider, content, true);
            }
        } catch (FormatException e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void onPause() {
        super.onPause();
        WriteModeOff();
    }

    @Override
    public void onResume() {
        super.onResume();
        WriteModeOn();
    }

    private void WriteModeOn() {
        writeMode = true;
        if (adapter == null) {
            adapter = NfcAdapter.getDefaultAdapter(this);
        }
        adapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
    }

    private void WriteModeOff() {

        writeMode = false;
        if (adapter == null) {
            adapter = NfcAdapter.getDefaultAdapter(this);
        }
        adapter.disableForegroundDispatch(this);
    }


}
