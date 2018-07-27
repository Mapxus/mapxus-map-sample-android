package com.mapxus.mapxusmapandroiddemo.model.overlay;

import java.util.ArrayList;
import java.util.List;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapxus.services.model.poi.PoiInfo;

public class MyPoiOverlay {
    private MapboxMap mapboxMap;
    private List<PoiInfo> mPoiInfoList;
    private ArrayList<ObjectMarker> mPoiMarks = new ArrayList();

    public MyPoiOverlay(MapboxMap mapboxMap, List<PoiInfo> poiInfos) {
        this.mapboxMap = mapboxMap;
        mPoiInfoList = poiInfos;
    }

    /**
     * 添加Marker到地图中。
     *
     * @since V2.1.0
     */
    public void addToMap() {
        for (int i = 0; i < mPoiInfoList.size(); i++) {
            Marker marker = mapboxMap.addMarker(getMarkerOptions(i));
            mPoiMarks.add((ObjectMarker) marker);
        }
    }

    /**
     * 移动镜头到当前的视角。
     *
     * @since V2.1.0
     */
    public void zoomToSpan() {
        if (mPoiInfoList != null && mPoiInfoList.size() > 0) {
            if (mapboxMap == null)
                return;
            if (mPoiInfoList.size() == 1) {
                PoiInfo firstPoiInfo = mPoiInfoList.get(0);
                mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(firstPoiInfo.getLocation().getLat(), firstPoiInfo.getLocation().getLon()),
                        17));
            } else {
                LatLngBounds bounds = getLatLngBounds();
                mapboxMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            }

        }
    }

    private LatLngBounds getLatLngBounds() {
        LatLngBounds.Builder b = new LatLngBounds.Builder();
        for (int i = 0; i < mPoiInfoList.size(); i++) {
            b.include(new LatLng(mPoiInfoList.get(i).getLocation().getLat(),
                    mPoiInfoList.get(i).getLocation().getLon()));
        }
        return b.build();
    }


    public void removeFromMap() {
        for (Marker mark : mPoiMarks) {
            mark.remove();
        }
    }


    private ObjectMarkerOptions getMarkerOptions(int index) {
        return new ObjectMarkerOptions()
                .position(
                        new LatLng(mPoiInfoList.get(index).getLocation()
                                .getLat(), mPoiInfoList.get(index)
                                .getLocation().getLon()))
                .title(getTitle(index)).snippet(getSnippet(index))
                .object(mPoiInfoList.get(index));
    }

    protected String getTitle(int index) {
        return mPoiInfoList.get(index).getName().get("default");
    }

    protected String getSnippet(int index) {
        return "floor:" + mPoiInfoList.get(index).getFloor();
    }


}