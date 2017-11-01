package com.example.nmarsahanov.medbrat;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AsyncResponse{


    RelativeLayout rlLoadingIndicatior;
    TextView connProblemsTv;
    ProgressBar loadingIndicator;
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

        rlLoadingIndicatior = (RelativeLayout) findViewById(R.id.rl_load_indicator);
        connProblemsTv = (TextView) findViewById(R.id.tv_connection_problem);
        loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);

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
                    imageView.setVisibility(View.GONE);
                    rlLoadingIndicatior.setVisibility(View.VISIBLE);
                    connProblemsTv.setVisibility(View.INVISIBLE);
                    loadingIndicator.setVisibility(View.VISIBLE);
                    String imgUrl = getImageUrl(String.valueOf(autoTextView1.getText()), String.valueOf(autoTextView2.getText()));

                    // Get a reference to the ConnectivityManager to check state of network connectivity
                    ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    // Get details on the currently active default data network
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    // If there is a network connection, fetch data
                    if (networkInfo != null && networkInfo.isConnected()) {
                        new ImageLoadTask(imgUrl, imageView, rlLoadingIndicatior).execute();
                    } else {
                        // Otherwise, display error
                        // First, hide loading indicator so error message will be visible
                        rlLoadingIndicatior.setVisibility(View.INVISIBLE);
                        connProblemsTv.setVisibility(View.VISIBLE);
                    }

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


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(MainActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

        autoTextView1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        autoTextView2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
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
