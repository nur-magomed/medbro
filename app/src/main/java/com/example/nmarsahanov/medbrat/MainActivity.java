package com.example.nmarsahanov.medbrat;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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
    String IMAGE_URL = "http://rlspro.ru/Interaction?";
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

        //TODO add three dot menu
        Button btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (autoTextView1.getText().toString().trim().equals("") ||
                    autoTextView2.getText().toString().trim().equals("")){
                    Toast.makeText(MainActivity.this, "Введите название лекарства", Toast.LENGTH_SHORT).show();
                } else {
                    String imgUrl = getImageUrl(String.valueOf(autoTextView1.getText()), String.valueOf(autoTextView2.getText()));
                    new ImageLoadTask(imgUrl, imageView).execute();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //on menu item click 'about'
        if (id == R.id.action_about) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String getImageUrl(String med1, String med2) {

        String medId1 = getMedicineId(med1);
        String medId2 = getMedicineId(med2);
        int ratio = 1;


        Uri baseUri = Uri.parse(IMAGE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        String mockID = "1111";
        //TODO ПОМЕНЯТЬ НА DEVid
        uriBuilder.appendQueryParameter("rls1", medId1);
        uriBuilder.appendQueryParameter("deviceId", mockID);
        uriBuilder.appendQueryParameter("rls2", medId2);
        uriBuilder.appendQueryParameter("ratio", (String.valueOf(ratio)));

        return uriBuilder.toString();
    }

    private String getMedicineId(String med) {
        for(Medicine m: medList){
            if(m.getName().equals(med)){
                return String.valueOf(m.getId());
            }
        }
        return "10000042";
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
