package com.example.nmarsahanov.medbrat;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements AsyncResponse{

    Spinner spinner1;
    Spinner spinner2;
    ImageView imageView;
    String IMAGE_URL = "https://lh3.googleusercontent.com/mxYA2XnI-4eqO2FaqLDoGird7yERflxs4zmthWhIHVKfzbQJZr-ILx_Ea-Fu1vha5A=w300";
    private List<Medicine> medList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner1 = (Spinner) findViewById(R.id.spinner_1_med);
        spinner2 = (Spinner) findViewById(R.id.spinner_2_med);
        imageView = (ImageView) findViewById(R.id.img);
        MedAsyncTask medAsyncTask = new MedAsyncTask();
        medAsyncTask.asyncResponse = this;
        medAsyncTask.execute();




        Button btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,
                        "OnClickListener : " +
                                "\nSpinner 1 : "+ String.valueOf(spinner1.getSelectedItem()) +
                                "\nSpinner 2 : "+ String.valueOf(spinner2.getSelectedItem()),
                        Toast.LENGTH_LONG).show();

                  new ImageLoadTask(IMAGE_URL, imageView).execute();
            }
        });
    }

    @Override
    public void processFinish(List<Medicine> medicines) {
        medList = medicines;
        updateView();
    }

    private void updateView() {
        ArrayAdapter<Medicine> adapter = new ArrayAdapter<Medicine>(this,  android.R.layout.simple_list_item_1, medList );
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);
    }


}
