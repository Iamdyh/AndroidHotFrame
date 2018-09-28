package com.dyh.eventbusdemo.one.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dyh.eventbusdemo.R;
import com.dyh.eventbusdemo.one.bean.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FirstActivity extends AppCompatActivity {

    private Button mFirstBtn;
    private TextView mFirstTv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        //注册, FirstActivity是一个Subscriber
        EventBus.getDefault().register(this);

        initView();
        click();

    }

    private void initView(){
        mFirstBtn  = (Button) findViewById(R.id.id_btn_first);
        mFirstTv = (TextView) findViewById(R.id.id_tv_first);
    }

    private void click(){
        mFirstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 事件处理方法，可以自定义名字，该方法必须是public，EventBus内部通过反射会去调用这个方法
     * @param messageEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent){
        mFirstTv.setText(messageEvent.getMessage());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
