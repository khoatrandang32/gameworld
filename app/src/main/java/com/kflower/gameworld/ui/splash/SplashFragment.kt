package com.kflower.gameworld.ui.splash

import android.os.Bundle
import android.os.Handler
import com.kflower.gameworld.common.core.BaseFragment
import com.kflower.gameworld.ui.main.MainFragment

import android.widget.LinearLayout
import androidx.databinding.ViewDataBinding
import androidx.transition.TransitionManager
import com.kflower.gameworld.databinding.SplashFragmentBinding
import androidx.transition.ArcMotion
import androidx.transition.ChangeBounds
import android.animation.ArgbEvaluator
import android.content.res.Resources

import android.animation.ValueAnimator

import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Intent
import com.kflower.gameworld.R

import android.util.Log

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kflower.gameworld.dialog.LoadingDialog


class SplashFragment : BaseFragment() {

    companion object {
        fun newInstance() = SplashFragment()
    }

    lateinit var binding: SplashFragmentBinding;
    lateinit var viewModel: SplashViewModel;
    lateinit var googleSignInClient: GoogleSignInClient;
    lateinit var auth: FirebaseAuth;
    lateinit var dialog: LoadingDialog;

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        dialog = LoadingDialog(requireContext());
        googleSignInClient = GoogleSignIn.getClient(context, gso)

        viewModel = SplashViewModel();
        auth = FirebaseAuth.getInstance();

        binding = SplashFragmentBinding.inflate(layoutInflater)

        binding.viewModel = viewModel;

        Handler().postDelayed({
//            finish()
            navigateTo( MainFragment.newInstance())

        }, 3000)

    }

    override fun getLayoutBinding(): ViewDataBinding {
        return binding
    }
}