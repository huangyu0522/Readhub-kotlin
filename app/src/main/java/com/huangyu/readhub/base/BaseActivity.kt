package com.huangyu.readhub.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.huangyu.readhub.mvp.IView

/**
 * Created by huangyu on 2018/3/28.
 */
abstract class BaseActivity : AppCompatActivity(), IView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initView()
        initListeners()
    }

    abstract fun getLayoutId(): Int

    abstract fun initView()

    abstract fun initListeners()

}