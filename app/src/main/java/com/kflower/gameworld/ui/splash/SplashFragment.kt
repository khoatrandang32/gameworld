package com.kflower.gameworld.ui.splash

import android.R.attr
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import com.kflower.gameworld.common.core.BaseFragment
import com.kflower.gameworld.ui.main.MainFragment
import android.R.attr.button

import android.view.Gravity

import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil.setContentView
import androidx.databinding.ViewDataBinding
import androidx.transition.TransitionManager
import com.kflower.gameworld.databinding.SplashFragmentBinding
import android.R.attr.button
import androidx.transition.ArcMotion
import androidx.transition.ChangeBounds
import android.R.attr.button
import android.R.attr.button
import android.animation.ArgbEvaluator
import android.content.res.Resources
import android.view.View
import android.util.DisplayMetrics
import android.text.Editable

import android.text.TextWatcher
import androidx.constraintlayout.widget.ConstraintLayout
import android.animation.ValueAnimator

import android.animation.ValueAnimator.AnimatorUpdateListener
import android.app.Activity
import android.content.Intent
import android.graphics.ColorFilter
import com.kflower.gameworld.R
import androidx.core.graphics.drawable.DrawableCompat

import android.graphics.drawable.Drawable
import android.util.Log

import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.ktx.Firebase
import com.kflower.gameworld.User
import com.kflower.gameworld.common.components.AppEditText
import com.kflower.gameworld.database.AppDatabaseHelper
import com.kflower.gameworld.database.UserTable
import kotlin.math.log


class SplashFragment : BaseFragment() {

    companion object {
        fun newInstance() = SplashFragment()
    }

    lateinit var binding: SplashFragmentBinding;
    lateinit var viewModel: SplashViewModel;
    lateinit var googleSignInClient: GoogleSignInClient;
    lateinit var auth: FirebaseAuth;


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(context, gso)

        viewModel = SplashViewModel();
        auth = FirebaseAuth.getInstance();

        binding = SplashFragmentBinding.inflate(layoutInflater)
        binding.viewModel = viewModel;
//        var database = AppDatabaseHelper(context);

        binding.loginBtn.setOnClickListener {
//            UserTable(null).addNewUser(context, User("Khoadz", "123456"))
//            binding.edtUsername.setError(true);
//            binding.edtPassword.setError(true,"Vl");
            signIn()
        }


        Handler().postDelayed({
            val currentUser = auth.currentUser
            Log.d("KHOA", "onCreate: $currentUser")
            if(currentUser != null){
               navigateTo(MainFragment())
            }
            else{
                val changeBounds = ChangeBounds();
                changeBounds.duration = 500;
                changeBounds.setPathMotion(ArcMotion())
                TransitionManager.beginDelayedTransition(
                    binding.layoutContainer,
                    changeBounds
                )
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    height = Resources.getSystem().displayMetrics.heightPixels / 3
                }
                binding.logoLayout.layoutParams = params
                val mainColor = resources.getColor(R.color.main_color)
                val bgColor = resources.getColor(R.color.white)
                updateColor(mainColor, bgColor) { animation ->
                    binding.layoutContainer.setBackgroundColor(animation.animatedValue as Int)
                };

                viewModel.username.observe(this, {

                })

                updateColor(bgColor, mainColor) { animation ->
                    binding.apply {
                        imgLogo.setColorFilter(animation.animatedValue as Int)
                        txtName.setTextColor(animation.animatedValue as Int)
                        txtSlogan.setTextColor(animation.animatedValue as Int)
                        binding.edtUsername.setEdtText("");
                        binding.edtPassword.setEdtText("");

                    }

                };
            }

        }, 2000)
//        Handler().postDelayed({
//
//        },2100)

    }

    private fun updateColor(colorFrom: Int, colorTo: Int, listener: AnimatorUpdateListener) {
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = 600 // milliseconds
//        colorAnimation.addUpdateListener { animator -> binding.layoutContainer.setBackgroundColor(animator.animatedValue as Int) }
        colorAnimation.addUpdateListener(listener);
        colorAnimation.start()
    }


    override fun getLayoutBinding(): ViewDataBinding {
        return binding
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("KHOA", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("KHOA", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("KHOA", "signInWithCredential:success")
                    val user = auth.currentUser
//                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("KHOA", "signInWithCredential:failure", task.exception)
//                    updateUI(null)
                }
            }
    }


}