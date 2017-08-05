package com.amplearch.circleonet.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.amplearch.circleonet.R;

import java.io.File;

import static android.R.attr.path;

public class EditProfileActivity extends AppCompatActivity
{
    private GridView gridView ;
    private String[] array ;
    private EditText etAttachFile ;
    private ImageView ivAttachFile ;

    private String file_path = "";
    private File file ;

    private static final int PICKFILE_RESULT_CODE = 1;


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
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent,PICKFILE_RESULT_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // TODO Auto-generated method stub
        switch(requestCode)
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
        }
    }
}
