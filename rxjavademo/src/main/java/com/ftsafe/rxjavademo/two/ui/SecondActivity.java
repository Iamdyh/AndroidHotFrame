package com.ftsafe.rxjavademo.two.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.ftsafe.rxjavademo.BuildConfig;
import com.ftsafe.rxjavademo.R;
import com.ftsafe.rxjavademo.two.entity.LoginRequest;
import com.ftsafe.rxjavademo.two.entity.LoginResponse;
import com.ftsafe.rxjavademo.two.retrofit.Api;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


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
        Api api = retrofit.create(Api.class);
        api.login(new LoginRequest())
                .subscribeOn(Schedulers.io())                     //在io线程中进行网络请求
                .observeOn(AndroidSchedulers.mainThread())        //回到主线程处理请求结果
                .subscribe(new Observer<LoginResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        cd = new CompositeDisposable();
                        cd.add(d);
                    }

                    @Override
                    public void onNext(LoginResponse loginResponse) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "登录失败");
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "登录成功");
                    }
                });


    }

    private static Retrofit create(){
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.connectTimeout(9, TimeUnit.SECONDS);
        if(BuildConfig.DEBUG){
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }
        return new Retrofit.Builder().baseUrl(ENDPOINT)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}
