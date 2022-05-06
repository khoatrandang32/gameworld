package com.kflower.gameworld.ui.main.categories

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kflower.gameworld.adapter.CategoriesAdapter
import com.kflower.gameworld.adapter.FavouriteCategoriesAdapter
import com.kflower.gameworld.common.components.CenterZoomLinearLayoutManager
import com.kflower.gameworld.common.core.BaseChildFragment
import com.kflower.gameworld.common.core.BaseFragment
import com.kflower.gameworld.databinding.CategoriesFragmentBinding
import com.kflower.gameworld.databinding.HomeFragmentBinding
import com.kflower.gameworld.model.Category
import com.kflower.gameworld.ui.main.home.HomeFragment
import com.kflower.gameworld.ui.main.home.HomeViewModel
import com.kflower.gameworld.ui.main.listAudio.ListAudioFragment

class CategoriesFragment : BaseChildFragment() {

    companion object {
        fun newInstance() = CategoriesFragment()
    }

    private lateinit var viewModel: CategoriesViewModel

    lateinit var binding: CategoriesFragmentBinding;
    lateinit var adapterListAll: CategoriesAdapter;
    lateinit var adapterListFav: FavouriteCategoriesAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CategoriesFragmentBinding.inflate(layoutInflater)
        viewModel = CategoriesViewModel()

        binding.viewModel= viewModel;
        binding.lifecycleOwner=this


        adapterListAll=CategoriesAdapter(requireContext(), viewModel.listCategories.value!!,
            object :CategoriesAdapter.OnClickCategory{
            override fun onClick(item: Category) {
                parentNavigateTo(ListAudioFragment(item))

            }

        })
        adapterListFav= FavouriteCategoriesAdapter(requireContext(), viewModel.listCategories.value!!)

        viewModel.getCategories();

        viewModel.listCategories.observe(this,{
            adapterListAll.setData(it)
            adapterListFav.setData(it)
        })
        val layoutManagerVertical =
            CenterZoomLinearLayoutManager(requireContext())
        val layoutManagerHorizontal =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.apply {
            lvCategories.layoutManager = layoutManagerVertical
            lvCategories.adapter = adapterListAll

            lvFav.layoutManager = layoutManagerHorizontal;
            lvFav.adapter =  adapterListFav;
            checkConnectionLayout.setOnRetry {
                viewModel?.getCategories();
            }
        }
    }

    override fun getLayoutBinding(): ViewDataBinding {
        return binding;
    }

    override fun onResume() {
        super.onResume()
        if(viewModel.isError.value==true){
            viewModel?.getCategories();
        }
    }

}