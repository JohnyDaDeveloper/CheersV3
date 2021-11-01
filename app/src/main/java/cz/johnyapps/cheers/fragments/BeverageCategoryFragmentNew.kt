package cz.johnyapps.cheers.fragments

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import cz.johnyapps.cheers.R
import cz.johnyapps.cheers.SharedPrefsNames
import cz.johnyapps.cheers.adapters.CountersAdapter
import cz.johnyapps.cheers.databinding.FragmentBeverageCategoryNewBinding
import cz.johnyapps.cheers.entities.BeverageCategory
import cz.johnyapps.cheers.entities.CounterWithBeverage
import cz.johnyapps.cheers.tools.Logger
import cz.johnyapps.cheers.tools.SharedPrefsUtils
import cz.johnyapps.cheers.viewmodels.BeverageCategoriesViewModel
import java.util.*
import kotlin.collections.ArrayList

class BeverageCategoryFragmentNew(private var viewModel: BeverageCategoriesViewModel, private var beverageCategory: BeverageCategory): Fragment(), BackOptionFragment {
    private lateinit var binding: FragmentBeverageCategoryNewBinding
    private lateinit var adapter: CountersAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    private var peekHeightIfCounters = 0

    companion object {
        private const val TAG = "BeverageCategoryFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_beverage_category_new, container, false)
        val context = binding.root.context

        peekHeightIfCounters = SharedPrefsUtils.getGeneralPrefs(context).getInt(SharedPrefsNames.COUNTER_HEIGHT, 0)

        binding.beverageCategoryImageView.setImageDrawable(ContextCompat.getDrawable(context, beverageCategory.imageResId))
        binding.beverageCategoryImageView.setOnClickListener { playSound(context) }

        setupCountersRecyclerView()
        setupBottomSheetBehavior()
        changePeekHeight(0)
        setupObservers()

        return binding.root
    }

    private fun changePeekHeight(peekHeight: Int) {
        bottomSheetBehavior.peekHeight = peekHeight

        val layoutParams = binding.beverageCategoryImageView.layoutParams as MarginLayoutParams
        layoutParams.setMargins(0, 0, 0, peekHeight)
        binding.beverageCategoryImageView.layoutParams = layoutParams
        binding.beverageCategoryImageView.forceLayout()
    }

    private fun setupCountersRecyclerView() {
        val context = binding.root.context

        adapter = CountersAdapter()
        adapter.setAllowSelection(false)
        adapter.onCounterClickListener = object : CountersAdapter.OnCounterClickListener {
            override fun onClick(counterWithBeverage: CounterWithBeverage) {
                selectCounter(counterWithBeverage)
            }
        }

        binding.countersRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.countersRecyclerView.adapter = adapter
    }

    private fun setupBottomSheetBehavior() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                adapter.allCountersDisabled = newState == BottomSheetBehavior.STATE_EXPANDED ||
                        newState == BottomSheetBehavior.STATE_HALF_EXPANDED
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
    }

    fun selectCounter(counterWithBeverage: CounterWithBeverage) {
        Logger.d(TAG, "selectCounter: Selected counter: ${counterWithBeverage.beverage.name}")

        binding.countersRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //For some reason it has to be there
            }
        })
        binding.countersRecyclerView.scrollToPosition(0)

        val context = binding.root.context
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        val selectedCounterId = counterWithBeverage.counter.id
        SharedPrefsUtils.getGeneralPrefs(context).edit()
            .putLong(beverageCategory.selectedCounterPrefName, selectedCounterId)
            .apply()

        submitList(viewModel.countersWithBeverages.value)
    }

    private fun submitList(it: List<CounterWithBeverage>?) {
        if (it != null) {
            val selected = SharedPrefsUtils.getGeneralPrefs(binding.root.context).getLong(beverageCategory.selectedCounterPrefName, -1)
            val list = ArrayList(it) as MutableList<CounterWithBeverage>

            if (selected > -1) {
                for (i in 0 until list.size) {
                    val item = list[i]
                    if (item.counter.id == selected) {
                        list.removeAt(i)
                        list.add(0, item)
                        break
                    }
                }
            }

            adapter.submitList(list)
        } else {
            adapter.submitList(null)
        }
    }

    private fun setupObservers() {
        viewModel.countersWithBeverages.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                changePeekHeight(peekHeightIfCounters)
            } else {
                changePeekHeight(0)
            }

            submitList(it)
        })
    }

    private fun playSound(context: Context) {
        val sounds = beverageCategory.sounds

        if (sounds.isEmpty()) {
            return
        }

        val soundToPlay: Int = if (sounds.size > 1) {
            sounds[Random().nextInt(sounds.size)]
        } else {
            sounds[0]
        }

        val player = MediaPlayer.create(context, soundToPlay)
        player.start()
    }

    override fun onBackPressed(): Boolean {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED ||
            bottomSheetBehavior.state == BottomSheetBehavior.STATE_HALF_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            return true
        }

        return false
    }
}