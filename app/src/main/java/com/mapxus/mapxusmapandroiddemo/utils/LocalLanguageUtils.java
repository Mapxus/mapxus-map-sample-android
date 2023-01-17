package com.mapxus.mapxusmapandroiddemo.utils;

import android.text.TextUtils;

import com.mapxus.map.mapxusmap.api.map.model.IndoorBuilding;
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
                if (poiInfo.getNameEn() != null) {
                    showName = poiInfo.getNameEn();
                }
                break;
            case "zh":
                if (locale.getCountry().equals("CN")) {
                    if (poiInfo.getNameCn() != null) {
                        showName = poiInfo.getNameCn();
                    }
                } else {
                    if (poiInfo.getNameZh() != null) {
                        showName = poiInfo.getNameZh();
                    }
                }
                break;
            case "ja":
                if (poiInfo.getNameJa() != null) {
                    showName = poiInfo.getNameJa();
                }
                break;
            case "ko":
                if (poiInfo.getNameKo() != null) {
                    showName = poiInfo.getNameKo();
                }
                break;
            default:
                if (poiInfo.getNameDefault() != null) {
                    showName = poiInfo.getNameDefault();
                }
                break;
        }

        if (TextUtils.isEmpty(showName)) {
            return TextUtils.isEmpty(poiInfo.getNameDefault()) ? "" : poiInfo.getNameDefault();
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
                buildingName = indoorBuilding.getNameEn();
                break;
            case "zh":
                if (locale.getCountry().equals("CN")) {
                    buildingName = indoorBuilding.getNameCn();
                } else {
                    buildingName = indoorBuilding.getNameZh();
                }
                break;
            case "ja":
                buildingName = indoorBuilding.getNameJa();
                break;
            case "ko":
                buildingName = indoorBuilding.getNameKo();
                break;
            default:
                buildingName = indoorBuilding.getNameDefault();
                break;
        }

        if (TextUtils.isEmpty(buildingName)) {
            buildingName = indoorBuilding.getNameDefault();
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
                buildingName = indoorBuilding.getNameEn();
                break;
            case "zh":
                if (locale.getCountry().equals("CN")) {
                    buildingName = indoorBuilding.getNameCn();
                } else {
                    buildingName = indoorBuilding.getNameZh();
                }
                break;
            case "ja":
                buildingName = indoorBuilding.getNameJa();
                break;
            case "ko":
                buildingName = indoorBuilding.getNameKo();
                break;
            default:
                buildingName = indoorBuilding.getBuildingName();
                break;
        }

        if (TextUtils.isEmpty(buildingName)) {
            buildingName = indoorBuilding.getBuildingName();
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
                Address addressCn = indoorBuildingInfo.getAddressCn();
                Address addressZh = indoorBuildingInfo.getAddressZh();
                if (country.equals("CN") && addressCn != null) {
                    street = addressCn.getStreet();
                } else if (addressZh != null) {
                    street = addressZh.getStreet();
                }
                break;
            case "ja":
                Address addressJa = indoorBuildingInfo.getAddressJa();
                if (addressJa != null) {
                    street = addressJa.getStreet();
                }
                break;
            case "ko":
                Address addressKo = indoorBuildingInfo.getAddressKo();
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
