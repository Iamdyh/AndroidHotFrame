package com.ftsafe.rxjavademo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

public class FirstActivity extends AppCompatActivity {

    private static final String TAG = "FirstActivity";
    private TextView mTvInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        mTvInfo = findViewById(R.id.id_tv);

        userRxJava();
    }

    private void userRxJava(){
//        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
//                emitter.onNext(1);
//                emitter.onNext(2);
//                emitter.onNext(3);
//            }
//        });
//
//        Observer<Integer> observer = new Observer<Integer>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                Log.e(TAG, "onSubscribe: " + d);
//            }
//
//            @Override
//            public void onNext(Integer integer) {
//                mTvInfo.setText("" + integer);
//                Log.e(TAG, "onNext: " + integer);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.e(TAG, "onError: " + e);
//            }
//
//            @Override
//            public void onComplete() {
//                Log.e(TAG, "onComplete: ");
//            }
//        };
//        observable.subscribe(observer);


//        Observable.create(new ObservableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
//                emitter.onNext(1);
//                emitter.onNext(2);
//                emitter.onNext(3);
//            }
//        }).subscribe(new Observer<Integer>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                Log.e(TAG, "onSubscribe: " + d);
//            }
//
//            @Override
//            public void onNext(Integer integer) {
//                mTvInfo.setText("" + integer);
//                Log.e(TAG, "onNext: " + integer);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.e(TAG, "onError: " + e);
//            }
//
//            @Override
//            public void onComplete() {
//                Log.e(TAG, "onComplete: ");
//            }
//
//        });

//        Observable.create(new ObservableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
//                Log.e(TAG, "subscribe: 1");
//                emitter.onNext(1);
//                Log.e(TAG, "subscribe: 2");
//                emitter.onNext(2);
//                Log.e(TAG, "subscribe: 3");
//                emitter.onNext(3);
//                Log.e(TAG, "subscribe: onComplete");
//                emitter.onComplete();
//                Log.e(TAG, "subscribe: 4");
//                emitter.onNext(4);
//
//            }
//        }).subscribe(new Observer<Integer>() {
//
//            private Disposable mDisposable;
//            private int i;
//            @Override
//            public void onSubscribe(Disposable d) {
//                Log.e(TAG, "onSubscribe: " + d);
//                mDisposable = d;
//            }
//
//            @Override
//            public void onNext(Integer integer) {
//                mTvInfo.setText("" + integer);
//                Log.e(TAG, "onNext: " + integer);
//                i++;
//                if(i == 2){
//                    mDisposable.dispose();
//                    Log.e(TAG, "isDisposed: " + mDisposable.isDisposed());
//                }
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.e(TAG, "onError: " + e);
//            }
//
//            @Override
//            public void onComplete() {
//                Log.e(TAG, "onComplete: ");
//            }
//
//        });

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.e(TAG, "subscribe: 1");
                emitter.onNext(1);
                Log.e(TAG, "subscribe: 2");
                emitter.onNext(2);
                Log.e(TAG, "subscribe: 3");
                emitter.onNext(3);
                Log.e(TAG, "subscribe: onComplete");
                emitter.onComplete();
                Log.e(TAG, "subscribe: 4");
                emitter.onNext(4);

            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.e(TAG, "accept: " + integer);
                mTvInfo.setText("" + integer);
            }
        });
    }
}
