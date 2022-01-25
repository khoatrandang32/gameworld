package com.kflower.gameworld.ui.timer

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.kflower.gameworld.R
import com.kflower.gameworld.bottomsheet.BottomSheetTimer
import com.kflower.gameworld.common.core.BaseFragment
import com.kflower.gameworld.databinding.SplashFragmentBinding
import com.kflower.gameworld.databinding.TimerFragmentBinding
import com.kflower.gameworld.dialog.LoadingDialog
import com.kflower.gameworld.ui.main.MainFragment
import com.kflower.gameworld.ui.play.PlayAudioViewModel
import com.kflower.gameworld.ui.splash.SplashFragment
import com.kflower.gameworld.ui.splash.SplashViewModel

class TimerFragment : BaseFragment() {

    companion object {
        fun newInstance() = SplashFragment()
    }

    lateinit var binding: TimerFragmentBinding;
    lateinit var viewModel: TimerViewModel;
    lateinit var bottomSheet: BottomSheetTimer;


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = TimerFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(TimerViewModel::class.java)

        binding.viewModel = viewModel;
        binding.lifecycleOwner= this

        bottomSheet= BottomSheetTimer();

        bottomSheet.show(parentFragmentManager,BottomSheetTimer.TAG)

        binding.btnSetting.setOnClickListener{
            bottomSheet.show(parentFragmentManager,BottomSheetTimer.TAG)
        }

    }


    override fun getLayoutBinding(): ViewDataBinding {
        return binding
    }
}