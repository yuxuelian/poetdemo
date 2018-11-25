package com.kaibo.poet_demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.kaibo.annotations.Builder
import com.kaibo.annotations.Optional
import com.kaibo.annotations.Require
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.nav_header_main.*

/**
 * @author kaibo
 * @date 2018/11/24 23:03
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */

@Builder
class UserActivity : AppCompatActivity() {

    @Require
    lateinit var username: String

    @Require
    lateinit var password: String

    @Optional
    var age: Int = 0

    @Optional
    lateinit var title: String

    @Optional
    lateinit var workSpace: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        usernameTextView.text = username
        passwordTextView.text = password
    }

}
