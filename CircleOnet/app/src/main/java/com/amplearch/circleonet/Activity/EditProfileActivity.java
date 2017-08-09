package com.amplearch.circleonet.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.UUID;

import static android.R.attr.path;

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

    private int REQUEST_CAMERA = 0, REQUEST_GALLERY = 1, REQUEST_DOCUMENT = 2;


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
        items = new CharSequence[]{"Take Documents","Take Camera", "Choose from Media", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle("Attach File!");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(EditProfileActivity.this);

                if (items[item].equals("Take Camera"))
                {
                    userChoosenTask = "Take Camera";
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
                else if (items[item].equals("Take Documents"))
                {
                    userChoosenTask = "Take Documents";
                    if(result)
                    {
                        documentIntent();
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
        String[] projection = { MediaStore.Images.Media.DATA };
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

    private void onSelectFromFiles(Intent data)
    {
        String FilePath = data.getData().getPath();
        File file = new File(FilePath);
        String fileName = file.getName();

        etAttachFile.setText(fileName);
    }


}
