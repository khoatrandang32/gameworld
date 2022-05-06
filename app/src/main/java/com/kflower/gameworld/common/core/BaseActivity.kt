package com.kflower.gameworld.common.core

import android.app.Activity
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kflower.gameworld.R
import com.kflower.gameworld.databinding.BaseActivityBinding
import androidx.navigation.NavOptions
import com.kflower.gameworld.MyApplication.Companion.TAG
import com.kflower.gameworld.MyApplication.Companion.mAppContext
import com.kflower.gameworld.interfaces.IOnBackPressed


abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = BaseActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, setDefaultFragment())
                .commitNow()
        }

        val rectangle = Rect()
        val window: Window = window
        window.decorView.getWindowVisibleDisplayFrame(rectangle)
        val statusBarHeight: Int = rectangle.top

    }


    protected abstract fun setDefaultFragment(): Fragment

    override fun onBackPressed() {
        Log.d(TAG, "onBackPressed: ")
        val fragment =
            supportFragmentManager.findFragmentById(R.id.container)
        (fragment as? IOnBackPressed)?.onBackPressed()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean   {
        Log.d(TAG, "onKeyUp: "+keyCode)
//        if (keyCode === KeyEvent.KEYCODE_BACK) {
//            val fragment =
//                supportFragmentManager.findFragmentById(R.id.container)
//            (fragment as? IOnBackPressed)?.onBackPressed()
//        }
        return super.onKeyUp(keyCode, event)


    }

}