package com.kflower.gameworld.ui.login

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
import com.kflower.gameworld.databinding.LoginFragmentBinding
import com.kflower.gameworld.dialog.LoadingDialog
import com.kflower.gameworld.ui.login.LoginViewModel


class LoginFragment : BaseFragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    lateinit var binding: LoginFragmentBinding;
    lateinit var viewModel: LoginViewModel;
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

        viewModel = LoginViewModel();
        auth = FirebaseAuth.getInstance();

        binding = LoginFragmentBinding.inflate(layoutInflater)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        binding.logoLayout.layoutParams = params
        binding.layoutContainer.setBackgroundColor(resources.getColor(R.color.main_color));

        binding.viewModel = viewModel;
//        var database = AppDatabaseHelper(context);

        binding.imgGoogle.setOnClickListener {
//            UserTable(null).addNewUser(context, User("Khoadz", "123456"))
//            binding.edtUsername.setError(true);
//            binding.edtPassword.setError(true,"Vl");
            dialog.show()
            signInWithGoogle()
        }


        Handler().postDelayed({
            val currentUser = auth.currentUser
            Log.d("KHOA", "onCreate: $currentUser")
            if (currentUser != null) {
                goToHome()
            } else {
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
//                        imgLogo.setColorFilter(animation.animatedValue as Int)
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

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 0)
        dialog.hide()
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
        dialog.show()
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("KHOA", "signInWithCredential:success")
                    val user = auth.currentUser
//                    updateUI(user)
                    goToHome()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("KHOA", "signInWithCredential:failure", task.exception)
//                    updateUI(null)
                }
                dialog.hide()
            }
    }
    fun goToHome(){
        navigateTo(MainFragment())
    }


}