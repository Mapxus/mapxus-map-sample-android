package com.mapxus.mapxusmapandroiddemo.model.overlay;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.services.model.building.Address;
import com.mapxus.map.mapxusmap.api.services.model.building.IndoorBuildingInfo;

import java.util.List;

public class MyIndoorBuildingOverlay {
    private MapboxMap mapboxMap;
    private MapxusMap mapxusMap;
    private List<IndoorBuildingInfo> mIndoorBuildingInfos;

    public MyIndoorBuildingOverlay(MapboxMap mapboxMap, MapxusMap mapxusMap, List<IndoorBuildingInfo> indoorBuildingInfos) {
        this.mapboxMap = mapboxMap;
        this.mapxusMap = mapxusMap;
        mIndoorBuildingInfos = indoorBuildingInfos;
    }

    /**
     * 添加Marker到地图中。
     *
     * @since V2.1.0
     */
    public void addToMap() {
        for (int i = 0; i < mIndoorBuildingInfos.size(); i++) {
            mapboxMap.addMarker(getMarkerOptions(i));
        }
    }

    /**
     * 移动镜头到当前的视角。
     *
     * @since V2.1.0
     */
    public void zoomToSpan() {
        if (mIndoorBuildingInfos != null && mIndoorBuildingInfos.size() > 0) {
            if (mapboxMap == null)
                return;
            if (mIndoorBuildingInfos.size() == 1) {
                IndoorBuildingInfo firstIndoorBuildingInfo = mIndoorBuildingInfos.get(0);
                mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(firstIndoorBuildingInfo.getLabelCenter().getLat(), firstIndoorBuildingInfo.getLabelCenter().getLon()),
                        16));
            } else {
                LatLngBounds bounds = getLatLngBounds();
                mapboxMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            }

        }
    }

    private LatLngBounds getLatLngBounds() {
        LatLngBounds.Builder b = new LatLngBounds.Builder();
        for (int i = 0; i < mIndoorBuildingInfos.size(); i++) {
            b.include(new LatLng(mIndoorBuildingInfos.get(i).getLabelCenter().getLat(),
                    mIndoorBuildingInfos.get(i).getLabelCenter().getLon()));
        }
        return b.build();
    }


    public void removeFromMap() {
        mapxusMap.removeMarkers();
    }


    private MarkerOptions getMarkerOptions(int index) {

        IndoorBuildingInfo indoorBuildingInfo = mIndoorBuildingInfos.get(index);
        return new MarkerOptions()
                .position(
                        new LatLng(indoorBuildingInfo.getLabelCenter()
                                .getLat(), indoorBuildingInfo.getLabelCenter().getLon()))
                .title(getTitle(index)).snippet(getSnippet(index));
    }

    protected String getTitle(int index) {
        String name = mIndoorBuildingInfos.get(index).getName().get("default") == null ? mIndoorBuildingInfos.get(index).getName().get("en") : mIndoorBuildingInfos.get(index).getName().get("default");
        if (name != null) {
            return name;
        } else {
            return "";
        }
    }

    protected String getSnippet(int index) {
        Address address = mIndoorBuildingInfos.get(index).getAddress().get("default") == null ? mIndoorBuildingInfos.get(index).getAddress().get("en") : mIndoorBuildingInfos.get(index).getAddress().get("default");
        if (address != null) {
            return address.getStreet();
        } else {
            return "";
        }
    }
}