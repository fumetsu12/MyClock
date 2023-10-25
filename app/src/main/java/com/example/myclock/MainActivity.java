package com.example.myclock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private final Handler handler = new Handler();
    private final Runnable runable= new Runnable() {
        @Override
        public void run() {
            TextView timeOut = findViewById(R.id.time);
            timeOut.setText(getTimeForm());
            handler.postDelayed(this,1000);
        }
    };
    private List<Record> recordList = new ArrayList<>();
    int time_number = 0;
    SharedPreferences timeRecord = getSharedPreferences("timeTable", MODE_PRIVATE);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView timeOut = findViewById(R.id.time);
        timeOut.setText(getTimeForm());

        Button refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeOut.setText(getTimeForm());
            }
        });

        Button record = findViewById(R.id.record);

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time = getTimeForm();
                time_number++;
                recordList.add(new Record(time));

                SharedPreferences.Editor editor = timeRecord.edit();
                editor.putString("record" + time_number, time);
                editor.putInt("number", time_number);
                editor.commit();
            }
        });

        handler.post(runable);
    }

    private String getTimeForm(){
        TimeZone chinaTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        Date date = new Date();
        SimpleDateFormat timeForm = new SimpleDateFormat("HH:mm:ss");
        timeForm.setTimeZone(chinaTimeZone);

        return timeForm.format(date);
    }
}