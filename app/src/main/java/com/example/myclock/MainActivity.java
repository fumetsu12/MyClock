package com.example.myclock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView timeOut = findViewById(R.id.time);

        TimeZone chinaTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        Date date = new Date();
        SimpleDateFormat timeForm = new SimpleDateFormat("HH:mm:ss");
        timeForm.setTimeZone(chinaTimeZone);

        String time = timeForm.format(date);
        timeOut.setText(time);

        Button  refresh= findViewById(R.id.refresh);





    }
}