package com.dyh.rxjavademo.two.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import com.dyh.rxjavademo.R;
import com.dyh.rxjavademo.two.entity.LoginRequest;
import com.dyh.rxjavademo.two.entity.LoginResponse;
import com.dyh.rxjavademo.two.entity.RegisterRequest;
import com.dyh.rxjavademo.two.entity.RegisterResponse;
import com.dyh.rxjavademo.two.retrofit.Api;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "SecondActivity";
    private TextView mTvInfo;
    private static final String ENDPOINT = "https:www.google.com";
    private Retrofit retrofit;
    private CompositeDisposable cd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        mTvInfo = findViewById(R.id.id_tv);
//        useRxJava();

        useRetrofitAndRxJava();
    }

    private void useRxJava(){
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.e(TAG, "Observable thread is: " + Thread.currentThread().getName());
                Log.e(TAG, "subscribe: 1");
                emitter.onNext(1);
            }
        });
        Consumer<Integer> consumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.e(TAG, "Observer thread is: " + Thread.currentThread().getName());
//                mTvInfo.setText("" + integer);
            }
        };
//        observable.subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(consumer);
        observable.subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, " After observeOn(mainThread), current thread is: " + Thread.currentThread().getName());
                        mTvInfo.setText("" + integer);
                    }
                })
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "After observeOn(io), current thread is : " + Thread.currentThread().getName());

                    }
                })
                .subscribe(consumer);
    }

    private void useRetrofitAndRxJava(){
        final Api api = retrofit.create(Api.class);
//        api.login(new LoginRequest())
//                .subscribeOn(Schedulers.io())                     //在io线程中进行网络请求
//                .observeOn(AndroidSchedulers.mainThread())        //回到主线程处理请求结果
//                .subscribe(new Observer<LoginResponse>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        cd = new CompositeDisposable();
//                        cd.add(d);
//                    }
//
//                    @Override
//                    public void onNext(LoginResponse loginResponse) {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e(TAG, "登录失败");
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.e(TAG, "登录成功");
//                    }
//                });
//

        //flatmap
        api.register(new RegisterRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<RegisterResponse>() {
                    @Override
                    public void accept(RegisterResponse registerResponse) throws Exception {
                        Log.e(TAG, "accept: 注册返回的结果");
                    }
                })
                .observeOn(Schedulers.io())                 //回到io线程去进行登录请求
                .flatMap(new Function<RegisterResponse, ObservableSource<LoginResponse>>() {
                    @Override
                    public ObservableSource<LoginResponse> apply(RegisterResponse registerResponse) throws Exception {
                        return api.login(new LoginRequest());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginResponse>() {
                    @Override
                    public void accept(LoginResponse loginResponse) throws Exception {
                        Log.e(TAG, "accept: 登录成功");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "accept: 登录失败");
                    }
                });
    }


}
