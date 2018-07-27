package com.mapxus.mapxusmapandroiddemo.model.overlay;

import android.content.Context;

import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.MarkerView;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.map.MapxusMap;
import com.mapxus.map.model.LatLng;
import com.mapxus.map.model.overlay.BitmapDescriptor;
import com.mapxus.map.model.overlay.BitmapDescriptorFactory;
import com.mapxus.services.model.planning.RoutePlanningPoint;

import java.util.ArrayList;
import java.util.List;

public class RouteOverlay {
    protected List<MarkerView> stationMarkers = new ArrayList();
    protected List<Polyline> allPolyLines = new ArrayList<Polyline>();
    protected Marker startMarker;
    protected Marker endMarker;
    protected MarkerView floorStartMarker;
    protected RoutePlanningPoint startPoint;
    protected RoutePlanningPoint endPoint;
    protected LatLng startLatLng;
    protected LatLng endLatLng;
    protected LatLng floorStartLatLng;
    protected MapboxMap mapboxMap;
    protected MapxusMap mapxusMap;
    protected Context mContext;
    protected boolean nodeIconVisible = true;


    public RouteOverlay(Context context) {
        mContext = context;
    }


    public void removeFromMap() {
        if (startMarker != null) {
            startMarker.remove();
            startMarker = null;

        }
        if (endMarker != null) {
            endMarker.remove();
            endMarker = null;
        }
        if (floorStartMarker != null) {
            floorStartMarker.remove();
            floorStartMarker = null;
        }
        for (Marker marker : stationMarkers) {
            marker.remove();
        }
        stationMarkers.clear();
        for (Polyline line : allPolyLines) {
            line.remove();
        }
        allPolyLines.clear();
    }


    protected BitmapDescriptor getStartBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.drawable.ic_start_point);
    }

    protected BitmapDescriptor getEndBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.drawable.ic_end_point);
    }

    protected BitmapDescriptor getStationBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.drawable.ic_path_point);
    }

    protected BitmapDescriptor getFloorStartBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.drawable.ic_path_start_point);
    }


    protected void addStartAndEndMarker() {
        if (startMarker == null && startPoint.getFloor().equals(mapxusMap.getCurrentFloor())) {
            startMarker = mapboxMap.addMarker((new MarkerOptions())
                    .position(new com.mapbox.mapboxsdk.geometry.LatLng(startLatLng.getLatitude(), startLatLng.getLongitude()))
                    .icon(IconFactory.getInstance(mContext).fromBitmap(getStartBitmapDescriptor().getBitmap()))
                    .title("起点"));
        }
//        if (startMarker != null && !startPoint.getFloor().equals(beeMap.getCurrentFloor())) {
//            startMarker.remove();
//            startMarker = null;
//        }

        if (endMarker == null && endPoint.getFloor().equals(mapxusMap.getCurrentFloor())) {
            endMarker = mapboxMap.addMarker((new MarkerOptions())
                    .position(new com.mapbox.mapboxsdk.geometry.LatLng(endLatLng.getLatitude(), endLatLng.getLongitude()))
                    .icon(IconFactory.getInstance(mContext).fromBitmap(getEndBitmapDescriptor().getBitmap()))
                    .title("终点"));
        }
//        if (endMarker != null && !endPoint.getFloor().equals(beeMap.getCurrentFloor())) {
//            endMarker.remove();
//            endMarker = null;
//        }
    }

    /**
     * 移动镜头到当前的视角。
     *
     * @since V2.1.0
     */
    public void zoomToSpan() {
        if (startMarker != null || floorStartMarker != null) {
            if (mapboxMap == null)
                return;
            try {
                com.mapbox.mapboxsdk.geometry.LatLngBounds bounds = getLatLngBounds();
                mapboxMap.animateCamera(CameraUpdateFactory
                        .newLatLngBounds(bounds, 100));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    protected com.mapbox.mapboxsdk.geometry.LatLngBounds getLatLngBounds() {
        com.mapbox.mapboxsdk.geometry.LatLngBounds.Builder b = new com.mapbox.mapboxsdk.geometry.LatLngBounds.Builder();
        if (floorStartMarker != null) {
            b.include(floorStartMarker.getPosition());
        } else if (startMarker != null) {
            b.include(startMarker.getPosition());
        }
        for (Marker stationMarker : stationMarkers) {
            b.include(stationMarker.getPosition());
        }
        if (endMarker != null) {
            b.include(endMarker.getPosition());
        }
        return b.build();
    }


    protected void addStationMarker(MarkerViewOptions options) {
        if (options == null) {
            return;
        }
        MarkerView marker = mapboxMap.addMarker(options);
        if (marker != null) {
            stationMarkers.add(marker);
        }

    }

    protected void addFloorStartMarker(MarkerViewOptions options) {
        if (options == null) {
            return;
        }
        floorStartMarker = mapboxMap.addMarker(options);
    }

    protected void addPolyLine(PolylineOptions options) {
        if (options == null) {
            return;
        }
        Polyline polyline = mapboxMap.addPolyline(options);
        if (polyline != null) {
            allPolyLines.add(polyline);
        }
    }

    protected float getLineWidth() {
        return 1;
    }

    protected int getLineColor() {
        return R.color.distance_text_color;
    }

}
