package com.mapxus.mapxusmapandroiddemo.examples.integrationcases.explore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.mapxus.map.mapxusmap.api.map.model.IndoorBuilding;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.utils.LocalLanguageUtils;

import java.util.Locale;

public class FirstFragment extends Fragment implements ExploreBuildingActivity.OnBuildingChageListener {

    private Button button;
    private IndoorBuilding indoorBuilding;
    private ExploreBuildingActivity exploreBuildingActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        exploreBuildingActivity = (ExploreBuildingActivity) this.getActivity();
        if (exploreBuildingActivity != null)
            exploreBuildingActivity.setOnBuildingChageListener(this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        button = view.findViewById(R.id.btn_explore);
        button.setOnClickListener(v -> {
            if (indoorBuilding != null) {
                Bundle bundle = new Bundle();
                bundle.putString("buildingId", indoorBuilding.getBuildingId());
                bundle.putString("buildingName", LocalLanguageUtils.getLocalLanguageWithIndoorBuilding(indoorBuilding));
                Navigation.findNavController(v).navigate(R.id.action_firstFragment_to_secondFragment, bundle);
                exploreBuildingActivity.getBottomSheetBehavior().setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (indoorBuilding != null) {
            button.setText(String.format("%s%s", "Explore ", LocalLanguageUtils.getLocalLanguageWithIndoorBuilding(indoorBuilding)));
        }
    }

    @Override
    public void onBuildingChange(IndoorBuilding indoorBuilding) {
        if (indoorBuilding != null) {
            this.indoorBuilding = indoorBuilding;
            button.setText(String.format("%s%s", "Explore ", LocalLanguageUtils.getLocalLanguageWithIndoorBuilding(indoorBuilding)));
        }
    }
}