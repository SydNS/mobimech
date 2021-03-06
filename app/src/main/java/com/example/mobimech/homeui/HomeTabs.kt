package com.example.mobimech.homeui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.mobimech.R
import com.example.mobimech.adapters.TabsAdapter
import com.example.mobimech.databinding.FragmentHomeTabsBinding
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeTabs.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeTabs : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var auth: FirebaseAuth

    lateinit var homeTabsBinding: FragmentHomeTabsBinding

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
    ): View {
        // Inflate the layout for this fragment

        auth = FirebaseAuth.getInstance()

        homeTabsBinding = FragmentHomeTabsBinding.inflate(inflater, container, false)
        homeTabsBinding.viewpager.adapter = TabsAdapter(childFragmentManager)
        homeTabsBinding.tabLayout.setupWithViewPager(homeTabsBinding.viewpager)

        homeTabsBinding.logoutbutton.setOnClickListener {
            auth.signOut()
            Navigation.findNavController(homeTabsBinding.root)
                .navigate(R.id.action_homeFrag_to_loginFrag2)
        }

        return homeTabsBinding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeTabs.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeTabs().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}