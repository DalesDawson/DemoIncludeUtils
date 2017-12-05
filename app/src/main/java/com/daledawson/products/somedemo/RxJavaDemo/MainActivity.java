package com.daledawson.products.somedemo.RxJavaDemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.daledawson.products.somedemo.R;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observables.GroupedObservable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //建立连接
//        observable.subscribe(observer);
        /**
         * 变换操作符map、flatMap、concatMap、switchMap、
         * flatMapIterable、buffer、groupBy、cast、window、scan
         */
        /**
         * interal创建一个按固定时间间隔发射整数序列的Observable,相当于定时器
         */
        Observable.interval(3, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.d(TAG, "interval:" + aLong.intValue());
            }
        });
        /**
         * 创建发射指定范围的整数序列的Observable,可以拿来替代for循环，
         * 发射一个范围内的有序整数序列。第一个参数是起始值，并且不小于0；
         * 第二个参数为整数序列的个数
         */
        Observable.range(0, 5).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "range:" + integer.intValue());
            }
        });

        /**
         * 创建一个N次重复发射特定数据的Observable
         */
        Observable.range(0, 3).repeat(2).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "repeat:" + integer.intValue());
            }
        });
        /**
         * 交换操作符map的使用
         */
        final String Host = "http://www.baidu.com/";
        Observable.just("hao123").map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                return Host + s;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "map:" + s);
            }
        });
        /**
         * flatMap、cast的使用
         */
        final String Host1 = "http://www.baidu.com/";
        final List<String> mlist = new ArrayList<>();
        mlist.add("hao123");
        mlist.add("hao1234");
        mlist.add("hao12345");
        mlist.add("hao123456");
        Observable.fromIterable(mlist).flatMap(new Function<String, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(String s) throws Exception {
                return Observable.just(Host1 + s);
            }
        }).cast(String.class).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "flatMap:" + s);
            }
        });

        /**
         * flatMapIterable操作符可以将数据包装成Iterable
         */
        Observable.just(1, 2, 3).flatMapIterable(new Function<Integer, Iterable<Integer>>() {
            @Override
            public Iterable<Integer> apply(Integer integer) throws Exception {
                List<Integer> mlist = new ArrayList<Integer>();
                mlist.add(integer + 1);
                return mlist;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "flatMapIterable:" + integer);
            }
        });
        /**
         * buffer操作符将Observable变换为一个新的Observable，
         * 这个新的Observable每次发射一组列表值而不是一个一个发射。
         * window操作符和buffer类似，不过window操作符发射的是Observable而不是数据列表
         */
        Observable.just(1, 2, 3, 4, 5, 6).buffer(3).subscribe(new Consumer<List<Integer>>() {
            @Override
            public void accept(List<Integer> integers) throws Exception {
                for (Integer i : integers) {
                    Log.d(TAG, "buffer:" + integers);
                }
                Log.d(TAG, "-------------------");
            }
        });
        /**
         * groupBy操作符用于分组元素，将源Observable变换成一个发射Observable的新Observable（分组后的）
         * 它们中的每一个新的Observable都发射一组指定的数据
         */
        Swordsman s1 = new Swordsman("韦一笑", "A");
        Swordsman s2 = new Swordsman("张三丰", "SS");
        Swordsman s3 = new Swordsman("周芷若", "S");
        Swordsman s4 = new Swordsman("宋远桥", "S");
        Swordsman s5 = new Swordsman("殷梨亭", "A");
        Swordsman s6 = new Swordsman("张无忌", "SS");
        Swordsman s7 = new Swordsman("鹤笔翁", "S");
        Swordsman s8 = new Swordsman("宋青书", "A");
        Observable<GroupedObservable<String, Swordsman>> GroupedObservable = Observable.just(s1, s2, s3, s4, s5, s6, s7, s8)
                .groupBy(new Function<Swordsman, String>() {
                    @Override
                    public String apply(Swordsman swordsman) throws Exception {
                        return swordsman.getRank();
                    }
                });
        Observable.concat(GroupedObservable).subscribe(new Consumer<Swordsman>() {
            @Override
            public void accept(Swordsman swordsman) throws Exception {
                Log.d(TAG, "groupBy:" + swordsman.getName() + "---" + swordsman.getRank());
            }
        });
        /**
         * 过滤操作符filter、elementAt、distinct、skip、take、skipLast、ignoreElements、
         * throttleFirst、sample、debounce、throttleWithTimeout
         */
        /**
         * filter操作符是对源Observable产生的结果自定义规则进行筛选，只有满足条件的结果才会提交给订阅者
         */
        Observable.just(1, 2, 3, 4).filter(new Predicate<Integer>() {

            @Override
            public boolean test(Integer integer) throws Exception {
                return integer > 2;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "filter:" + integer);
            }
        });
        /**
         * elementAt操作符用来返回指定位置的数据
         */
        Observable.just(1, 2, 3, 4).elementAt(2).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "elementAt:" + integer);
            }
        });
        /**
         * distinct操作符用来去重，其只允许还没有发射过的数据项通过。
         * 和它类似的还有distinctUntilChanged操作符，它用来去掉连续重复的数据
         */
        Observable.just(1, 2, 2, 3, 4, 1, 2, 3).distinct().subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "distinct:" + integer);
            }
        });
        /**
         * skip操作符将源Observable 发射的数据过滤掉钱n项，而take操作符则只取前n项；
         * 还有skipLast和takeLast操作符，则是从后面进行过滤操作
         */
        Observable.just(1, 2, 3, 4, 5, 6).skip(2).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "skip:" + integer);
            }
        });
        Observable.just(1, 2, 3, 4, 5, 6).take(2).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "take:" + integer);
            }
        });
        Observable.just(1, 2, 3, 4, 5, 6).skipLast(2).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "skipLast:" + integer);
            }
        });
        Observable.just(1, 2, 3, 4, 5, 6).takeLast(2).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "takeLast:" + integer);
            }
        });
        /**
         * ignoreElements操作符忽略所有源Observable产生的结果，只把Observable的onCompleted和onError时间通知给订阅者
         */
        Observable.just(1, 2, 3, 4).ignoreElements().subscribe(new TestObserver() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        });
        /**
         * throttleFirst
         * throttleFirst操作符则会定期发射这个时间段里源Observable发射的第一个数据，
         * throttleFirst操作符默认在computation调度器上执行。和throttleFirst操作符类似的还有sample操作符，
         * 它会定时地发射源Observable最近发射的数据，其他的都会被过滤掉。
         */
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> inte) throws Exception {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                subscriber.onComplete();
            }
        }).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "throttleFirst:" + integer);
            }
        });
        /**
         * throttleWithTimeOut
         *通过时间来限流。源Observable每次发射出来一个数据后就会进行计时。
         * 如果在设定好的时间结束前Observable有新的数据发射出来，这个数据
         * 就会被丢弃，同时throttleWithTimeOut重新开始计时。
         * 如果每次都是在计时结束前发射数据，那么这个限流就会走向极端：
         * 只会发射发射最后一个数据。其默认在computation调度器上执行。
         * 和throttleWithTimeOut操作符类似的有deounce操作符，它不仅可以使用时间来进行过滤，
         * 还可以根据一个函数来进行限流。
         */
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; i < 10; i++) {
                    subscriber.onNext(i);
                    int sleep = 100;
                    if (i % 3 == 0) {
                        sleep = 300;
                    }
                    try {
                        Thread.sleep(sleep);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                subscriber.onComplete();
            }
        }).throttleWithTimeout(200, TimeUnit.MILLISECONDS).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "throttleWithTimeOut:" + integer);
            }
        });
        /**
         * 组合操作符
         * 组合操作符可以同时处理多个Observable来创建我们所需要的Observable。
         * 组合操作符有startWith、merge、zip、combineLastest、join、switch等。
         */
        /**
         * startWith
         */
        Observable.just(3, 4, 5).startWith(2).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "startWith:" + integer);
            }
        });
        /**
         * merge操作符将多个Observable合并到一个Observable中进行发射，merge可能会让合并的Observable发射的数据交错。
         */
        Observable<Integer> obs1 = Observable.just(1, 2, 3).subscribeOn(Schedulers.io());
        Observable<Integer> obs2 = Observable.just(4, 5, 6);
        Observable.merge(obs1, obs2).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "merge:" + integer);
            }
        });
        /**
         *concat
         *将多个Observable发射的数据进行合并发射。concat严格按照顺序发射数据，
         * 前一个Observable没发射完成是不会发射后一个Observable的数据的。
         */
        Observable<Integer> obs11 = Observable.just(1, 2, 3).subscribeOn(Schedulers.io());
        Observable<Integer> obs22 = Observable.just(4, 5, 6);
        Observable.concat(obs11, obs22).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "concat:" + integer);
            }
        });
        /**
         * zip
         * 合并两个或者多个Observable发射出的数据项，根据指定的函数变换它们，并发射一个新只值。
         */
        Observable<Integer> obs111 = Observable.just(1, 2, 3);
        Observable<String> obs222 = Observable.just("a", "b", "c");
        Observable.zip(obs111, obs222, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "zip:" + s);
            }
        });
        /**
         * combineLatest
         * 将两个Observable中的任何一个发射了数据时，使用一个函数结合每个Observable发射的最近数据项，
         * 并且基于这个函数的结果发射数据。combineLatest操作符和zip有些类似。只不过zip操作符作用于
         * 最近未打开的两个Observable，只有当原始的Observable中的每一个都发射了一条数据时zip才发射数据；
         * 而combineLatest操作符作用于最近发射的数据项，在原始Observable中的任意一个发射了数据时发射一条数据。
         */
        Observable<Integer> obs1111 = Observable.just(1, 2, 3);
        Observable<String> obs2222 = Observable.just("a", "b", "c");
        Observable.combineLatest(obs1111, obs2222, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "combineLatest:" + s);
            }
        });
        /**
         * 辅助操作符
         * 辅助操作符可以帮助我们更加方便地处理Observable。包括delay、Do、subscribeOn、
         * observeOn、timeout、materialize、dematerialize、timeInterval、timestamp、to
         */
        /**
         * delay
         * 让原始的Observable在发射每一项数据之前都暂停一段指定的时间段
         */
