package com.mapxus.mapxusmapandroiddemo.utils;

public class GeoUtil {
    public static double earthRadius = 6371000.0D;

    public GeoUtil() {
    }

    public static double calculateBearing(double fromLat, double formLon, double toLat, double toLon) {
        double lat1 = Math.toRadians(fromLat);
        double lon1 = Math.toRadians(formLon);
        double lat2 = Math.toRadians(toLat);
        double lon2 = Math.toRadians(toLon);
        double y = Math.sin(lon2 - lon1) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1);
        double bearing = Math.atan2(y, x);
        bearing = Math.toDegrees(bearing);
        bearing = bearing < 0.0D ? bearing + 360.0D : bearing;
        return bearing;
    }
}