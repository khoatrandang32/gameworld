package com.kflower.gameworld.common.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kflower.gameworld.R
import com.kflower.gameworld.databinding.BaseActivityBinding
import androidx.navigation.NavOptions




abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = BaseActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, setDefaultFragment())
                .commitNow()
        }


    }

    fun navigate(newFragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.container, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onBackPressed() {
        supportFragmentManager.popBackStack()
    }

    protected abstract fun setDefaultFragment(): Fragment
}