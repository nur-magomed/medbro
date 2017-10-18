package com.example.nmarsahanov.medbrat;

import android.net.Uri;
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
    //String IMAGE_URL = "https://lh3.googleusercontent.com/mxYA2XnI-4eqO2FaqLDoGird7yERflxs4zmthWhIHVKfzbQJZr-ILx_Ea-Fu1vha5A=w300";
    String IMAGE_URL = "http://rlspro.ru/Interaction?rls1=14596268338890001&tabletId=111&rls2=14596268338890006&ratio=1";
    private List<Medicine> medList      = new ArrayList<>();
    private List<String> medicinesStr   = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.img);
        MedAsyncTask medAsyncTask = new MedAsyncTask(this);
        medAsyncTask.asyncResponse = this;
        medAsyncTask.execute();




        Button btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,
                        "OnClickListener : " +
                                "\nMed 1 : "+ String.valueOf(autoTextView1.getText()) +
                                "\nMed 2 : "+ String.valueOf(autoTextView2.getText()),
                        Toast.LENGTH_LONG).show();

                  String imgUrl = getImageUrl(String.valueOf(autoTextView1.getText()), String.valueOf(autoTextView2.getText()));

                  new ImageLoadTask(imgUrl, imageView).execute();
            }
        });
    }

    private String getImageUrl(String med1, String med2) {

        String medId1 = getMedicineId(med1);
        String medId2 = getMedicineId(med2);
        int h = imageView.getHeight();
        int w = imageView.getWidth();
        int ratio = (int) Math.ceil(h/w);


        Uri baseUri = Uri.parse(IMAGE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        String mockID = "1111";
        //TODO ПОМЕНЯТЬ НА DEVid
        uriBuilder.appendQueryParameter("rls1", medId1);
        uriBuilder.appendQueryParameter("tabletId", mockID);
        uriBuilder.appendQueryParameter("rls2", medId2);
        uriBuilder.appendQueryParameter("ratio", (String.valueOf(ratio)));

        return uriBuilder.toString();
    }

    private String getMedicineId(String med) {
        for(Medicine m: medList){
            m.getName().equals(med);
            return String.valueOf(m.getId());
        }
        return null;
    }

    private void initAutoCompletes() {
        autoTextView1 = (AutoCompleteTextView) findViewById(R.id.actv_med1);
        autoTextView2 = (AutoCompleteTextView) findViewById(R.id.actv_med2);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,medicinesStr);
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
