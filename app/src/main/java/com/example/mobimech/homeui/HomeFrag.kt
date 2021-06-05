package com.example.mobimech.homeui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobimech.R
import com.example.mobimech.UI.UserMapUi
import com.example.mobimech.adapters.OrdersRRecyclerViewAdapter
import com.example.mobimech.adapters.TabsAdapter
import com.example.mobimech.databinding.FragmentHomeBinding
import com.example.mobimech.databinding.FragmentHomeTabsBinding
import com.example.mobimech.models.DisplayItem
import com.example.mobimech.models.OrderListItem


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFrag.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFrag : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var fragmentHomeBinding: FragmentHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)


        fragmentHomeBinding= FragmentHomeBinding.inflate(inflater, container, false)
        fragmentHomeBinding.viewpager.adapter= TabsAdapter(childFragmentManager)
        fragmentHomeBinding.tabLayout.setupWithViewPager(fragmentHomeBinding.viewpager)

        return fragmentHomeBinding.root


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        fragmentHomeBinding.makeorders.setOnClickListener {
//            startActivity(Intent(activity,UserMapUi::class.java))
//        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFrag.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFrag().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}