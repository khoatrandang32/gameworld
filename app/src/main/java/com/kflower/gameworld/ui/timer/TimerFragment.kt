package com.kflower.gameworld.ui.timer

import android.content.DialogInterface
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.kflower.gameworld.MyApplication
import com.kflower.gameworld.MyApplication.Companion.TAG
import com.kflower.gameworld.MyApplication.Companion.stopTimer
import com.kflower.gameworld.MyApplication.Companion.timeCountDown
import com.kflower.gameworld.R
import com.kflower.gameworld.bottomsheet.BottomSheetTimer
import com.kflower.gameworld.common.core.BaseFragment
import com.kflower.gameworld.databinding.SplashFragmentBinding
import com.kflower.gameworld.databinding.TimerFragmentBinding
import com.kflower.gameworld.dialog.LoadingDialog
import com.kflower.gameworld.interfaces.BottomSheetListener
import com.kflower.gameworld.interfaces.TimerChange
import com.kflower.gameworld.ui.main.MainFragment
import com.kflower.gameworld.ui.play.PlayAudioViewModel
import com.kflower.gameworld.ui.splash.SplashFragment
import com.kflower.gameworld.ui.splash.SplashViewModel

class TimerFragment : BaseFragment() {

    companion object {
        fun newInstance() = TimerFragment()
    }

    lateinit var binding: TimerFragmentBinding;
    lateinit var viewModel: TimerViewModel;
    lateinit var bottomSheet: BottomSheetTimer;


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = TimerFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(TimerViewModel::class.java)

        binding.viewModel = viewModel;
        binding.lifecycleOwner = this

        bottomSheet = BottomSheetTimer(object :BottomSheetListener{
            override fun onCancel() {
                onBackPressed()
            }
        });

        if (timeCountDown == 0L) {
            bottomSheet.show(parentFragmentManager, BottomSheetTimer.TAG)
        }

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnSetting.setOnClickListener {
            bottomSheet.show(parentFragmentManager, BottomSheetTimer.TAG)
            stopTimer()
            MyApplication.curTimer.postValue(0)

        }

        MyApplication.curTimer.observe(this, {
            binding.progressBar.max = Integer.parseInt(timeCountDown.toString())
            binding.progressBar.progress = Integer.parseInt(it.toString())
            binding.txtTime.text = millisecondsToTime(it)
        })

//        MyApplication.listenerTimer= object :TimerChange{
//            override fun onTimerChange(time: Long) {
//                binding.progressBar.max=  Integer.parseInt(timeCountDown.toString())
//                binding.progressBar.progress= Integer.parseInt(time.toString())
//                binding.txtTime.text = millisecondsToTime(time)
//            }
//
//        }

    }

    private fun millisecondsToTime(milliseconds: Long): String {
        val minutes = milliseconds / 1000 / 60
        val seconds = milliseconds / 1000 % 60
        val secondsStr = seconds.toString()
        val secs: String = if (secondsStr.length >= 2) {
            secondsStr.substring(0, 2)
        } else {
            "0$secondsStr"
        }
        var minStr = if (minutes < 10) "0$minutes" else minutes
        return "$minStr:$secs"
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun getLayoutBinding(): ViewDataBinding {
        return binding
    }
}