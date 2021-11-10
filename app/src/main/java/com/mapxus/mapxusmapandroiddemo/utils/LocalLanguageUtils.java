package com.mapxus.mapxusmapandroiddemo.utils;

import com.mapxus.map.mapxusmap.api.map.model.IndoorBuilding;
import com.mapxus.map.mapxusmap.api.services.model.building.IndoorBuildingInfo;
import com.mapxus.map.mapxusmap.api.services.model.poi.PoiCategoryInfo;
import com.mapxus.map.mapxusmap.api.services.model.poi.PoiInfo;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Edison on 2020/9/7.
 * Describe:
 */
public class LocalLanguageUtils {

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
        switch (localeLanguage) {
            case "en":
                return poiInfo.getNameEn();
            case "zh":
                if (locale.getCountry().equals("CN")) {
                    return poiInfo.getNameCn();
                } else {
                    return poiInfo.getNameZh();
                }
            case "ja":
                return poiInfo.getNameJa();
            case "ko":
                return poiInfo.getNameKo();
            default:
                return poiInfo.getNameDefault();
        }
    }

    /**
     * for indoorbuildingInfo
     */
    public static String getLocalLanguageWithIndoorBuildingInfo(IndoorBuildingInfo indoorBuilding) {
        Locale locale = Locale.getDefault();
        String localeLanguage = Locale.getDefault().getLanguage();
        switch (localeLanguage) {
            case "en":
                return indoorBuilding.getNameEn();
            case "zh":
                if (locale.getCountry().equals("CN")) {
                    return indoorBuilding.getNameCn();
                } else {
                    return indoorBuilding.getNameZh();
                }
            case "ja":
                return indoorBuilding.getNameJa();
            case "ko":
                return indoorBuilding.getNameKo();
            default:
                return indoorBuilding.getNameDefault();
        }
    }

    /**
     * for indoorbuilding
     */
    public static String getLocalLanguageWithIndoorBuilding(IndoorBuilding indoorBuilding) {
        Locale locale = Locale.getDefault();
        String localeLanguage = Locale.getDefault().getLanguage();
        switch (localeLanguage) {
            case "en":
                return indoorBuilding.getNameEn();
            case "zh":
                if (locale.getCountry().equals("CN")) {
                    return indoorBuilding.getNameCn();
                } else {
                    return indoorBuilding.getNameZh();
                }
            case "ja":
                return indoorBuilding.getNameJa();
            case "ko":
                return indoorBuilding.getNameKo();
            default:
                return indoorBuilding.getBuildingName();
        }
    }

    /**
     * for address
     */
    public static String getLocalLanguageWithAddress(IndoorBuildingInfo indoorBuildingInfo) {
        Locale locale = Locale.getDefault();
        String localeLanguage = Locale.getDefault().getLanguage();
        switch (localeLanguage) {
            case "zh":
                if (locale.getCountry().equals("CN")) {
                    return Objects.requireNonNull(indoorBuildingInfo.getAddressCn()).getStreet();
                } else {
                    return Objects.requireNonNull(indoorBuildingInfo.getAddressZh()).getStreet();
                }
            case "ja":
                return Objects.requireNonNull(indoorBuildingInfo.getAddressJa()).getStreet();
            case "ko":
                return Objects.requireNonNull(indoorBuildingInfo.getAddressKo()).getStreet();
            default:
                return Objects.requireNonNull(indoorBuildingInfo.getAddressEn()).getStreet();
        }
    }
}
