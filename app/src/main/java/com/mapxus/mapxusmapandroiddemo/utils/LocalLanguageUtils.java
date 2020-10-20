package com.mapxus.mapxusmapandroiddemo.utils;

import com.mapxus.map.mapxusmap.api.map.model.IndoorBuilding;
import com.mapxus.map.mapxusmap.api.services.model.building.Address;

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
    public static String getLocalLanguageWithAddress(Map<String, Address> addressName) {
        Locale locale = Locale.getDefault();
        String localeLanguage = Locale.getDefault().getLanguage();
        switch (localeLanguage) {
            case "zh":
                if (locale.getCountry().equals("CN")) {
                    return Objects.requireNonNull(addressName.get("zh-Hans")).getStreet();
                } else {
                    return Objects.requireNonNull(addressName.get("zh-Hant")).getStreet();
                }
            case "ja":
                return Objects.requireNonNull(addressName.get("ja")).getStreet();
            case "ko":
                return Objects.requireNonNull(addressName.get("ko")).getStreet();
            default:
                return Objects.requireNonNull(addressName.get("en")).getStreet();
        }
    }
}
