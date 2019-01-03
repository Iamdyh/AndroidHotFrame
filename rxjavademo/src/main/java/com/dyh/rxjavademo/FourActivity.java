package com.dyh.rxjavademo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


public class FourActivity extends AppCompatActivity {

    private static final String TAG = "FourActivity";
    private Button mBtn1;
    private Button mBtn2;
    private TextView mTvInfo;
    private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four);
        mBtn1 = findViewById(R.id.id_btn_1);
        mBtn2 = findViewById(R.id.id_btn_2);
        mTvInfo = findViewById(R.id.id_tv);

//        useZip();

//        useOneThread();

//        useFilter();

//        useSimple();

//        useSleep();

//        useFlowable();

//        useFlowableWithoutRequest();

//        useFlowableWithoutRequestInTwoThread();


        mBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                userFlowableWithRequestInTwoThread();

                useFlowableRequest();
            }
        });
        mBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                request(1);

                request(96);
            }
        });

//        useBackpressureStrategyBUFFER();

//        useFlowableInterval();

//        useFlowableEmitter();


    }

    private void useZip(){
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for(int i = 0; ; i++){
                    emitter.onNext(i);         //无限循环发事件
                }
            }
        }).subscribeOn(Schedulers.io());

        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("A");
            }
        }).subscribeOn(Schedulers.io());

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) {
                return integer + s;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.e(TAG, "accept: " + s);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e(TAG, "accept: " + throwable);
            }
        });
    }

    private void useOneThread(){
//        Observable.create(new ObservableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(ObservableEmitter<Integer> emitter) {
//                for(int i = 0; ; i++){
//                    emitter.onNext(i);         //无限循环发事件
//                }
//            }
//        }).subscribe(new Consumer<Integer>() {
//            @Override
//            public void accept(Integer integer) throws Exception {
//                Thread.sleep(2000);
//                Log.e(TAG, "accept: " + integer);
//            }
//        });

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) {
                for(int i = 0; ; i++){
                    emitter.onNext(i);         //无限循环发事件
                }
            }
        }).subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Thread.sleep(2000);
                Log.e(TAG, "accept: " + integer);
            }
        });
    }

    private void useFilter(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) {
                for(int i = 0; ; i++){
                    emitter.onNext(i);       //无限循环发事件
                }
            }
        }).subscribeOn(Schedulers.io())
          .filter(new Predicate<Integer>() {
              @Override
              public boolean test(Integer integer) throws Exception {
                  return integer % 10 == 0;         //只有被10整除的数才能往下发
              }
          })
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Consumer<Integer>() {
              @Override
              public void accept(Integer integer) throws Exception {
                  Log.e(TAG, "accept: " + integer);
              }
          });
    }
    
    private void useSimple(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) {
                for(int i = 0; ; i++){
                    emitter.onNext(i);       //无限循环发事件
                }
            }
        }).subscribeOn(Schedulers.io())
          .sample(2, TimeUnit.SECONDS) //每隔2秒发一次
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Consumer<Integer>() {
              @Override
              public void accept(Integer integer) throws Exception {
                  Log.e(TAG, "accept: " + integer);
              }
          });
    }

    private void useSleep(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception{
                for(int i = 0; ; i++){
                    emitter.onNext(i);       //无限循环发事件
                    Thread.sleep(2000);  //每次发送完事件延时2秒
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "accept: " + integer);
                    }
                });
    }

    private void useFlowable(){
        Flowable<Integer> upstream = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                Log.e(TAG, "emit: 1");
                emitter.onNext(1);
                Log.e(TAG, "emit: 2");
                emitter.onNext(2);
                Log.e(TAG, "emit: 3");
                emitter.onNext(3);
                Log.e(TAG, "emit complete");
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR);

        Subscriber<Integer> downStream = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.e(TAG, "onSubscribe: " );
                s.request(Long.MAX_VALUE);
            }



            @Override
            public void onNext(Integer integer) {
                Log.e(TAG, "onNext: " + integer);
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "onError: ");
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete: ");
            }
        };


        upstream.subscribe(downStream);
    }

    private void useFlowableWithoutRequest(){
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) {
                Log.e(TAG, "emit: 1");
                emitter.onNext(1);
                Log.e(TAG, "emit: 2");
                emitter.onNext(2);
                Log.e(TAG, "emit: 3");
                emitter.onNext(3);
                Log.e(TAG, "emit complete");
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR).subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.e(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(Integer integer) {
                Log.e(TAG, "onNext: ");
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "onError: ");
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete: ");
            }
        });
    }

    private void useFlowableWithoutRequestInTwoThread(){
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) {
                Log.e(TAG, "emit: 1");
                emitter.onNext(1);
                Log.e(TAG, "emit: 2");
                emitter.onNext(2);
                Log.e(TAG, "emit: 3");
                emitter.onNext(3);
                Log.e(TAG, "emit complete");
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.e(TAG, "onSubscribe: ");
                mSubscription = s;
            }

            @Override
            public void onNext(Integer integer) {
                Log.e(TAG, "onNext: " + integer);
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "onError: " + t);
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete: ");
            }
        });
    }

    private void userFlowableWithRequestInTwoThread(){
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) {
                Log.e(TAG, "emit: 1");
                emitter.onNext(1);
                Log.e(TAG, "emit: 2");
                emitter.onNext(2);
                Log.e(TAG, "emit: 3");
                emitter.onNext(3);
                Log.e(TAG, "emit complete");
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.e(TAG, "onSubscribe: ");
                        mSubscription = s;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e(TAG, "onNext: " + integer);
                        mTvInfo.setText(integer + "");
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e(TAG, "onError: " + t);
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete: ");
                    }
                });
    }

    private void request(long n){
        if(mSubscription != null){
            mSubscription.request(n); //在外部调用request请求
        }

    }

    private void useBackpressureStrategyBUFFER(){
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) {
                for(int i = 0; i < 1000; i++){
                    emitter.onNext(i);
                    Log.e(TAG, "emit: " + i);
                }
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.e(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(Integer integer) {
                Log.e(TAG, "onNext: " + integer);
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "onError: ");
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete: ");
            }
        });
    }

    private void useFlowableInterval(){
         Flowable.interval(1, TimeUnit.SECONDS)
                 .onBackpressureDrop()                    //加上背压策略
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(new Subscriber<Long>() {
                     @Override
                     public void onSubscribe(Subscription s) {
                         Log.e(TAG, "onSubscribe: ");
                         mSubscription = s;
                         s.request(Long.MAX_VALUE);
                     }

                     @Override
                     public void onNext(Long aLong) {
                         Log.e(TAG, "onNext: " + aLong);
                         try {
                             Thread.sleep(1000);
                         }catch (Exception e){
                             e.printStackTrace();
                         }
                     }

                     @Override
                     public void onError(Throwable t) {
                         Log.e(TAG, "onError: " + t);
                     }

                     @Override
                     public void onComplete() {
                         Log.e(TAG, "onComplete: ");
                     }
                 });
    }

    private void useFlowableEmitter(){
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) {
                Log.e(TAG, "current requested: " + emitter.requested());
            }
        }, BackpressureStrategy.ERROR)
        .subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.e(TAG, "onSubscribe: ");
                mSubscription = s;
                s.request(10);
                s.request(100);
            }

            @Override
            public void onNext(Integer integer) {
                Log.e(TAG, "onNext: ");
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "onError: ");
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete: ");
            }
        });
    }

    private void useFlowableRequest(){
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception{
                Log.e(TAG, "subscribe: ");
                boolean flag;
                for(int i = 0; ; i++){
                    flag = false;
                    while(emitter.requested() == 0){
                        Log.e(TAG, "no, I can't emit value!");
                        flag = true;
                    }
                    emitter.onNext(i);
                    Log.e(TAG, "emit " + ", requested = " + emitter.requested());
                }
            }
        }, BackpressureStrategy.ERROR)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.e(TAG, "onSubscribe: ");
                mSubscription = s;
            }

            @Override
            public void onNext(Integer integer) {
                Log.e(TAG, "onNext: " + integer);
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "onError: " + t);
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete: ");
            }
        });
    }
}
