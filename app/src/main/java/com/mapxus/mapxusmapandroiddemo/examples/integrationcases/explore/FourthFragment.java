package com.mapxus.mapxusmapandroiddemo.examples.integrationcases.explore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.mapxus.map.mapxusmap.api.services.BuildingSearch;
import com.mapxus.map.mapxusmap.api.services.model.DetailSearchOption;
import com.mapxus.map.mapxusmap.api.services.model.building.BuildingDetailResult;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.utils.LocalLanguageUtils;


public class FourthFragment extends Fragment {
    private BuildingSearch buildingSearch;
    private ImageView ivCategory;
    private TextView tvTitle, tvBuildingName, tvLocationInfo;
    private String floorName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fourth, container, false);

        ivCategory = view.findViewById(R.id.iv_category);
        tvTitle = view.findViewById(R.id.tv_title);
        tvBuildingName = view.findViewById(R.id.location_building_name);
        tvLocationInfo = view.findViewById(R.id.location_street);
        view.findViewById(R.id.iv_close).setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        buildingSearch = BuildingSearch.newInstance();
        buildingSearch.setBuildingSearchResultListener(buildingSearchResultListener);
        return view;
    }

    private BuildingSearch.BuildingSearchResultListenerAdapter buildingSearchResultListener = new BuildingSearch.BuildingSearchResultListenerAdapter() {
        @Override
        public void onGetBuildingDetailResult(BuildingDetailResult buildingDetailResult) {
            if (buildingDetailResult.status != 0) {
                Toast.makeText(requireContext(), buildingDetailResult.error.toString(), Toast.LENGTH_LONG).show();
                return;
            }
            if (buildingDetailResult.getIndoorBuildingInfo() == null) {
                Toast.makeText(requireContext(), getString(R.string.no_result), Toast.LENGTH_LONG).show();
                return;
            }

            tvBuildingName.setText(LocalLanguageUtils.getLocalLanguage(buildingDetailResult.getIndoorBuildingInfo().getName()));
            tvLocationInfo.setText(String.format("%s , %s · %s", buildingDetailResult.getIndoorBuildingInfo().getRegion(), LocalLanguageUtils.getLocalLanguageWithAddress(buildingDetailResult.getIndoorBuildingInfo().getAddress()), floorName));
        }
    };

    protected void doSearchQuery(String buildingId) {
        DetailSearchOption detailSearchOption = new DetailSearchOption();
        detailSearchOption.id(buildingId);
        buildingSearch.searchBuildingDetail(detailSearchOption);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            floorName = arguments.getString("floorName");
            String poiName = arguments.getString("poiName");
            String buildingId = arguments.getString("buildingId");
            ivCategory.setImageResource(arguments.getInt("catgegoryImage", 0));
            if (poiName != null) {
                tvTitle.setText(poiName);
            }
            if (buildingId != null) {
                doSearchQuery(buildingId);
            }
        }
    }
}