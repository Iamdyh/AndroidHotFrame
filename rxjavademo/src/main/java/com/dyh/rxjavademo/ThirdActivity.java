package com.dyh.rxjavademo;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
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
//        Observable.create(new ObservableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(ObservableEmitter<Integer> emitter) {
//                emitter.onNext(1);
//                emitter.onNext(2);
//                emitter.onNext(3);
//            }
//        }).concatMap(new Function<Integer, ObservableSource<String>>() {
//            @Override
//            public ObservableSource<String> apply(Integer integer) throws Exception {
//                final List<String> list = new ArrayList<>();
//                for(int i = 0; i < 3; i++){
//                    list.add("I am value " + integer);
//                }
//                return Observable.fromIterable(list).delay(10, TimeUnit.SECONDS);
//            }
//        }).subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//                        Log.e(TAG, "accept: " + s);
//                        mTvInfo.setText(s);
//                    }
//                });


        //zip
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.e(TAG, "emit: 1");
                emitter.onNext(1);
                SystemClock.sleep(1000);

                Log.e(TAG, "emit: 2");
                emitter.onNext(2);
                SystemClock.sleep(1000);

                Log.e(TAG, "emit: 3");
                emitter.onNext(3);
                SystemClock.sleep(1000);

                Log.e(TAG, "emit: 4");
                emitter.onNext(4);
                SystemClock.sleep(1000);

                Log.e(TAG, "complete 1");
                emitter.onComplete();



            }
        }).subscribeOn(Schedulers.io());

        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.e(TAG, "emit: A");
                emitter.onNext("A");
                Thread.sleep(1000);

                Log.e(TAG, "emit: B");
                emitter.onNext("B");
                Thread.sleep(1000);

                Log.e(TAG, "emit: C");
                emitter.onNext("C");
                Thread.sleep(1000);

                Log.e(TAG, "complete 2");
                emitter.onComplete();

            }
        }).subscribeOn(Schedulers.io());

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) {
                return integer + s;
            }
        }).observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(String s) {
                Log.e(TAG, "onNext: " + s);
//                mTvInfo.setText(s);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: ");
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete: ");
            }
        });
    }
}
