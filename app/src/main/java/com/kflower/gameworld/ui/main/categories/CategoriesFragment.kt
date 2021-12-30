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
import com.kflower.gameworld.common.components.CenterZoomLinearLayoutManager
import com.kflower.gameworld.common.core.BaseFragment
import com.kflower.gameworld.databinding.CategoriesFragmentBinding
import com.kflower.gameworld.databinding.HomeFragmentBinding
import com.kflower.gameworld.model.Category
import com.kflower.gameworld.ui.main.home.HomeFragment
import com.kflower.gameworld.ui.main.home.HomeViewModel

class CategoriesFragment : BaseFragment() {

    companion object {
        fun newInstance() = CategoriesFragment()
    }

    private lateinit var viewModel: CategoriesViewModel

    lateinit var binding: CategoriesFragmentBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CategoriesFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(CategoriesViewModel::class.java)
        val layoutManagerVertical =
            CenterZoomLinearLayoutManager(requireContext())
        var listCategory = arrayListOf<Category>()
        listCategory.add(Category("Romantic", ""))
        listCategory.add(Category("Kungfu", ""))
        listCategory.add(Category("Science", ""))
        listCategory.add(Category("Romantic", ""))
        listCategory.add(Category("Kungfu", ""))
        listCategory.add(Category("Science", ""))
        listCategory.add(Category("Romantic", ""))
        listCategory.add(Category("Kungfu", ""))
        listCategory.add(Category("Science", ""))
        listCategory.add(Category("Romantic", ""))
        listCategory.add(Category("Kungfu", ""))
        listCategory.add(Category("Science", ""))
        binding.apply {
            lvCategories.layoutManager = layoutManagerVertical
            lvCategories.adapter = CategoriesAdapter(requireContext(), listCategory)
        }
    }

    override fun getLayoutBinding(): ViewDataBinding {
        return binding;
    }

}