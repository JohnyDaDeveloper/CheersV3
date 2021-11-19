package cz.johnyapps.cheers.adapters.recycler

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import cz.johnyapps.cheers.entities.BeverageCategory
import cz.johnyapps.cheers.fragments.BeverageCategoryFragment

class BeverageFragmentAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    private var fragments = ArrayList<Fragment>()

    override fun getItemCount(): Int {
        return BeverageCategory.values().size
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = BeverageCategoryFragment(BeverageCategory.values()[position])
        fragments.add(fragment)
        return fragment
    }

    fun getFragment(position: Int): Fragment? {
        if (position in 0 until itemCount) {
            return fragments[position]
        }

        return null
    }
}