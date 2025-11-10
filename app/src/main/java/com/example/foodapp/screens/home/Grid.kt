package com.example.foodapp.screens.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.foodapp.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class Grid : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private val sliderHandler = Handler(Looper.getMainLooper())
    private lateinit var sliderRunnable: Runnable
    private lateinit var ingredientAdapter: IngredientAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_grid, container, false)
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = view.findViewById(R.id.viewpager2)
        tabLayout = view.findViewById(R.id.tabLayout)

        val sliderItems = listOf(
            HomeModel(R.mipmap.ic_launcher,"Chicken","$30" ,"Islmabad I-8 Markaz" ,"5.6" ,"(8 Reviews)" ,"Tender chicken pieces cooked in rich spices and herbs, bursting with flavor. A wholesome dish loved by all ages. Tender chicken pieces cooked in rich spices and herbs, bursting with flavor. A wholesome dish loved by all ages. Tender chicken pieces cooked in rich spices and herbs, bursting with flavor. A wholesome dish loved by all ages. Tender chicken pieces cooked in rich spices and herbs, bursting with flavor. A wholesome dish loved by all ages. Tender chicken pieces cooked in rich spices and herbs, bursting with flavor. A wholesome dish loved by all ages.", listOf(
                Ingredient(R.mipmap.ic_launcher, "Chicken"),
                Ingredient(R.mipmap.ic_launcher, "Onion"),
                Ingredient(R.mipmap.ic_launcher, "Garlic"),
                Ingredient(R.mipmap.ic_launcher, "Onion"),
            )),
            HomeModel(R.drawable.food2,"Haleem","$40","Islmabad I-10 Markaz" ,"5.2" ,"(18 Reviews)","A slow-cooked delicacy made with wheat, lentils, and meat, simmered to perfection. Rich, hearty, and full of nutrients",
                listOf(
                    Ingredient(R.drawable.chicken, "Chicken"),
                    Ingredient(R.drawable.onion, "Onion"),
                    Ingredient(R.drawable.garlic, "Garlic"),
                    Ingredient(R.drawable.onion, "Onion"),
                    Ingredient(R.drawable.pappers, "Pappers"),
                    Ingredient(R.drawable.onion, "Onion"),
                )),
            HomeModel(R.drawable.food3, "Biryani","$30","Karachi " , "4.6" ,"(28 Reviews)","A fragrant rice dish layered with spiced meat and saffron, cooked with traditional spices. Aromatic, flavorful, and truly royal",
                listOf(
                    Ingredient(R.drawable.chicken, "Chicken"),
                    Ingredient(R.drawable.onion, "Onion"),
                    Ingredient(R.drawable.garlic, "Garlic"),
                    Ingredient(R.drawable.onion, "Onion"),
                    Ingredient(R.drawable.pappers, "Pappers"),
                    Ingredient(R.drawable.onion, "Onion"),
                )),
            HomeModel(R.drawable.food1,"Chicken","$30","Islmabad I-8 Markaz" ,"5.6" ,"(8 Reviews)","Juicy, golden-brown chicken roasted with spices. Crispy outside, tender inside – a perfect comfort meal.",
                listOf(
                    Ingredient(R.drawable.chicken, "Chicken"),
                    Ingredient(R.drawable.onion, "Onion"),
                    Ingredient(R.drawable.garlic, "Garlic"),
                    Ingredient(R.drawable.onion, "Onion"),
                    Ingredient(R.drawable.pappers, "Pappers"),
                    Ingredient(R.drawable.onion, "Onion"),
                )),
            HomeModel(R.drawable.food2,"Haleem","$40","Rawalpindi Commercial Market" ,"5.2" ,"(18 Reviews)","Smooth, creamy, and packed with protein. A soulful dish that melts in your mouth with every bite.",
                listOf(
                    Ingredient(R.drawable.chicken, "Chicken"),
                    Ingredient(R.drawable.onion, "Onion"),
                    Ingredient(R.drawable.garlic, "Garlic")
                )),
            HomeModel(R.drawable.food3, "Biryani","$30","Rawalpindi" ,"3.6" ,"(28 Reviews)","Long-grain basmati rice layered with aromatic masala and tender meat. A feast for the senses, every single time.",
                listOf(
                    Ingredient(R.drawable.chicken, "Chicken"),
                    Ingredient(R.drawable.onion, "Onion"),
                    Ingredient(R.drawable.garlic, "Garlic")
                )),
        )
        val adapter = HomeAdapter(sliderItems)
        viewPager.adapter = adapter
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        ingredientAdapter = IngredientAdapter(emptyList())
        recyclerView.adapter = ingredientAdapter
        val layoutManager = GridLayoutManager(requireContext(), 5, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        val spacingInDpH = (19 * resources.displayMetrics.density).toInt()
        val spacingInDpV = (30 * resources.displayMetrics.density).toInt()
        recyclerView.addItemDecoration(
            GridSpacingItemDecoration(5, spacingInDpH, spacingInDpV)
        )
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val currentItem = sliderItems[position]
                view.findViewById<TextView>(R.id.foodtitle).text = currentItem.title
                view.findViewById<TextView>(R.id.foodprice).text = currentItem.price
                view.findViewById<TextView>(R.id.location).text = currentItem.location
                view.findViewById<TextView>(R.id.rating).text = currentItem.rating
                view.findViewById<TextView>(R.id.reviews).text = currentItem.reviews
                view.findViewById<TextView>(R.id.descriptiontext).apply {
                    text = currentItem.description
                    post {
                        requestLayout()
                    }
                }
                ingredientAdapter.updateData(currentItem.ingredients)
            }
        })
        TabLayoutMediator(tabLayout, viewPager) { tab, _ ->
            tab.setCustomView(R.layout.custom_tab)
        }.attach()
        sliderRunnable = Runnable {
            val itemCount = adapter.itemCount
            if (itemCount > 0) {
                val nextItem = (viewPager.currentItem + 1) % itemCount
                viewPager.setCurrentItem(nextItem, true)
            }
            sliderHandler.postDelayed(sliderRunnable, 5000)
        }
//        sliderHandler.postDelayed(sliderRunnable, 6000)
        (tabLayout.getChildAt(0) as? ViewGroup)?.let { tabStrip ->
            for (i in 0 until tabStrip.childCount) {
                val tab = tabStrip.getChildAt(i)
                tab.setPadding(0, 0, 0, 0)
                val params = tab.layoutParams as ViewGroup.MarginLayoutParams
                params.marginStart = 4
                params.marginEnd = 4
                tab.layoutParams = params
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sliderHandler.removeCallbacks(sliderRunnable)
    }


}