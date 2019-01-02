package com.ftsafe.rxjavademo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class ThirdActivity extends AppCompatActivity {

    private static final String TAG = "ThirdActivity";
    private TextView mTvInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        mTvInfo = findViewById(R.id.id_tv);

        useRxJava();

    }

    private void useRxJava(){
        //map
//        Observable.create(new ObservableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(ObservableEmitter<Integer> emitter) {
//                emitter.onNext(1);
//                emitter.onNext(2);
//                emitter.onNext(3);
//            }
//        }).map(new Function<Integer, String>() {
//            @Override
//            public String apply(Integer integer) {
//                return "This is result " + integer;
//            }
//        }).subscribeOn(Schedulers.newThread())
//          .observeOn(AndroidSchedulers.mainThread())
//          .subscribe(new Consumer<String>() {
//              @Override
//              public void accept(String s) throws Exception {
//                  Log.e(TAG, "accept: " + s);
//                  mTvInfo.setText(s);
//              }
//          });

        //flatmap
//        Observable.create(new ObservableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(ObservableEmitter<Integer> emitter) {
//                emitter.onNext(1);
//                emitter.onNext(2);
//                emitter.onNext(3);
//            }
//        }).flatMap(new Function<Integer, ObservableSource<String>>() {
//            @Override
//            public ObservableSource<String> apply(Integer integer) throws Exception {
//                final List<String> list = new ArrayList<>();
//                for(int i = 0; i < 3; i++){
//                    list.add("I am value " + integer);
//                }
//                return Observable.fromIterable(list).delay(10, TimeUnit.SECONDS);
//            }
//        }).subscribeOn(Schedulers.newThread())
//          .observeOn(AndroidSchedulers.mainThread())
//          .subscribe(new Consumer<String>() {
//              @Override
//              public void accept(String s) throws Exception {
//                  Log.e(TAG, "accept: " + s);
//                  mTvInfo.setText(s);
//              }
//          });

        //concatMap
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                final List<String> list = new ArrayList<>();
                for(int i = 0; i < 3; i++){
                    list.add("I am value " + integer);
                }
                return Observable.fromIterable(list).delay(10, TimeUnit.SECONDS);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e(TAG, "accept: " + s);
                        mTvInfo.setText(s);
                    }
                });
    }
}
