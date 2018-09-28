package com.dyh.eventbusdemo.one.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dyh.eventbusdemo.R;
import com.dyh.eventbusdemo.one.bean.MessageEvent;

import org.greenrobot.eventbus.EventBus;

public class SecondActivity extends AppCompatActivity {

    private Button mSecondBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        initView();
        click();
    }

    private void initView(){
        mSecondBtn = (Button) findViewById(R.id.id_btn_second);
    }

    private void click(){
        mSecondBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageEvent messageEvent = new MessageEvent("这是从SecondActivity传过来的数据!");

                //调用post方法后，表明SecondActivity是一个Publisher
                EventBus.getDefault().post(messageEvent);

                finish();
            }
        });
    }
}
