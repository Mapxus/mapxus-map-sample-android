package com.mapxus.mapxusmapandroiddemo.model.overlay;

import android.content.Context;

import com.mapbox.android.gestures.RotateGestureDetector;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerView;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapxus.mapxusmapandroiddemo.utils.GeoUtil;
import com.mapxus.map.MapxusMap;
import com.mapxus.map.model.IndoorBuilding;
import com.mapxus.map.model.LatLng;
import com.mapxus.services.model.planning.RouteCoordinateDto;
import com.mapxus.services.model.planning.RouteLegDto;
import com.mapxus.services.model.planning.RoutePlanningPoint;
import com.mapxus.services.model.planning.RouteResponseDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WalkRouteOverlay extends RouteOverlay {

    private static final Logger LOGGER = LoggerFactory.getLogger(WalkRouteOverlay.class);

    private RouteResponseDto routeResponseDto;

    private List<LatLng> allNavigationPoints = new ArrayList<>();

    private HashMap<String, HashMap<String, List<List<LatLng>>>> indoorAllFloorNavigationPoints = new HashMap<>(); // buildingId --> (floor --> latLon)

    private List<LatLng> outDoorNavigationPoints = new ArrayList<>();

    public WalkRouteOverlay(Context context, MapboxMap mapboxMap, MapxusMap mapxusMap, RouteResponseDto path,
                            RoutePlanningPoint start, RoutePlanningPoint end) {
        super(context);
        this.mapboxMap = mapboxMap;
        this.mapxusMap = mapxusMap;
        this.routeResponseDto = path;
        startPoint = start;
        endPoint = end;
        startLatLng = new LatLng(start.getLat(), start.getLon());
        endLatLng = new LatLng(end.getLat(), end.getLon());

    }


    public void addToMap() {

        convertNavigationResult(routeResponseDto);
        initListener();
        showTrace();

    }

    public void removeFromMap() {
        super.removeFromMap();
        mapboxMap.removeOnRotateListener(onRotateListener);
        mapxusMap.removeOnFloorChangeListener(onFloorChangeListener);
    }


    private void convertNavigationResult(RouteResponseDto routeResponseDto) {
        //先默认使用查询回来的第一条路线
        LOGGER.info("Convert the query result");
        for (RouteLegDto routeLegDto : routeResponseDto.getRoutes().get(0).getLegs()) {
            double distance = routeLegDto.getDistance();
            double time = routeLegDto.getDuration();
            LOGGER.info("Distance {}, time {}", distance, time);
            String lastFloor = null;
            for (RouteCoordinateDto coordinateDto : routeLegDto.getCoordinates()) {
                LatLng latLng = new LatLng(coordinateDto.getLat(), coordinateDto.getLon());
                String buildingId = coordinateDto.getBuildingId();
                String floor = coordinateDto.getFloor();
                allNavigationPoints.add(latLng);
                if (buildingId != null && floor != null) { //缺失两个信息之一都认为是室外的
                    if (indoorAllFloorNavigationPoints.containsKey(buildingId)) {
                        if (indoorAllFloorNavigationPoints.get(buildingId).containsKey(floor)) {
                            if (floor.equals(lastFloor)) {
                                int index = indoorAllFloorNavigationPoints.get(buildingId).get(floor).size() - 1; //在最后一个添加
                                indoorAllFloorNavigationPoints.get(buildingId).get(floor).get(index).add(latLng);
                            } else {
                                List<LatLng> points = new ArrayList<>(); //楼层变化则新创建新的路线
                                points.add(latLng);
                                indoorAllFloorNavigationPoints.get(buildingId).get(floor).add(points);
                            }
                        } else {
                            List<List<LatLng>> floorPoints = new ArrayList<>();
                            List<LatLng> points = new ArrayList<>();
                            points.add(latLng);
                            floorPoints.add(points);
                            indoorAllFloorNavigationPoints.get(buildingId).put(floor, floorPoints);
                        }
                    } else {
                        //没有当前建筑，创建新的list
                        HashMap<String, List<List<LatLng>>> buildingFloorPoints = new HashMap<>();
                        List<List<LatLng>> floorPoints = new ArrayList<>();
                        List<LatLng> points = new ArrayList<>();
                        points.add(latLng);
                        floorPoints.add(points);
                        buildingFloorPoints.put(floor, floorPoints);
                        indoorAllFloorNavigationPoints.put(buildingId, buildingFloorPoints);
                    }
                    lastFloor = floor;
                } else {
                    outDoorNavigationPoints.add(new LatLng(coordinateDto.getLat(), coordinateDto.getLon()));
                }
            }
        }
    }

    private void showTrace() {
        //室内点与室外点路径分别画出，当显示一个室内建筑时查询
        super.removeFromMap();//
        addStartAndEndMarker();
        if (outDoorNavigationPoints.size() > 0) {
            drawNavigationPolyline(outDoorNavigationPoints, false);//画室外路线
        }
        if (mapxusMap.getCurrentIndoorBuilding() != null) { //导航路线有包含当前楼层的路线则画出
            if (indoorAllFloorNavigationPoints.containsKey(mapxusMap.getCurrentIndoorBuilding().getBuildingId()) && indoorAllFloorNavigationPoints
                    .get(mapxusMap.getCurrentIndoorBuilding().getBuildingId()).containsKey(mapxusMap.getCurrentFloor())) {
                for (List<LatLng> latLngs : indoorAllFloorNavigationPoints
                        .get(mapxusMap.getCurrentIndoorBuilding().getBuildingId()).get(mapxusMap.getCurrentFloor())) {
                    drawNavigationPolyline(latLngs, true); //同一楼层不同路线
                }
            }
        }
        zoomToSpan();
    }


    private void drawNavigationPolyline(List<LatLng> polylineList, boolean isIndoor) {

        if (polylineList.size() < 0) {//需要划线的点位空，不需要划线
            return;
        }

        if (!polylineList.get(0).equals(startLatLng) && isIndoor && polylineList.size() > 2) { //室内分层路线的起点
            double bearing = GeoUtil.calculateBearing(polylineList.get(0).latitude, polylineList.get(0).longitude,
                    polylineList.get(1).latitude, polylineList.get(1).longitude);
            float rotateAngle = (float) (bearing - mapboxMap.getCameraPosition().bearing);
            MarkerViewOptions floorStartMarkerOptions = new MarkerViewOptions()
                    .icon(IconFactory.getInstance(mContext).fromBitmap(getFloorStartBitmapDescriptor().getBitmap()))
                    .position(new com.mapbox.mapboxsdk.geometry.LatLng(polylineList.get(0).getLatitude(), polylineList.get(0).getLongitude()))
                    .rotation(rotateAngle).anchor(0.5f, 0.5f).snippet(bearing + "");
            addFloorStartMarker(floorStartMarkerOptions);
        }

        if (polylineList.size() > 3) { //画经过点
            for (int i = 1; i < polylineList.size() - 1; i++) {
                double bearing = GeoUtil.calculateBearing(polylineList.get(i).latitude, polylineList.get(i).longitude,
                        polylineList.get(i + 1).latitude, polylineList.get(i + 1).longitude);
                MarkerViewOptions stationMarkerOptions = new MarkerViewOptions()
                        .icon(IconFactory.getInstance(mContext).fromBitmap(getStationBitmapDescriptor().getBitmap()))
                        .position(new com.mapbox.mapboxsdk.geometry.LatLng(polylineList.get(i).getLatitude(), polylineList.get(i).getLongitude()))
                        .rotation((float) (bearing - mapboxMap.getCameraPosition().bearing)).anchor(0.5f, 0.5f).snippet(bearing + "");
                addStationMarker(stationMarkerOptions);
            }
        }

        //画线
        PolylineOptions polylineOptions = new PolylineOptions().addAll(convertMapboxLatLngList(polylineList)).color(getLineColor()).width(getLineWidth());
        addPolyLine(polylineOptions);
    }

    private List<com.mapbox.mapboxsdk.geometry.LatLng> convertMapboxLatLngList(List<LatLng> latLngList) {
        List<com.mapbox.mapboxsdk.geometry.LatLng> mapboxLatLngList = new ArrayList<>(latLngList.size());

        for (LatLng latLng : latLngList) {
            com.mapbox.mapboxsdk.geometry.LatLng mapboxLatlng = new com.mapbox.mapboxsdk.geometry.LatLng(latLng.getLatitude(), latLng.getLongitude());
            mapboxLatLngList.add(mapboxLatlng);
        }
        return mapboxLatLngList;

    }

    private void initListener() {

        mapboxMap.addOnRotateListener(onRotateListener);

        mapxusMap.addOnFloorChangeListener(onFloorChangeListener);
    }

    private MapboxMap.OnRotateListener onRotateListener = new MapboxMap.OnRotateListener() {
        @Override
        public void onRotateBegin(RotateGestureDetector detector) {

        }

        @Override
        public void onRotate(RotateGestureDetector detector) {

            for (MarkerView marker : stationMarkers) {
                marker.setRotation((float) (Float.valueOf(marker.getSnippet()) - mapboxMap.getCameraPosition().bearing));
            }
            if (floorStartMarker != null) {
                floorStartMarker.setRotation((float) (Float.valueOf(floorStartMarker.getSnippet()) - mapboxMap.getCameraPosition().bearing));
            }
        }

        @Override
        public void onRotateEnd(RotateGestureDetector detector) {

        }
    };

    private MapxusMap.OnFloorChangeListener onFloorChangeListener = new MapxusMap.OnFloorChangeListener() {
        @Override
        public void onFloorChange(IndoorBuilding indoorBuilding, String s) {

            showTrace();

        }
    };


}
