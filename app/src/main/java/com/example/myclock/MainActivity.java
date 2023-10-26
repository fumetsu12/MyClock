package com.example.myclock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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
    private List<Record> recordList = new ArrayList<>();
    int time_number = 0;
    private final Runnable runable= new Runnable() {
        //每秒更新一次时钟
        @Override
        public void run() {
            TextView timeOut = findViewById(R.id.time);
            timeOut.setText(getTimeForm());
            handler.postDelayed(this,1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化控件
        TextView timeOut = findViewById(R.id.time);
        Button button_refresh = findViewById(R.id.refresh);
        Button button_record = findViewById(R.id.record);
        RecyclerView timeTable = findViewById(R.id.timeTable);
        //初始化适配器
        MyAdapter myAdapter = new MyAdapter(recordList);
        //设置适配器
        timeTable.setAdapter(myAdapter);
        //输出时间
        timeOut.setText(getTimeForm());
        //刷新
        handler.post(runable);

        SharedPreferences timeRecord = getSharedPreferences("timeTable", MODE_PRIVATE);
        //更新时间按钮实先
        button_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeOut.setText(getTimeForm());
            }
        });
        //记录时间按钮实现
        button_record.setOnClickListener(new View.OnClickListener() {
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
        reload();
    }

    private String getTimeForm(){
        TimeZone chinaTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        Date date = new Date();
        SimpleDateFormat timeForm = new SimpleDateFormat("HH:mm:ss");
        timeForm.setTimeZone(chinaTimeZone);

        return timeForm.format(date);
    }
    private void reload() {
        SharedPreferences timeRecord = getSharedPreferences("timeTable", MODE_PRIVATE);
        time_number = timeRecord.getInt("number", 0);
        int number = time_number;
        for (int i = 1; i <= number; i++) {
            recordList.add(new Record(timeRecord.getString("record"+i,null)));
        }
    }
}