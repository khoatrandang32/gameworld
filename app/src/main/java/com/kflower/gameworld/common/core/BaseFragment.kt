package com.kflower.gameworld.common.core

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.kflower.gameworld.R
import com.kflower.gameworld.constants.StatusMode
import com.kflower.gameworld.interfaces.IOnBackPressed


public abstract class BaseFragment : Fragment(), IOnBackPressed {
    lateinit var fgParent: ViewGroup;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fgParent = container!!;
        return getLayoutBinding().root
    }

    protected fun navigateTo(newFragment: Fragment) {
        if (fgParent != null) {
            val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
            ft.setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out,
            )
            ft.replace(fgParent.id, newFragment)
            ft.addToBackStack(null)
            ft.commit()
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


}