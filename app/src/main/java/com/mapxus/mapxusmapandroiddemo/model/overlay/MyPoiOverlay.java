package com.mapxus.mapxusmapandroiddemo.model.overlay;

import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.model.MapxusPointAnnotationOptions;
import com.mapxus.map.mapxusmap.api.services.model.poi.PoiInfo;

import java.util.List;

public class MyPoiOverlay {
    private MapboxMap mapboxMap;
    private MapxusMap mapxusMap;
    private List<PoiInfo> mPoiInfoList;

    public MyPoiOverlay(MapboxMap mapboxMap, MapxusMap mapxusMap, List<PoiInfo> poiInfos) {
        this.mapboxMap = mapboxMap;
        this.mapxusMap = mapxusMap;
        mPoiInfoList = poiInfos;
    }

    /**
     * 添加Marker到地图中。
     *
     * @since V2.1.0
     */
    public void addToMap() {
        for (int i = 0; i < mPoiInfoList.size(); i++) {
            mapxusMap.addMapxusPointAnnotation(getMarkerOptions(i));
        }
    }

    /**
     * 移动镜头到当前的视角。
     *
     * @since V2.1.0
     */
    public void zoomToSpan(double zoom) {
        if (mPoiInfoList != null && mPoiInfoList.size() > 0) {
            if (mapboxMap == null)
                return;
            if (mPoiInfoList.size() == 1) {
                PoiInfo firstPoiInfo = mPoiInfoList.get(0);
                mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(firstPoiInfo.getLocation().getLat(), firstPoiInfo.getLocation().getLon()),
                        zoom));
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
        mapxusMap.removeMapxusPointAnnotations();
    }

    private MapxusPointAnnotationOptions getMarkerOptions(int index) {
        return new MapxusPointAnnotationOptions()
                .setPosition(
                        new com.mapxus.map.mapxusmap.api.map.model.LatLng(mPoiInfoList.get(index).getLocation()
                                .getLat(), mPoiInfoList.get(index)
                                .getLocation().getLon()))
                .setFloorId(mPoiInfoList.get(index).getFloorId())
                .setTitle(getTitle(index)).setSnippet(getSnippet(index));
    }

    protected String getTitle(int index) {
        return mPoiInfoList.get(index).getNameDefault();
    }

    protected String getSnippet(int index) {
        return "floor:" + mPoiInfoList.get(index).getFloor();
    }


}