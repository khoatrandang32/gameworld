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
        val layoutManagerHorizontal =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        var listCategory = arrayListOf<Category>()
        listCategory.add(
            Category(
                "Romantic",
                "https://jssors8.azureedge.net/demos/image-slider/img/px-beach-daylight-fun-1430675-image.jpg"
            )
        )
        listCategory.add(
            Category(
                "Kungfu",
                "https://d1j8r0kxyu9tj8.cloudfront.net/images/1566809340Y397jnilYDd15KN.jpg"
            )
        )
        listCategory.add(
            Category(
                "Science",
                "https://i.pinimg.com/originals/bc/d5/c9/bcd5c9519581acc60bd60a429ab0c88f.jpg"
            )
        )
        listCategory.add(
            Category(
                "Romantic",
                "https://cdn-amz.fadoglobal.io/images/I/71OIhbUOF-L.jpg"
            )
        )
        listCategory.add(
            Category(
                "Romantic",
                "https://jssors8.azureedge.net/demos/image-slider/img/px-beach-daylight-fun-1430675-image.jpg"
            )
        )
        listCategory.add(
            Category(
                "Kungfu",
                "https://d1j8r0kxyu9tj8.cloudfront.net/images/1566809340Y397jnilYDd15KN.jpg"
            )
        )
        listCategory.add(
            Category(
                "Science",
                "https://i.pinimg.com/originals/bc/d5/c9/bcd5c9519581acc60bd60a429ab0c88f.jpg"
            )
        )
        listCategory.add(
            Category(
                "Romantic",
                "https://cdn-amz.fadoglobal.io/images/I/71OIhbUOF-L.jpg"
            )
        )
        listCategory.add(
            Category(
                "Romantic",
                "https://jssors8.azureedge.net/demos/image-slider/img/px-beach-daylight-fun-1430675-image.jpg"
            )
        )
        listCategory.add(
            Category(
                "Kungfu",
                "https://d1j8r0kxyu9tj8.cloudfront.net/images/1566809340Y397jnilYDd15KN.jpg"
            )
        )
        listCategory.add(
            Category(
                "Science",
                "https://i.pinimg.com/originals/bc/d5/c9/bcd5c9519581acc60bd60a429ab0c88f.jpg"
            )
        )
        listCategory.add(
            Category(
                "Romantic",
                "https://cdn-amz.fadoglobal.io/images/I/71OIhbUOF-L.jpg"
            )
        )
        listCategory.add(
            Category(
                "Romantic",
                "https://jssors8.azureedge.net/demos/image-slider/img/px-beach-daylight-fun-1430675-image.jpg"
            )
        )
        listCategory.add(
            Category(
                "Kungfu",
                "https://d1j8r0kxyu9tj8.cloudfront.net/images/1566809340Y397jnilYDd15KN.jpg"
            )
        )
        listCategory.add(
            Category(
                "Science",
                "https://i.pinimg.com/originals/bc/d5/c9/bcd5c9519581acc60bd60a429ab0c88f.jpg"
            )
        )
        listCategory.add(
            Category(
                "Romantic",
                "https://cdn-amz.fadoglobal.io/images/I/71OIhbUOF-L.jpg"
            )
        )
        binding.apply {
            lvCategories.layoutManager = layoutManagerVertical
            lvCategories.adapter = CategoriesAdapter(requireContext(), listCategory)

            lvFav.layoutManager = layoutManagerHorizontal;
            lvFav.adapter = FavouriteCategoriesAdapter(requireContext(), listCategory)
        }
    }

    override fun getLayoutBinding(): ViewDataBinding {
        return binding;
    }

}