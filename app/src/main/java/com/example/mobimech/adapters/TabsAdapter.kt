@file:Suppress("DEPRECATION")

package com.example.mobimech.adapters

import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.mobimech.homeui.LogsFrag
import com.example.mobimech.homeui.MakeOrder

class TabsAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return MakeOrder()
            1 -> return LogsFrag()
        }

        return MakeOrder()
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return "Make Order"
            1 -> return "History"
        }
        return null
    }

}