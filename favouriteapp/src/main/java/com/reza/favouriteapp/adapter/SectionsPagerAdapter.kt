package com.reza.favouriteapp.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.reza.favouriteapp.ui.fragment.FollowerTabFragment
import com.reza.favouriteapp.ui.fragment.FollowingTabFragment

class SectionsPagerAdapter(activity:AppCompatActivity,fm:FragmentManager):FragmentStateAdapter(activity) {
    var userName :String? = null
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowingTabFragment.newInstance(userName)
            1 -> fragment = FollowerTabFragment.newInstance(userName)
        }
        return fragment as Fragment    }
}