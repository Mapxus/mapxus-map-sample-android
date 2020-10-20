package com.mapxus.mapxusmapandroiddemo.model.overlay;

import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.model.MapxusMarkerOptions;
import com.mapxus.map.mapxusmap.api.services.model.poi.PoiOrientationInfo;

import java.util.List;

public class MyPoiOrientationOverlay {
    private MapboxMap mapboxMap;
    private MapxusMap mapxusMap;
    private List<PoiOrientationInfo> mPoiInfoList;

    public MyPoiOrientationOverlay(MapboxMap mapboxMap, MapxusMap mapxusMap, List<PoiOrientationInfo> poiInfos) {
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
            mapxusMap.addMarker(getMarkerOptions(i));
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
                PoiOrientationInfo firstPoiInfo = mPoiInfoList.get(0);
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
        mapxusMap.removeMarkers();
    }

    private MapxusMarkerOptions getMarkerOptions(int index) {
        return new MapxusMarkerOptions()
                .setPosition(
                        new com.mapxus.map.mapxusmap.api.map.model.LatLng(mPoiInfoList.get(index).getLocation()
                                .getLat(), mPoiInfoList.get(index)
                                .getLocation().getLon()))
                .setFloor(mPoiInfoList.get(index).getFloor())
                .setBuildingId(mPoiInfoList.get(index).getBuildingId())
                .setTitle(getTitle(index)).setSnippet(getSnippet(index));
    }

    protected String getTitle(int index) {
        return mPoiInfoList.get(index).getName().get("default");
    }

    protected String getSnippet(int index) {
        int angle = mPoiInfoList.get(index).getAngle();
        if (angle <= 45 || angle >= 315) {
            return "in the front";
        } else if (angle <= 135) {
            return "on the right";
        } else if (angle <= 225) {
            return "in the back";
        } else {
            return "on the left";
        }
    }
}