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
import com.mapxus.map.mapxusmap.api.services.model.PoiCategorySearchOption;
import com.mapxus.map.mapxusmap.api.services.model.poi.PoiCategoryResult;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.adapter.CategoriesListAdapter;

import java.util.Objects;

public class SecondFragment extends Fragment {

    private TextView tvTitle;
    private PoiSearch poiSearch;
    private RecyclerView recyclerView;
    private String buildingId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        tvTitle = view.findViewById(R.id.tv_title);
        view.findViewById(R.id.iv_close).setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(Objects.requireNonNull(ResourcesCompat.getDrawable(getResources(), R.drawable.inset_recyclerview_divider, null)));
        recyclerView.addItemDecoration(dividerItemDecoration);

        poiSearch = PoiSearch.newInstance();
        poiSearch.setPoiSearchResultListener(poiSearchResultListener);
        return view;
    }

    private final PoiSearch.PoiSearchResultListenerAdapter poiSearchResultListener = new PoiSearch.PoiSearchResultListenerAdapter() {
        @Override
        public void onPoiCategoriesResult(PoiCategoryResult poiCategoryResult) {
            if (poiCategoryResult.status != 0) {
                Toast.makeText(requireContext(), poiCategoryResult.error.toString(), Toast.LENGTH_LONG).show();
                return;
            }
            if (poiCategoryResult.getPoiCategoryInfos() == null || poiCategoryResult.getPoiCategoryInfos().isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.no_result), Toast.LENGTH_LONG).show();
                return;
            }
            CategoriesListAdapter adapter = new CategoriesListAdapter(poiCategoryResult.getPoiCategoryInfos());
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener((category, categoryName, position, v) -> {
                Bundle bundle = new Bundle();
                bundle.putString("category", category);
                bundle.putString("categoryName", categoryName);
                bundle.putString("buildingId", buildingId);
                Navigation.findNavController(v).navigate(R.id.action_secondFragment_to_thridFragment, bundle);
            });
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            buildingId = arguments.getString("buildingId");
            tvTitle.setText(arguments.getString("buildingName"));
            if (buildingId != null) {
                searchAllPoiCategory(buildingId);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (poiSearch != null) {
            poiSearch.destroy();
        }
    }

    protected void searchAllPoiCategory(String buildingId) {
        PoiCategorySearchOption poiCategorySearchOption = new PoiCategorySearchOption();
        poiCategorySearchOption.buildingId(buildingId);
        poiSearch.searchPoiCategoryInSite(poiCategorySearchOption);
    }
}