package com.mapxus.mapxusmapandroiddemo.examples.searchservices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.maps.MapboxMapOptions
import com.mapxus.map.mapxusmap.api.map.model.LatLng
import com.mapxus.map.mapxusmap.api.map.model.LatLngBounds
import com.mapxus.map.mapxusmap.api.services.CategorySearch
import com.mapxus.map.mapxusmap.api.services.model.BoundSearchOption
import com.mapxus.map.mapxusmap.api.services.model.SearchResult
import com.mapxus.map.mapxusmap.api.services.model.category.CategoryResult
import com.mapxus.mapxusmapandroiddemo.R
import com.mapxus.mapxusmapandroiddemo.base.BaseWithParamMenuActivity
import com.mapxus.mapxusmapandroiddemo.compose.MapxusMap
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SearchCategoryInBoundActivity : BaseWithParamMenuActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MapxusMap(
                    modifier = Modifier.fillMaxSize(),
                    mapboxMapOptions = MapboxMapOptions.createFromAttributes(this).apply {
                        this.camera(
                            CameraPosition.Builder()
                                .target(
                                    com.mapbox.mapboxsdk.geometry.LatLng(
                                        getString(R.string.default_lat).toDouble(),
                                        getString(R.string.default_lon).toDouble()
                                    )
                                )
                                .zoom(17.0)
                                .build()
                        )
                    },
                    onGetMapboxMap = {},
                    onGetMapxusMap = {}
                )
            }
        }
    }

    override fun initBottomSheetDialog() {
        val bottomSheet = MyBottomSheet()
        bottomSheet.show(supportFragmentManager, null)
    }
}

class MyBottomSheet : BottomSheetDialogFragment() {
    private var isShowingResult = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.bottomsheet_dialog_bound_search_category_style,
            container,
            false
        )

        view.findViewById<View>(R.id.create).setOnClickListener { v: View? ->
            if (!isShowingResult)
                lifecycleScope.launch { getValueAndSearch(view) }
            else dismiss()
        }
        return view
    }

    private suspend fun getValueAndSearch(bottomSheetDialogView: View) {
        val llSearchLayout =
            bottomSheetDialogView.findViewById<LinearLayout>(R.id.ll_search_layout)
        val resultListView = bottomSheetDialogView.findViewById<ComposeView>(R.id.list)

        val etKeywords = bottomSheetDialogView.findViewById<EditText>(R.id.et_keywords)

        val etMaxLat = bottomSheetDialogView.findViewById<EditText>(R.id.et_max_lat)
        val etMaxLon = bottomSheetDialogView.findViewById<EditText>(R.id.et_max_lon)
        val etMinLat = bottomSheetDialogView.findViewById<EditText>(R.id.et_min_lat)
        val etMinLon = bottomSheetDialogView.findViewById<EditText>(R.id.et_min_lon)

        val maxLat = etMaxLat.text.trim().toString().toDoubleOrNull() ?: 0.0
        val maxLon = etMaxLon.text.trim().toString().toDoubleOrNull() ?: 0.0
        val minLat = etMinLat.text.trim().toString().toDoubleOrNull() ?: 0.0
        val minLon = etMinLon.text.trim().toString().toDoubleOrNull() ?: 0.0

        val keyword = etKeywords.text.trim().takeIf { it.isNotEmpty() }?.toString()

        val southwest = LatLng(minLat, minLon)
        val northeast = LatLng(maxLat, maxLon)

        val latLngBounds = LatLngBounds.Builder()
            .include(southwest)
            .include(northeast)
            .build()

        val result = doSearchQuery(latLngBounds, keyword)
        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(requireContext(), result.errorMessage, Toast.LENGTH_SHORT).show()
            return
        }

        isShowingResult = true
        bottomSheetDialogView.findViewById<Button>(R.id.create).setText(R.string.close)
        llSearchLayout.visibility = View.GONE
        resultListView.visibility = View.VISIBLE

        resultListView.setContent {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                result.categoryGroups.forEach {
                    item {
                        Column(modifier = Modifier.padding(10.dp, 20.dp)) {
                            Text(text = "Category: " + it.category)
                            Text(text = "Title_en: ${it.title.en}")
                            Text(text = "Title_zhHans: ${it.title.zhHans}")
                            Text(text = "Title_zhHant: ${it.title.zhHant}")
                            Text(text = "Venue: " + it.venueNames.default)
                        }
                        Divider()
                    }
                }
            }
        }
    }

    private suspend fun doSearchQuery(
        latLngBounds: LatLngBounds,
        keyword: String?,
    ): CategoryResult =
        suspendCoroutine { continuation ->
            CategorySearch.newInstance().searchCategoriesByKeyword(
                BoundSearchOption().also {
                    it.bound(latLngBounds)
                    keyword?.let { kw -> it.keyword(kw) }
                }
            ) {
                continuation.resume(it)
            }
        }
}