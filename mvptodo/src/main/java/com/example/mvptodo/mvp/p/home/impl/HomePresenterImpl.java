package com.example.mvptodo.mvp.p.home.impl;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.mvptodo.bean.TestBean;
import com.example.mvptodo.injector.ContextLife;
import com.example.mvptodo.model.rx.RxTest;
import com.example.mvptodo.mvp.IView;
import com.example.mvptodo.mvp.p.home.IHomePresenter;
import com.example.mvptodo.mvp.v.home.IHomeView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;

/**
 * Created by fan on 2016/2/22.
 */
public class HomePresenterImpl implements IHomePresenter {
    private final Context mContext;
    private final Activity mActivity;
    private RxTest mRxTest;
    private IHomeView mHomeView;
    private static int i = 0;

    @Inject
    public HomePresenterImpl(@ContextLife("Activity") Context context, Activity activity, RxTest rxTest) {
        mContext = context;
        mActivity = activity;
        mRxTest = rxTest;
    }

    @Override
    public void attachView(@NonNull IView iView) {
        mHomeView = (IHomeView) iView;
    }

    @Override
    public void detachView() {

    }

    @Override
    public void sayHello() {
        mRxTest.createTest(Thread.currentThread().getId() + ":张三", Thread.currentThread().getId() + ":消息").
                toList().
                flatMap(Observable::from).distinct(TestBean::getName).
                map(TestBean::toString)
                .toList()
                .flatMap(Observable::from)
                .collect((Func0<ArrayList<String>>) ArrayList::new, ArrayList::add).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(result -> {
                    mHomeView.showHello("fuck:\n" + result.toString());
                });
    }
}
