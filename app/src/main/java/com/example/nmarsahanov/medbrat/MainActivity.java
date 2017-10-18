package com.example.nmarsahanov.medbrat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AsyncResponse{

    AutoCompleteTextView autoTextView1;
    AutoCompleteTextView autoTextView2;
    ImageView imageView;
    String IMAGE_URL = "https://lh3.googleusercontent.com/mxYA2XnI-4eqO2FaqLDoGird7yERflxs4zmthWhIHVKfzbQJZr-ILx_Ea-Fu1vha5A=w300";
    private List<Medicine> medList      = new ArrayList<>();
    private List<String> medicinesStr   = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                                "\nSpinner 1 : "+ String.valueOf(autoTextView1.getText()) +
                                "\nSpinner 2 : "+ String.valueOf(autoTextView2.getText()),
                        Toast.LENGTH_LONG).show();

                  new ImageLoadTask(IMAGE_URL, imageView).execute();
            }
        });
    }

    private void initAutoCompletes() {

        autoTextView1 = (AutoCompleteTextView) findViewById(R.id.actv_med1);
        autoTextView2 = (AutoCompleteTextView) findViewById(R.id.actv_med2);

        //String[] meds = (String[]) medMap.values().toArray();
        // Create the adapter and set it to the AutoCompleteTextView

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,medicinesStr);
        autoTextView1.setAdapter(adapter);
        autoTextView2.setAdapter(adapter);


    }

    @Override
    public void processFinish(List<Medicine> medicines) {
        medList = medicines;
        loadStringList();
        initAutoCompletes();
    }

    private void loadStringList() {

        for (Medicine m: medList){
            medicinesStr.add(m.getName());
        }

    }


}
