package com.kflower.gameworld.common.core

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.kflower.gameworld.MyApplication
import com.kflower.gameworld.R
import com.kflower.gameworld.constants.StatusMode
import com.kflower.gameworld.interfaces.IOnBackPressed
import android.view.ViewGroup





public abstract class BaseFragment : Fragment(), IOnBackPressed {
    private var fgParent: ViewGroup? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fgParent = container!!;
        var view = getLayoutBinding().root
        setupUI(view)
        return view
    }

    fun navigateTo(newFragment: Fragment) {
        if (fgParent != null) {
            val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
            ft.setCustomAnimations(
                R.anim.slide_in,
                R.anim.slide_out,
                R.anim.slide_left,
                R.anim.slide_right,
            )
            ft.replace(fgParent!!.id, newFragment)
            ft.addToBackStack(null)
            ft.commit()
        }

    }

    fun parentNavigateTo(newFragment: Fragment) {
        if (fgParent != null) {
            val ft: FragmentTransaction? = parentFragment?.parentFragmentManager?.beginTransaction()
            ft?.setCustomAnimations(
                R.anim.slide_in,
                R.anim.slide_out,
                R.anim.slide_left,
                R.anim.slide_right,
            )

            parentFragment?.let { ft?.replace(it.id, newFragment) }
            ft?.addToBackStack(null)
            ft?.commit()
        }

    }

    protected fun finish() {
        parentFragmentManager.popBackStack();
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    protected abstract fun getLayoutBinding(): ViewDataBinding;

    override fun onBackPressed() {
        parentFragmentManager.popBackStack()
    }

    fun setStatusBarColor() {
        var window: Window = requireActivity().window;
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.main_color)
    }

    fun setStatusBarMode(mode: StatusMode) {
        val decorView: View = requireActivity().window.decorView;

        when (mode) {
            StatusMode.DarkMode -> {
                decorView.systemUiVisibility =
                    decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

            }
            StatusMode.LightMode -> {
                decorView.systemUiVisibility =
                    decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()

            }
        }

    }

    fun setupUI(view: View) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
                hideSoftKeyboard(MyApplication.mAppContext as Activity)
                clearForm(view)
                false
            }
        }

        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupUI(innerView)
            }
        }
    }

    fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager: InputMethodManager = activity.getSystemService(
            AppCompatActivity.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        if (inputMethodManager.isAcceptingText) {
            inputMethodManager.hideSoftInputFromWindow(
                activity.currentFocus?.windowToken,
                0
            )
        }
    }

     fun clearForm(view : View) {
         if(view is ViewGroup){
             var i = 0
             val count = view.childCount
             while (i < count) {
                 val view = view.getChildAt(i)
                 if (view is EditText) {
                     view.clearFocus()
                 }
                 if (view is ViewGroup && (view).childCount > 0) clearForm(view)
                 ++i
             }
         }

    }


}