package com.kflower.gameworld.ui.main.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.window.SplashScreen
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kflower.gameworld.R
import com.kflower.gameworld.adapter.AudioAdapter
import com.kflower.gameworld.common.core.BaseFragment
import com.kflower.gameworld.databinding.HomeFragmentBinding
import com.kflower.gameworld.adapter.AudioGroupAdapter
import com.kflower.gameworld.common.Key
import com.kflower.gameworld.dialog.AppDrawer
import com.kflower.gameworld.interfaces.OnClickAudioBook
import com.kflower.gameworld.model.AudioBook
import com.kflower.gameworld.model.AudioGroup
import com.kflower.gameworld.model.Category
import com.kflower.gameworld.ui.audioDetail.AudioDetailFragment
import com.kflower.gameworld.ui.main.MainFragment
import com.kflower.gameworld.ui.splash.SplashFragment
import java.lang.reflect.Type


class HomeFragment() : BaseFragment() {

    private lateinit var viewModel: HomeViewModel

    lateinit var binding: HomeFragmentBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = HomeFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding.viewModel= viewModel;
        binding.lifecycleOwner= this;

//        initPreData();

        //
        viewModel.getAudioList();
        //
        viewModel.listAudioGroup.observe(this, {
            binding.apply {
                lvGroupAudios.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
                lvGroupAudios.adapter =
                    AudioGroupAdapter(requireContext(), it, object : OnClickAudioBook {
                        override fun onClick(item: AudioBook) {
                            parentNavigateTo(AudioDetailFragment(item))
                        }
                    });
            }
            val sharedPref: SharedPreferences = requireContext().getSharedPreferences(
                Key.KEY_STORE,
                Context.MODE_PRIVATE
            )
            val gson = Gson()
            val listAudioGroups = gson.toJson(it)
            val editor = sharedPref.edit()
            editor.putString(Key.KEY_HOMECATE_DATA, listAudioGroups)
            editor.commit()

        })
        //


        binding.apply {
            binding.containerLayout.setStatusBarColor(resources.getColor(R.color.main_color))
            //

            binding.checkConnectionLayout.setOnRetry { viewModel?.getAudioList(); }

            refreshLayout.apply {
                setOnRefreshListener {
                    viewModel?.getAudioList();
                    isRefreshing = false
                }
            }
//            appToolBar.setOnClickLeft {
//                val drawer = AppDrawer(requireContext())
//                drawer.show()
//            }

        }

    }

    private fun initPreData() {
        val sharedPref =requireContext().getSharedPreferences(Key.KEY_STORE, AppCompatActivity.MODE_PRIVATE)
        val listAudioGroupsJson = sharedPref.getString(Key.KEY_HOMECATE_DATA, "")

        if(listAudioGroupsJson!!.isNotEmpty()){
            val type = object : TypeToken<MutableList<AudioGroup>>() {}.type
            val connections: MutableList<AudioGroup> = Gson().fromJson(listAudioGroupsJson, type)
            viewModel.listAudioGroup.postValue(connections)
        }



    }

    override fun getLayoutBinding(): ViewDataBinding {
        return binding;
    }
}