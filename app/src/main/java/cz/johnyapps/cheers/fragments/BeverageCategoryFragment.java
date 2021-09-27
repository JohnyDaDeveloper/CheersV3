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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.List;
import java.util.Random;

import cz.johnyapps.cheers.R;
import cz.johnyapps.cheers.adapters.recycleradapters.CountersAdapter;
import cz.johnyapps.cheers.entities.BeverageCategory;
import cz.johnyapps.cheers.entities.CounterWithBeverage;
import cz.johnyapps.cheers.tools.Logger;
import cz.johnyapps.cheers.tools.SharedPrefsUtils;
import cz.johnyapps.cheers.viewmodels.MainViewModel;
import cz.johnyapps.cheers.views.CounterView;

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
    private int counterHeight = 0;
    private long selectedCounterId = -1;

    private MainViewModel viewModel;

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
        View root = inflater.inflate(R.layout.fragment_beverage_category, container, false);

        setupCounter(root);
        setupBottomSheet(root);
        setupCounterButtons(root);
        setupObservers();

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

    private void setupCounterButtons(@NonNull View root) {
        AppCompatImageView closeCounterImageView = root.findViewById(R.id.closeCounterImageView);
        closeCounterImageView.setOnClickListener(v -> {
            if (bottomSheetBehavior != null) {
                bottomSheetBehavior.setPeekHeight(0);
            }

            selectedCounterId = -1;
            setCounterButtonsVisibility(root);
        });

        AppCompatImageView swapCountersImageView = root.findViewById(R.id.swapCountersImageView);
        swapCountersImageView.setOnClickListener(v -> {
            if (bottomSheetBehavior != null) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    private void setupObservers() {
        viewModel.getCountersWithBeverages().observe(getViewLifecycleOwner(), counterWithBeverages -> {
            if (adapter != null) {
                adapter.update(counterWithBeverages);
            }
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

        if (adapter.isAllCountersDisabled()) {
            adapter.setAllCountersDisabled(false);
        }

        RecyclerView counterRecyclerView = root.findViewById(R.id.countersRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        counterRecyclerView.setLayoutManager(layoutManager);
        counterRecyclerView.setAdapter(adapter);
        counterRecyclerView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            if (bottomSheetBehavior != null) {
                bottomSheetBehavior.setDraggable(firstVisibleItemPosition == 0);
            }
        });

        setupSelectedCounter(root);
    }

    private void setupSelectedCounter(@NonNull View root) {
        long selectedCounterId = getGeneralPrefs().getLong(beverageCategory.getSelectedCounterPrefName(), -1);
        CounterWithBeverage selectedCounter = null;
        int position = -1;

        if (selectedCounterId > 0) {
            List<CounterWithBeverage> counters = viewModel.getCountersWithBeverages().getValue();

            if (counters != null) {
                for (int i = 0; i < counters.size(); i++) {
                    if (counters.get(i).getCounter().getId() == selectedCounterId) {
                        position = i;
                        selectedCounter = counters.get(i);
                        break;
                    }
                }
            }
        }

        if (selectedCounter != null) {
            selectCounter(selectedCounter, position, root);
        }
    }

    private void selectCounter(@Nullable CounterWithBeverage counterWithBeverage, int position) {
        selectCounter(counterWithBeverage, position, null);
    }

    private void selectCounter(@Nullable CounterWithBeverage counterWithBeverage,
                               int position,
                               @Nullable View root) {
        selectedCounterId = counterWithBeverage == null ? -1 : counterWithBeverage.getCounter().getId();
        setCounterButtonsVisibility(root);

        if (bottomSheetBehavior != null) {
            bottomSheetBehavior.setPeekHeight(counterHeight);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        if (adapter != null) {
            adapter.moveToTop(position);
        }

        if (counterWithBeverage != null) {
            getGeneralPrefs().edit()
                    .putLong(beverageCategory.getSelectedCounterPrefName(), counterWithBeverage.getCounter().getId())
                    .apply();
        }
    }

    private void setCounterButtonsVisibility(@Nullable View root) {
        if (root == null) {
            root = getView();

            if (root == null) {
                Logger.w(TAG, "setCloseCounterVisibility: root is null");
                return;
            }
        }

        AppCompatImageView closeCounterImageView = root.findViewById(R.id.closeCounterImageView);
        AppCompatImageView swapCountersImageView = root.findViewById(R.id.swapCountersImageView);

        if (showFirstCounter()) {
            closeCounterImageView.setVisibility(View.GONE);
            swapCountersImageView.setVisibility(View.VISIBLE);
        } else {
            closeCounterImageView.setVisibility(View.VISIBLE);
            swapCountersImageView.setVisibility(View.GONE);
        }
    }

    private boolean showFirstCounter() {
        return adapter != null &&
                adapter.getItemCount() > 0 &&
                adapter.getItem(0).getCounter().getId() == selectedCounterId;
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

    private void setupCounter(@NonNull View root) {
        int count = getGeneralPrefs().getInt(beverageCategory.getCountPrefName(), 0);

        CounterView counterView = root.findViewById(R.id.counterView);
        counterView.setCount(count);
        counterView.setTitleText(beverageCategory.getTitleResId());
        counterView.setOnValueChangeListener(value -> getGeneralPrefs().edit().putInt(beverageCategory.getCountPrefName(), value).apply());
        counterView.setOnSizeChangedListener((width, height) -> {
            if (bottomSheetBehavior != null) {
                bottomSheetBehavior.setPeekHeight(showFirstCounter() ? 0 : height);
            }

            counterHeight = height;
        });

        AppCompatImageView beverageCategoryImageView = root.findViewById(R.id.beverageCategoryImageView);
        beverageCategoryImageView.setImageDrawable(ContextCompat.getDrawable(root.getContext(), beverageCategory.getImageResId()));
        beverageCategoryImageView.setOnClickListener(v -> playSound(v.getContext()));
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
