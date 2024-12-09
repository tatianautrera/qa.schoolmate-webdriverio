package com.fsacchi.schoolmate.core.platform

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.fsacchi.schoolmate.R
import com.fsacchi.schoolmate.core.components.CustomToolbar

abstract class BaseActivity<B : ViewDataBinding> : AppCompatActivity() {

    abstract val layoutRes: Int
    lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutRes)

        findViewById<Toolbar>(R.id.toolbar)?.let {
            bindToolbar(it)
        }

        init()
    }

    fun bindToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    abstract fun init()
}
