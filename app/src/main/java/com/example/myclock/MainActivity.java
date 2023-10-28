package com.example.myclock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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
    private TextView timeOut;
    private SharedPreferences preferences;
    private final Runnable runable= new Runnable() {
        //每秒更新一次时钟
        @Override
        public void run() {
            timeOut.setText(getTimeForm());
            handler.postDelayed(this,1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化控件
        timeOut = findViewById(R.id.time);
        Button button_refresh = findViewById(R.id.refresh);
        Button button_delete = findViewById(R.id.delete);
        Button button_record = findViewById(R.id.record);
        RecyclerView timeTable = findViewById(R.id.timeTable);
        //初始化适配器
        MyAdapter myAdapter = new MyAdapter(recordList);
        timeTable.setAdapter(myAdapter);
        //初始化布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        timeTable.setLayoutManager(layoutManager);
        //获得SharedPreferences和editor对象
        preferences = getSharedPreferences("timetable", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        //输出时间
        timeOut.setText(getTimeForm());
        handler.post(runable);

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

                editor.putString("record" + time_number, time);
                editor.putInt("number", time_number);
                editor.commit();
            }
        });
        //删除时间按钮
        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (time_number > 0) {
                    editor.remove("record" + time_number);
                    time_number--;
                    editor.putInt("number", time_number);
                    editor.commit();
                    recordList.remove(recordList.size() - 1);
                    // 通知adapter数据已经改变,改变输出
                    myAdapter.notifyDataSetChanged();
                }
            }
        });
        reload();
    }
    //设置时区并获得输出格式
    private String getTimeForm(){
        TimeZone chinaTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        SimpleDateFormat timeForm = new SimpleDateFormat("HH:mm:ss");
        timeForm.setTimeZone(chinaTimeZone);
        return timeForm.format(new Date());
    }
    //用户在关闭应用后再次打开时，重新加载之前保存的所有时间记录
    private void reload() {
        time_number = preferences.getInt("number", 0);
        int number = time_number;
        for (int i = 1; i <= number; i++) {
            recordList.add(new Record(preferences.getString("record"+i,null)));
        }
    }
}