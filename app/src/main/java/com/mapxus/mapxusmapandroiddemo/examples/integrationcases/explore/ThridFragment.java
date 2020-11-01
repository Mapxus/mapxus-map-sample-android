package com.mapxus.mapxusmapandroiddemo.examples.integrationcases.explore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mapxus.map.mapxusmap.api.services.PoiSearch;
import com.mapxus.map.mapxusmap.api.services.model.PoiInBuildingSearchOption;
import com.mapxus.map.mapxusmap.api.services.model.poi.PoiResult;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.adapter.PoiListAdapter;

import java.util.Objects;

public class ThridFragment extends Fragment {

    private TextView tvTitle;
    private RecyclerView recyclerView;
    private PoiSearch poiSearch;
    private String buildingId;
    private ExploreBuildingActivity exploreBuildingActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        exploreBuildingActivity = (ExploreBuildingActivity) this.getActivity();
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thrid, container, false);
        tvTitle = view.findViewById(R.id.tv_title);
        view.findViewById(R.id.iv_close).setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
            if (exploreBuildingActivity != null) {
                exploreBuildingActivity.removeMarker();
            }
        });
        recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(Objects.requireNonNull(ResourcesCompat.getDrawable(getResources(), R.drawable.inset_recyclerview_divider, null)));
        recyclerView.addItemDecoration(dividerItemDecoration);

        poiSearch = PoiSearch.newInstance();
        poiSearch.setPoiSearchResultListener(poiSearchResultListener);
        return view;
    }

    private PoiSearch.PoiSearchResultListenerAdapter poiSearchResultListener = new PoiSearch.PoiSearchResultListenerAdapter() {
        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            if (poiResult.status != 0) {
                Toast.makeText(requireContext(), poiResult.error.toString(), Toast.LENGTH_LONG).show();
                return;
            }
            if (poiResult.getAllPoi() == null || poiResult.getAllPoi().isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.no_result), Toast.LENGTH_LONG).show();
                return;
            }
            PoiListAdapter adapter = new PoiListAdapter(poiResult.getAllPoi(), tvTitle.getText().toString().trim());
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener((floorName, poiName, catgegoryImage, position, v) -> {
                Bundle bundle = new Bundle();
                bundle.putString("poiName", poiName);
                bundle.putString("floorName", floorName);
                bundle.putInt("catgegoryImage", catgegoryImage);
                bundle.putString("buildingId", buildingId);
                Navigation.findNavController(v).navigate(R.id.action_thridFragment_to_fourthFragment, bundle);

                if (exploreBuildingActivity != null) {
                    exploreBuildingActivity.addMarker(poiResult.getAllPoi().get(position));
                }
            });
        }
    };

    protected void doSearchQuery(String category, String buildingId) {
        PoiInBuildingSearchOption inBuildingSearchOption = new PoiInBuildingSearchOption();
        inBuildingSearchOption.category(category);
        inBuildingSearchOption.buildingId(buildingId);
        poiSearch.searchInBuilding(inBuildingSearchOption);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            String category = arguments.getString("category");
            tvTitle.setText(arguments.getString("categoryName"));
            buildingId = arguments.getString("buildingId");
            if (category != null && buildingId != null) {
                doSearchQuery(category, buildingId);
            }
        }
    }

}