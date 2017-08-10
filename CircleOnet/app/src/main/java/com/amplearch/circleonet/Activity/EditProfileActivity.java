package com.amplearch.circleonet.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.amplearch.circleonet.R;
import com.amplearch.circleonet.Utils.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;

import static android.R.attr.path;
import static junit.framework.Assert.assertEquals;

public class EditProfileActivity extends AppCompatActivity
{
    private GridView gridView ;
    private String[] array ;
    private EditText etAttachFile ;
    private ImageView ivAttachFile ;

    private CharSequence[] items ;
    private String userChoosenTask;

    private String file_path = "";
    private File file ;

    private static final int PICKFILE_RESULT_CODE = 1;
    private int REQUEST_CAMERA = 0, REQUEST_GALLERY = 1, REQUEST_DOCUMENT = 2, REQUEST_AUDIO = 3;

    private int camera_permission ;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_profile);

        gridView = (GridView)findViewById(R.id.gridView);
        etAttachFile = (EditText)findViewById(R.id.etAttachFile);
        ivAttachFile = (ImageView)findViewById(R.id.ivAttachFile);

        array = new String[]{"Accommodations","Information","Accounting","Information technology","Advertising",
                "Insurance","Aerospace","Journalism & News","Agriculture & Agribusiness","Legal Services","Air Transportation",
                "Manufacturing","Apparel & Accessories","Media & Broadcasting","Auto","Medical Devices & Supplies","Banking",
                "Motions Picture & Video","Beauty & Cosmetics","Music","Biotechnology","Pharmaceutical","Chemical","Public Administration",
                "Communications","Public Relations","Computer","Publishing","Construction","Rail","Consulting","Real Estate",
                "Consumer Products","Retail","Education","Service","Electronics","Sports","Employment","Technology","Energy",
                "Telecommunications","Entertainment & Recreation","Tourism","Fashion","Transportation","Financial Services",
                "Travel","Fine Arts","Utilities","Food & Beverage","Video Game","Green Technology","Web Services","Health"};
        gridView.setAdapter(new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1, array));

        ivAttachFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               /* Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file*//*");
                startActivityForResult(intent,PICKFILE_RESULT_CODE);*/
                selectFile();
            }
        });

        camera_permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (camera_permission != PackageManager.PERMISSION_GRANTED)
        {
            Log.i("Camera Permission", "Permission to record denied");
            makeRequest();
        }
    }

    protected void makeRequest()
    {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // TODO Auto-generated method stub

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            if(resultCode == PICKFILE_RESULT_CODE)
            {
                String FilePath = data.getData().getPath();

                File file = new File(FilePath);
                String file_name = file.getName();

                etAttachFile.setText(file_name);
            }
            else if(requestCode == REQUEST_CAMERA)
            {
                onCaptureImageResult(data);
            }
            else if(requestCode == REQUEST_GALLERY)
            {
                onSelectFromGalleryResult(data);
            }
            else if(requestCode == REQUEST_DOCUMENT)
            {
                onSelectFromFiles(data);
            }
            else if(requestCode == REQUEST_AUDIO)
            {
                onSelectFromAudio(data);
            }
        }
      /*  switch(requestCode)
        {
            case PICKFILE_RESULT_CODE:
                if(resultCode == RESULT_OK)
                {
                    String FilePath = data.getData().getPath();

//                    etAttachFile.setText(FilePath);

                    File file = new File(FilePath);
                    String file_name = file.getName();

                    etAttachFile.setText(file_name);

                }
                break;
        }*/
    }

    private void selectFile()
    {
        items = new CharSequence[]{"Take Document","Take Picture","Choose from Media","Take Audio","Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle("Attach File!");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(EditProfileActivity.this);

                if (items[item].equals("Take Picture"))
                {
                    userChoosenTask = "Take Picture";
                    if(result)
                    {
                        cameraIntent();
                    }
                }
                else if (items[item].equals("Choose from Media"))
                {
                    userChoosenTask = "Choose from Media";
                    if(result)
                    {
                        galleryIntent();
                    }
                }
                else if (items[item].equals("Cancel"))
                {
                    dialog.dismiss();
                }
                else if (items[item].equals("Take Document"))
                {
                    userChoosenTask = "Take Document";
                    if(result)
                    {
                        documentIntent();
                    }
                }
                else if (items[item].equals("Take Audio"))
                {
                    userChoosenTask = "Take Audio";
                    if(result)
                    {
                        audioIntent();
                    }
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"),REQUEST_GALLERY);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void documentIntent()
    {
        Intent intent = new Intent();
        intent.setType("file//*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_DOCUMENT);
    }

    private void audioIntent()
    {
        /*Intent intent_upload = new Intent();
        intent_upload.setType("audio*//*");
        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent_upload,REQUEST_AUDIO);*/

     /*   Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_AUDIO);  */

     /* Intent intent = new Intent();
        intent.setType("audio*//*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Audio"), REQUEST_AUDIO); */

        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Audio"), REQUEST_AUDIO);
    }

    private void onSelectFromGalleryResult(Intent data)
    {
        Uri selectedImageUri = data.getData();
        String imgPath = getPath(selectedImageUri);

        File imgFile = new File(imgPath);
        String imgName = imgFile.getName();

        etAttachFile.setText(imgName);

        Bitmap bm = null;
        if (data != null)
        {
            try
            {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            }
            catch (IOException e)
            {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Image too large.", Toast.LENGTH_LONG).show();
            }
        }
//        ivProfileImg.setImageBitmap(bm);
    }

    public String getPath(Uri uri)
    {
        String[] projection = {MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void onCaptureImageResult(Intent data)
    {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
        Uri imgUri = getImageUri(getApplicationContext(), thumbnail);
        // CALL THIS METHOD TO GET THE ACTUAL PATH
        File imgFile = new File(getRealPathFromURI(imgUri));
        String imgName = imgFile.getName();

        etAttachFile.setText(imgName);

        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try
        {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
//        ivProfileImg.setImageBitmap(thumbnail);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri)
    {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void onSelectFromFiles(Intent data)
    {
        String docsPath = data.getData().getPath();
        File docsFile = new File(docsPath);
        String docsName = docsFile.getName();

        etAttachFile.setText(docsName);
    }

    private void onSelectFromAudio(Intent data)
    {
        //the selected audio.
//        Uri uri = data.getData();
       /* String audioPath = data.getData().getPath();
        File audioFile = new File(audioPath);
        String audioName = audioFile.getName();
        etAttachFile.setText(audioName);

        Toast.makeText(getApplicationContext(),"Audio path: "+audioPath,Toast.LENGTH_LONG).show();*/

//        mMediaPlayer = MediaPlayer.create(getApplicationContext(), uri);

        Uri selectedImageUri = data.getData();
        String audioPath = getPath(selectedImageUri);

        File audioFile = new File(audioPath);
        String audioName = audioFile.getName();

        etAttachFile.setText(audioName);
    }

    private void createFile()
    {
        final File file = new File(Environment.getExternalStorageDirectory(), "read.me");
        Uri uri = Uri.fromFile(file);
        File auxFile = new File(uri.toString());
        assertEquals(file.getAbsolutePath(), auxFile.getAbsolutePath());

       /* new File(uri.getPath());
//        or
        new File(uri.toString());

//        NOTE: url.toString() return a String in the format: "file:///mnt/sdcard/myPicture.jpg",
// whereas url.getPath() returns a String in the format: "/mnt/sdcard/myPicture.jpg",  */

       // and
//        new File(new URI(androidURI.toString()));

    }

   /* private void getFile()
    {
        File file = new File(getPath(uri));
    }*/

   /* public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }*/


}
