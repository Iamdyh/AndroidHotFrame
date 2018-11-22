package com.dyhdev.asynctaskdemo;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ProgressBar;

/**
 * Copyright © FEITIAN Technologies Co., Ltd. All Rights Reserved.
 *
 * @Date 2018/11/22  15:38
 * @Author DYH
 * @Des 点击'加载进度条'按钮后程序看起来运行正常。但是,如果接着点击BACK键,紧接着再次点击'加载进度条'按钮,会发现进度条的进度一直是零,过了一会才开始更新。
 * 我们知道,AsyncTask是基于线程池进行实现的,当一个线程没有结束时,后面的线程是不能执行的.所以必须等到第一个task的for循环结束后,才能执行第二个task.
 * 我们知道,当点击BACK键时会调用Activity的onPause()方法.为了解决这个问题,我们需要在Activity的onPause()方法中将正在执行的task标记为cancel状态,
 * 在doInBackground方法中进行异步处理时判断是否是cancel状态来决定是否取消之前的task.
 */
public class ProgressBarActivity extends Activity {

    private ProgressBar mProgressBar;
    private MyAsyncTask myAsyncTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        mProgressBar = (ProgressBar)findViewById(R.id.id_progressbar);
        myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
    }

    //AsyncTask是基于线程池进行实现的,当一个线程没有结束时,后面的线程是不能执行的.
    @Override
    protected void onPause() {
        super.onPause();
        if(myAsyncTask != null && myAsyncTask.getStatus() == AsyncTask.Status.RUNNING){
            //cancel方法只是将对应的AsyncTask标记为cancel状态,并不是真正的取消线程的执行.
            myAsyncTask.cancel(true);
        }
    }

    class MyAsyncTask extends AsyncTask<Void, Integer, Void>{
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //更新进度条
            mProgressBar.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for(int i = 0; i < 100; i++){
                //如果task是cancel状态，则终止for循环，以进行下个task的执行
                if(isCancelled()){
                    break;
                }
                publishProgress(i);
                try {
                    //模拟耗时操作
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
