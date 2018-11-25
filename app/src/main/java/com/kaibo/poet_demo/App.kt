package com.kaibo.poet_demo

import android.app.Application
import com.kaibo.runtime.ActivityBuilder

/**
 * @author kaibo
 * @date 2018/11/25 1:06
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ActivityBuilder.INSTANCE.init(this)
    }

}