//        Observable.create(new ObservableOnSubscribe<Long>() {
//            @Override
//            public void subscribe(ObservableEmitter<Long> e) throws Exception {
//                Long currentTime = System.currentTimeMillis() / 1000;
//                subscriber.onNext(currentTime);
//            }
//        }).delay(2, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
//            @Override
//            public void accept(Long aLong) throws Exception {
//                Log.d(TAG, "delay:" + (System.currentTimeMillis() / 1000 - aLong));
//            }
//        });

//        observable.subscribe(observer);
//        Observable.create(new ObservableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
//                emitter.onNext(1);
//                emitter.onNext(2);
//                emitter.onNext(3);
//                emitter.onComplete();
//            }
//        }).subscribe(new Observer<Integer>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                Log.d(TAG, "subscribe");
//            }
//
//            @Override
//            public void onNext(Integer value) {
//                Log.d(TAG, "" + value);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.d(TAG, "error");
//            }
//
//            @Override
//            public void onComplete() {
//                Log.d(TAG, "complete");
//            }
//        });

    }


    //创建一个上游 Observable：
    Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
        @Override
        public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
            emitter.onNext(1);
            emitter.onNext(2);
            emitter.onNext(3);
            emitter.onComplete();
        }
    });
    Subscriber<Integer> subscriber = new Subscriber<Integer>() {
        @Override
        public void onSubscribe(Subscription s) {
            Log.d(TAG, "subscribe");
        }

        @Override
        public void onNext(Integer integer) {
            Log.d(TAG, "" + integer);
        }

        @Override
        public void onError(Throwable t) {
            Log.d(TAG, "error");
        }

        @Override
        public void onComplete() {
            Log.d(TAG, "complete");
        }
    };

    //创建一个下游 Observer
    Observer<Integer> observer = new Observer<Integer>() {
        @Override
        public void onSubscribe(Disposable d) {
            Log.d(TAG, "subscribe");
        }

        @Override
        public void onNext(Integer value) {
            Log.d(TAG, "" + value);
        }

        @Override
        public void onError(Throwable e) {
            Log.d(TAG, "error");
        }

        @Override
        public void onComplete() {
            Log.d(TAG, "complete");
        }
    };

}

