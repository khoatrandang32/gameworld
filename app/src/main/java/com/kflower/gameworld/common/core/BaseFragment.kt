package com.kflower.gameworld.common.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

public abstract class BaseFragment : Fragment() {
    lateinit var fgParent:ViewGroup;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fgParent= container!!;
        return getLayoutBinding().root
    }

    protected fun navigateTo(newFragment: Fragment) {
        if(fgParent!=null){
            val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
            ft.replace(fgParent.id, newFragment)
            ft.addToBackStack(null)
           ft.commit()
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    protected abstract fun getLayoutBinding():ViewDataBinding;

}