package com.fsacchi.schoolmate.core.platform

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PagerAdapter(
    manager: FragmentManager
) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragments = arrayListOf<Fragment>()
    private val titles = arrayListOf<String>()

    fun addFragment(fragment: Fragment) {
        fragments.add(fragment)
    }

    fun addFragment(frags: List<Fragment>) {
        frags.forEach { fragments.add(it) }
    }

    fun addTitle(list: List<String>) {
        list.forEach {
            titles.add(it)
        }
    }

    fun addTitle(title: String) {
        titles.add(title)
    }

    fun getFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size

    override fun getPageTitle(position: Int) = titles[position]
}
