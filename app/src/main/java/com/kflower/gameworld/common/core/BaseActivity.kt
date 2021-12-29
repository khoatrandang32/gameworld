package com.kflower.gameworld.common.core

import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kflower.gameworld.R
import com.kflower.gameworld.databinding.BaseActivityBinding
import androidx.navigation.NavOptions
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

    fun navigate(newFragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.container, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    protected abstract fun setDefaultFragment(): Fragment

    override fun onBackPressed() {
        val fragment =
            supportFragmentManager.findFragmentById(R.id.container)
        (fragment as? IOnBackPressed)?.onBackPressed()
    }
}