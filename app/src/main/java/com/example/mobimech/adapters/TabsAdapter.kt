@file:Suppress("DEPRECATION")

package com.example.mobimech.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TabsAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragments: List<Fragment> = ArrayList()
    private val fragmentTitle: List<String> = ArrayList()

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {

            }
            1 -> {

            }
        }

    }
}