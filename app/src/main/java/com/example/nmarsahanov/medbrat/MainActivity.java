package com.example.nmarsahanov.medbrat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner1 = (Spinner) findViewById(R.id.spinner_1_med);
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner_2_med);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_1, getStringArray() );
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);
    }

    public List<String> getStringArray() {
        List<String> stringArray = new ArrayList<>();

        stringArray.add("med1");
        stringArray.add("med2");
        stringArray.add("med3");
        stringArray.add("med4");
        stringArray.add("med5");
        stringArray.add("med6");
        stringArray.add("med7");
        stringArray.add("med8");

        return stringArray;
    }
}
