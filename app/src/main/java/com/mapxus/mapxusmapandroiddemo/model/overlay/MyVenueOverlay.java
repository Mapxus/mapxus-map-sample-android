package com.mapxus.mapxusmapandroiddemo.model.overlay;

import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.model.MapxusPointAnnotationOptions;
import com.mapxus.map.mapxusmap.api.services.model.building.IndoorBuildingInfo;
import com.mapxus.map.mapxusmap.api.services.model.venue.VenueInfo;

import java.util.ArrayList;
import java.util.List;

public class MyVenueOverlay {
    private MapboxMap mapboxMap;
    private MapxusMap mapxusMap;
    private List<VenueInfo> mVenueInfo;

    public MyVenueOverlay(MapboxMap mapboxMap, MapxusMap mapxusMap, List<VenueInfo> venueInfos) {
        this.mapboxMap = mapboxMap;
        this.mapxusMap = mapxusMap;
        mVenueInfo = venueInfos;
    }

    /**
     * 添加Marker到地图中。
     *
     * @since V2.1.0
     */
    public void addToMap() {
        List<MapxusPointAnnotationOptions> options = new ArrayList<>();
        for (int i = 0; i < mVenueInfo.size(); i++) {
            options.add(getMarkerOptions(i));
        }
        mapxusMap.addMapxusPointAnnotations(options);
    }

    /**
     * 移动镜头到当前的视角。
     *
     * @since V2.1.0
     */
    public void zoomToSpan(double zoom) {
        if (mVenueInfo != null && !mVenueInfo.isEmpty()) {
            if (mapboxMap == null)
                return;
            if (mVenueInfo.size() == 1) {
                VenueInfo firstVenue = mVenueInfo.get(0);
                mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(firstVenue.getLabelCenter().getLat(), firstVenue.getLabelCenter().getLon()),
                        zoom));
            } else {
                LatLngBounds bounds = getLatLngBounds();
                mapboxMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
            }
        }
    }

    private LatLngBounds getLatLngBounds() {
        LatLngBounds.Builder b = new LatLngBounds.Builder();

        for (int i = 0; i < mVenueInfo.size(); i++) {
            VenueInfo venueInfo = mVenueInfo.get(i);
            b.include(new LatLng(venueInfo.getLabelCenter().getLat(),
                    venueInfo.getLabelCenter().getLon()));
            for (IndoorBuildingInfo indoorBuildingInfo : venueInfo.getBuildings()) {
                if (indoorBuildingInfo.getLabelCenter() == null) {
                    continue;
                }
                b.include(new LatLng(indoorBuildingInfo.getLabelCenter().getLat(),
                        indoorBuildingInfo.getLabelCenter().getLon()));
            }
        }
        return b.build();
    }


    public void removeFromMap() {
        mapxusMap.removeMapxusPointAnnotations();
    }


    private MapxusPointAnnotationOptions getMarkerOptions(int index) {

        VenueInfo venueInfo = mVenueInfo.get(index);
        return new MapxusPointAnnotationOptions()
                .setPosition(
                        new com.mapxus.map.mapxusmap.api.map.model.LatLng(venueInfo.getLabelCenter()
                                .getLat(), venueInfo.getLabelCenter().getLon()))
                .setTitle(getTitle(index));
    }

    protected String getTitle(int index) {
        String name = mVenueInfo.get(index).getNameMap().getDefault() == null ? mVenueInfo.get(index).getNameMap().getEn() : mVenueInfo.get(index).getNameMap().getDefault();
        if (name != null) {
            return name;
        } else {
            return "";
        }
    }
}