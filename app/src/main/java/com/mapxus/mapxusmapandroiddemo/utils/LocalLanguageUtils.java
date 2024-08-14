package com.mapxus.mapxusmapandroiddemo.utils;

import android.text.TextUtils;

import com.mapxus.map.mapxusmap.api.map.model.IndoorBuilding;
import com.mapxus.map.mapxusmap.api.services.constant.RoutePlanningLocale;
import com.mapxus.map.mapxusmap.api.services.model.building.Address;
import com.mapxus.map.mapxusmap.api.services.model.building.IndoorBuildingInfo;
import com.mapxus.map.mapxusmap.api.services.model.poi.PoiCategoryInfo;
import com.mapxus.map.mapxusmap.api.services.model.poi.PoiInfo;

import java.util.Locale;
import java.util.Map;

/**
 * Created by Edison on 2020/9/7.
 * Describe:
 */
public class LocalLanguageUtils {

    /**
     * for route planning
     *
     * @return RoutePlanningLocale
     */
    public static String getLocalLanguage() {
        Locale locale = Locale.getDefault();
        String localeLanguage = Locale.getDefault().getLanguage();
        String routePlanningLocale;
        switch (localeLanguage) {
            case "zh":
                if (locale.getCountry().equals("CN")) {
                    routePlanningLocale = RoutePlanningLocale.ZH_CN;
                } else {
                    routePlanningLocale = RoutePlanningLocale.ZH_HK;
                }
                break;
            case "ja":
                routePlanningLocale = RoutePlanningLocale.JA;
                break;
            case "ko":
                routePlanningLocale = RoutePlanningLocale.KO;
                break;
            default:
                routePlanningLocale = RoutePlanningLocale.EN;
                break;
        }

        return routePlanningLocale;
    }


    /**
     * common
     */
    public static String getLocalLanguage(Map<String, String> names) {
        Locale locale = Locale.getDefault();
        String localeLanguage = Locale.getDefault().getLanguage();
        switch (localeLanguage) {
            case "zh":
                if (locale.getCountry().equals("CN")) {
                    return names.get("zh-Hans");
                } else {
                    return names.get("zh-Hant");
                }
            case "ja":
                return names.get("ja");
            case "ko":
                return names.get("ko");
            default:
                return names.get("en");
        }
    }

    /**
     * for PoiCategoryInfo
     */
    public static String getLocalLanguageWithPoiCategoryInfo(PoiCategoryInfo poiInfo) {
        Locale locale = Locale.getDefault();
        String localeLanguage = Locale.getDefault().getLanguage();
        if ("zh".equals(localeLanguage)) {
            if (locale.getCountry().equals("CN")) {
                return poiInfo.getTitleCn();
            } else {
                return poiInfo.getTitleZh();
            }
        }
        return poiInfo.getTitleEn();
    }

    /**
     * for PoiInfo
     */
    public static String getLocalLanguageWithPoiInfo(PoiInfo poiInfo) {
        Locale locale = Locale.getDefault();
        String localeLanguage = Locale.getDefault().getLanguage();
        String showName = "";
        switch (localeLanguage) {
            case "en":
                if (poiInfo.getNameMap().getEn() != null) {
                    showName = poiInfo.getNameMap().getEn();
                }
                break;
            case "zh":
                if (locale.getCountry().equals("CN")) {
                    if (poiInfo.getNameMap().getZhHans() != null) {
                        showName = poiInfo.getNameMap().getZhHans();
                    }
                } else {
                    if (poiInfo.getNameMap().getZhHant() != null) {
                        showName = poiInfo.getNameMap().getZhHant();
                    }
                }
                break;
            case "ja":
                if (poiInfo.getNameMap().getJa() != null) {
                    showName = poiInfo.getNameMap().getJa();
                }
                break;
            case "ko":
                if (poiInfo.getNameMap().getKo() != null) {
                    showName = poiInfo.getNameMap().getKo();
                }
                break;
            default:
                if (poiInfo.getNameMap().getDefault() != null) {
                    showName = poiInfo.getNameMap().getDefault();
                }
                break;
        }

        if (TextUtils.isEmpty(showName)) {
            return TextUtils.isEmpty(poiInfo.getNameMap().getDefault()) ? "" : poiInfo.getNameMap().getDefault();
        } else {
            return showName;
        }
    }

    /**
     * for indoorbuildingInfo
     */
    public static String getLocalLanguageWithIndoorBuildingInfo(IndoorBuildingInfo indoorBuilding) {
        Locale locale = Locale.getDefault();
        String localeLanguage = Locale.getDefault().getLanguage();
        String buildingName = "";
        switch (localeLanguage) {
            case "en":
                buildingName = indoorBuilding.getNameMap().getEn();
                break;
            case "zh":
                if (locale.getCountry().equals("CN")) {
                    buildingName = indoorBuilding.getNameMap().getZhHans();
                } else {
                    buildingName = indoorBuilding.getNameMap().getZhHant();
                }
                break;
            case "ja":
                buildingName = indoorBuilding.getNameMap().getJa();
                break;
            case "ko":
                buildingName = indoorBuilding.getNameMap().getKo();
                break;
            default:
                buildingName = indoorBuilding.getNameMap().getDefault();
                break;
        }

        if (TextUtils.isEmpty(buildingName)) {
            buildingName = indoorBuilding.getNameMap().getDefault();
        }

        return buildingName;
    }

    /**
     * for indoorbuilding
     */
    public static String getLocalLanguageWithIndoorBuilding(IndoorBuilding indoorBuilding) {
        Locale locale = Locale.getDefault();
        String localeLanguage = Locale.getDefault().getLanguage();
        String buildingName = "";
        switch (localeLanguage) {
            case "en":
                buildingName = indoorBuilding.getBuildingNameMap().getEn();
                break;
            case "zh":
                if (locale.getCountry().equals("CN")) {
                    buildingName = indoorBuilding.getBuildingNameMap().getZhHans();
                } else {
                    buildingName = indoorBuilding.getBuildingNameMap().getZhHant();
                }
                break;
            case "ja":
                buildingName = indoorBuilding.getBuildingNameMap().getJa();
                break;
            case "ko":
                buildingName = indoorBuilding.getBuildingNameMap().getKo();
                break;
            default:
                buildingName = indoorBuilding.getBuildingNameMap().getDefault();
                break;
        }

        if (TextUtils.isEmpty(buildingName)) {
            buildingName = indoorBuilding.getBuildingNameMap().getDefault();
        }

        return buildingName;
    }

    /**
     * for address
     */
    public static String getLocalLanguageWithAddress(IndoorBuildingInfo indoorBuildingInfo) {
        Locale locale = Locale.getDefault();
        String localeLanguage = Locale.getDefault().getLanguage();

        String street = "";
        switch (localeLanguage) {
            case "zh":
                String country = locale.getCountry();
                Address addressCn = indoorBuildingInfo.getAddressMap().getZhHans();
                Address addressZh = indoorBuildingInfo.getAddressMap().getZhHant();
                if (country.equals("CN") && addressCn != null) {
                    street = addressCn.getStreet();
                } else if (addressZh != null) {
                    street = addressZh.getStreet();
                }
                break;
            case "ja":
                Address addressJa = indoorBuildingInfo.getAddressMap().getJa();
                if (addressJa != null) {
                    street = addressJa.getStreet();
                }
                break;
            case "ko":
                Address addressKo = indoorBuildingInfo.getAddressMap().getKo();
                if (addressKo != null) {
                    street = addressKo.getStreet();
                }
                break;
            default:
                street = "";
                break;
        }
        return street;
    }
}
