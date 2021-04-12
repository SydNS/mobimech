package com.example.mobimech.AuthDestinationFrags

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.transition.Visibility
import androidx.viewpager2.widget.ViewPager2
import com.example.mobimech.R
import com.example.mobimech.adapters.WalkthroughRecyclerviewdapter
import com.example.mobimech.databinding.FragmentWalkthroughBinding
import com.example.mobimech.mobimechsharedpreferences.IsItTheAppsFirstTimeOpenning
import com.example.mobimech.models.DisplayItem

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Walkthrough.newInstance] factory method to
 * create an instance of this fragment.
 */
class Walkthrough : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private lateinit var sharedPreferences: SharedPreferences
    lateinit var isItTheAppsFirstTimeOpenning: IsItTheAppsFirstTimeOpenning

    lateinit var walkthroughBinding: FragmentWalkthroughBinding
    lateinit var walkthroughRecyclerviewdapter: WalkthroughRecyclerviewdapter
    lateinit var walkthruVP: ViewPager2

    lateinit var walkthruprevious: Button
    lateinit var walkthrunext: Button
    lateinit var walkthrsignin: Button
    lateinit var users: ArrayList<DisplayItem>

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        sharedPreferences =
//            context.getSharedPreferences("NotTheFirsttime", Context.MODE_PRIVATE)
    }

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

        isItTheAppsFirstTimeOpenning = IsItTheAppsFirstTimeOpenning(requireActivity())

        walkthroughBinding = FragmentWalkthroughBinding.inflate(inflater, container, false)
        val view: View = walkthroughBinding.root
        //getting recyclerview from xml
        walkthruVP = walkthroughBinding.walkthroughviewpager
        walkthruprevious = walkthroughBinding.previous
        walkthrunext = walkthroughBinding.next
        walkthrsignin = walkthroughBinding.Signinbtnwalkthrough


        sharedPreferences =
            activity?.getSharedPreferences("NotTheFirsttime", Context.MODE_PRIVATE)!!



        //crating an arraylist to store users using the data class user
        users = ArrayList<DisplayItem>()

        //adding some dummy data to the list
        users.add(DisplayItem("We are Mobile Mech", R.drawable.bg, getString(R.string.descn1)))
        users.add(DisplayItem("We are Mobile Mech", R.drawable.bg2, getString(R.string.descn2)))
        users.add(DisplayItem("We are Mobile Mech", R.drawable.bg3, getString(R.string.descn2)))

        //creating our adapter
        walkthroughRecyclerviewdapter = WalkthroughRecyclerviewdapter(users)

        //now adding the adapter to recyclerview
        walkthruVP.adapter = walkthroughRecyclerviewdapter

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        walkthruVP.adapter = walkthroughRecyclerviewdapter
//        var ci: Int = walkthruVP.currentItem
//        walkthruprevious.visibility = View.GONE


        walkthruVP.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)

            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
//                val d: Int = users.size - 1
                when {
                    position == 2 -> {
                        walkthruprevious.visibility = View.VISIBLE
                        walkthrunext.visibility = View.GONE
                        walkthrsignin.visibility = View.VISIBLE
                    }
                    position==1 -> {
                        walkthruprevious.visibility = View.VISIBLE
                        walkthrunext.visibility = View.VISIBLE
                        walkthrsignin.visibility = View.GONE
                    }
                    position==0 -> {
                        walkthruprevious.visibility = View.GONE
                        walkthrunext.visibility = View.VISIBLE
                        walkthrsignin.visibility = View.GONE
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })

        walkthruprevious.setOnClickListener {
            val ci = walkthruVP.currentItem

            walkthruVP.setCurrentItem(ci - 1, true)
        }
        walkthrunext.setOnClickListener {
            val ci = walkthruVP.currentItem

            walkthruVP.setCurrentItem(ci + 1, true)
        }

        walkthroughBinding.Signinbtnwalkthrough.setOnClickListener {
            isItTheAppsFirstTimeOpenning.writeInstalled()
            Navigation.findNavController(view).navigate(R.id.action_walkthrough_to_loginFrag)
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Walkthrough.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Walkthrough().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}