package com.kflower.gameworld.ui.main.profile

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kflower.gameworld.R
import com.kflower.gameworld.adapter.OptionAdapter
import com.kflower.gameworld.common.core.BaseFragment
import com.kflower.gameworld.databinding.HomeFragmentBinding
import com.kflower.gameworld.databinding.ProfileFragmentBinding
import com.kflower.gameworld.model.Option
import com.kflower.gameworld.ui.main.home.HomeFragment
import com.kflower.gameworld.ui.main.home.HomeViewModel

class ProfileFragment : BaseFragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
    private lateinit var optionAdapter: OptionAdapter
    private lateinit var listOption: MutableList<Option>

    lateinit var binding: ProfileFragmentBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ProfileFragmentBinding.inflate(layoutInflater)
        binding.lifecycleOwner= this;
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        //
        listOption= arrayListOf()

        listOption.add(Option(0,"Truyện yêu thích", R.drawable.ic_like_outline))
        listOption.add(Option(1,"Kho tải truyện", R.drawable.ic_download))
        listOption.add(Option(2,"Lịch sử nghe", R.drawable.ic_outline_history))
        listOption.add(Option(3,"Thể loại yêu thích", R.drawable.ic_categories_outline))
        listOption.add(Option(4,"Thông báo", R.drawable.ic_notification))
        //
        optionAdapter= OptionAdapter(requireContext(),listOption,object :OptionAdapter.ClickOptionListener{
            override fun onClick(audioUrl: Option, position: Int) {

            }
        })
        binding?.apply {
            rcOptions.layoutManager=  LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rcOptions.adapter= optionAdapter
        }
    }

    override fun getLayoutBinding(): ViewDataBinding {
        return binding;
    }

}