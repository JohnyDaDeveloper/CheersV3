package cz.johnyapps.cheers.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cz.johnyapps.cheers.R;
import cz.johnyapps.cheers.adapters.recycleradapters.CountersAdapter;
import cz.johnyapps.cheers.entities.BeverageCategory;
import cz.johnyapps.cheers.entities.CounterWithBeverage;
import cz.johnyapps.cheers.tools.Logger;
import cz.johnyapps.cheers.tools.SharedPrefsUtils;
import cz.johnyapps.cheers.viewmodels.MainViewModel;
import cz.johnyapps.cheers.views.CustomCoordinatorLayout;
import cz.johnyapps.cheers.views.EmptyMessageRecyclerView;

public class BeverageCategoryFragment extends Fragment implements BackOptionFragment {
    private static final String TAG = "BeverageCategoryFragment";

    @NonNull
    private final BeverageCategory beverageCategory;
    @Nullable
    private SharedPreferences generalPrefs;
    @Nullable
    private BottomSheetBehavior<View> bottomSheetBehavior;
    @Nullable
    private CountersAdapter adapter;
    private long selectedCounterId = -1;

    private MainViewModel viewModel;
    private AppCompatImageView beverageCategoryImageView;

    public BeverageCategoryFragment(@NonNull BeverageCategory beverageCategory) {
        this.beverageCategory = beverageCategory;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViewModel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CustomCoordinatorLayout root = (CustomCoordinatorLayout) inflater.inflate(R.layout.fragment_beverage_category, container, false);

        selectedCounterId = getGeneralPrefs().getLong(beverageCategory.getSelectedCounterPrefName(), -1);

        setupCategoryImage(root);
        setupBottomSheet(root);
        setupCounterRecyclerView(root);
        setupObservers();
        changePeekHeight(viewModel.getCounterHeight());

        return root;
    }

    @Override
    public boolean onBackPressed() {
        if (isBottomSheetExpanded() && bottomSheetBehavior != null) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return true;
        }

        return false;
    }

    private void setupCounterRecyclerView(@NonNull View root) {
        EmptyMessageRecyclerView counterRecyclerView = root.findViewById(R.id.countersRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        counterRecyclerView.setLayoutManager(layoutManager);
        counterRecyclerView.setAdapter(adapter);
        counterRecyclerView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            if (bottomSheetBehavior != null) {
                bottomSheetBehavior.setDraggable(firstVisibleItemPosition == 0);
            }
        });
    }

    private void setupCategoryImage(@NonNull View root) {
        beverageCategoryImageView = root.findViewById(R.id.beverageCategoryImageView);
        beverageCategoryImageView.setImageDrawable(ContextCompat.getDrawable(root.getContext(), beverageCategory.getImageResId()));
        beverageCategoryImageView.setOnClickListener(v -> playSound(v.getContext()));
    }

    private void setupObservers() {
        viewModel.getCountersWithBeverages().observe(getViewLifecycleOwner(), counterWithBeverages -> {
            counterWithBeverages = new ArrayList<>(counterWithBeverages);

            if (adapter != null) {
                adapter.update(counterWithBeverages);
            }

            if (bottomSheetBehavior != null) {
                changePeekHeight(counterWithBeverages.isEmpty() ? 0 : viewModel.getCounterHeight());
            }

            showSelectedCounter();
        });
    }

    private void setupBottomSheet(@NonNull View root) {
        View bottomSheet = root.findViewById(R.id.bottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED || newState == BottomSheetBehavior.STATE_HALF_EXPANDED) {
                    if (adapter != null) {
                        adapter.setAllCountersDisabled(true);
                    }
                } else {
                    if (adapter != null) {
                        adapter.setAllCountersDisabled(false);
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        adapter = new CountersAdapter(root.getContext(), viewModel.getCountersWithBeverages().getValue());
        adapter.setAllowSelection(false);
        adapter.setOnCounterClickListener(this::selectCounter);
        adapter.setOnSizeChangedListener((width, height) -> changePeekHeight(height));

        if (adapter.isAllCountersDisabled()) {
            adapter.setAllCountersDisabled(false);
        }
    }

    private void showSelectedCounter() {
        List<CounterWithBeverage> counters = viewModel.getCountersWithBeverages().getValue();

        if (counters != null) {
            CounterWithBeverage selectedCounter = null;
            int position = -1;

            if (selectedCounterId > 0) {
                for (int i = 0; i < counters.size(); i++) {
                    if (counters.get(i).getCounter().getId() == selectedCounterId) {
                        position = i;
                        selectedCounter = counters.get(i);
                        break;
                    }
                }
            }

            selectCounter(selectedCounter, position);
        }
    }

    private void changePeekHeight(int peekHeight) {
        viewModel.setCounterHeight(peekHeight);

        if (bottomSheetBehavior != null) {
            bottomSheetBehavior.setPeekHeight(peekHeight);
        }

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) beverageCategoryImageView.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, peekHeight);
        beverageCategoryImageView.setLayoutParams(layoutParams);
        beverageCategoryImageView.forceLayout();
    }

    public void selectLastCounter() {
        if (adapter != null) {
            int position = adapter.getItemCount() - 1;
            selectCounter(adapter.getItem(position), position);
        } else {
            Logger.w(TAG, "selectLastCounter: Adapter is null");
        }
    }

    private void selectCounter(@Nullable CounterWithBeverage counterWithBeverage, int position) {
        if (adapter != null) {
            adapter.moveToTop(position);
        }

        selectedCounterId = counterWithBeverage == null ? -1 : counterWithBeverage.getCounter().getId();

        if (bottomSheetBehavior != null) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            Logger.w(TAG, "selectCounter: BottomSheetBehavior is null");
        }

        getGeneralPrefs().edit()
                .putLong(beverageCategory.getSelectedCounterPrefName(), selectedCounterId)
                .apply();
    }

    private void setupViewModel() {
        ViewModelProvider provider = new ViewModelProvider(requireActivity());
        viewModel = provider.get(MainViewModel.class);
    }

    private boolean isBottomSheetExpanded() {
        if (bottomSheetBehavior == null) {
            return false;
        }

        int state = bottomSheetBehavior.getState();
        return state == BottomSheetBehavior.STATE_EXPANDED || state == BottomSheetBehavior.STATE_HALF_EXPANDED;
    }

    private void playSound(@NonNull Context context) {
        int[] sounds = beverageCategory.getSounds();

        if (sounds.length == 0) {
            return;
        }

        int soundToPlay;

        if (sounds.length > 1) {
            soundToPlay = sounds[new Random().nextInt(sounds.length)];
        } else {
            soundToPlay = sounds[0];
        }

        MediaPlayer player = MediaPlayer.create(context, soundToPlay);
        player.start();
    }

    @NonNull
    public SharedPreferences getGeneralPrefs() {
        if (generalPrefs == null) {
            generalPrefs = SharedPrefsUtils.getGeneralPrefs(requireContext());
        }

        return generalPrefs;
    }
}
