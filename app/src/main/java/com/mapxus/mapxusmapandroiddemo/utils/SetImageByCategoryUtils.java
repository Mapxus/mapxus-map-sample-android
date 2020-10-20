package com.mapxus.mapxusmapandroiddemo.utils;

import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.constant.CategoryConstant;

/**
 * Created by Edison on 2020/9/4.
 * Describe:
 */
public class SetImageByCategoryUtils {

    public static int setImageByCategory(String category) {
        String[] categorySize = category.split("\\.");
        String categoryResult = categorySize[categorySize.length - 1];
        switch (categoryResult) {
            case CategoryConstant.HAIR:
            case CategoryConstant.BEAUTY_SPAS:
            case CategoryConstant.BEAUTY_PARLOR:
                return R.drawable.beauty;
            case CategoryConstant.ENTRY:
            case CategoryConstant.GATE:
                return R.drawable.exit;
            case CategoryConstant.SPORT_SWEAR:
            case CategoryConstant.MENS_CLOTH:
            case CategoryConstant.CLOTHES:
            case CategoryConstant.WOMENS_CLOTH:
            case CategoryConstant.CHILD_CLOTH:
                return R.drawable.cloths;
            case CategoryConstant.OPTICIANS:
                return R.drawable.opticians;
            case CategoryConstant.SNACK_SHOP:
            case CategoryConstant.SHOPPING:
            case CategoryConstant.LUGGAGE_STORAGE:
            case CategoryConstant.LUGGAGE_BAGS:
            case CategoryConstant.GROCERY:
            case CategoryConstant.CONVENIENCE:
            case CategoryConstant.HOBBY_SHOPS:
            case CategoryConstant.HOME_AND_GARDEN:
            case CategoryConstant.HEALTH_CARE:
            case CategoryConstant.GOURMET:
                return R.drawable.shopping;
            case CategoryConstant.TOYS:
                return R.drawable.toys;
            case CategoryConstant.RESTAURANT:
            case CategoryConstant.WESTERN:
            case CategoryConstant.FAST_FOOD:
            case CategoryConstant.HOTPOT:
            case CategoryConstant.PANASIAN:
            case CategoryConstant.CHINESE:
            case CategoryConstant.CAFES:
            case CategoryConstant.BAKERIES:
            case CategoryConstant.SUSHI:
            case CategoryConstant.BBQ:
            case CategoryConstant.FOOD:
                return R.drawable.restaurant;
            case CategoryConstant.COSMETICS:
                return R.drawable.cosmetics;
            case CategoryConstant.SPORT_GOODS:
                return R.drawable.sportgoods;
            case CategoryConstant.COIN_LOCKER:
                return R.drawable.coin_lockers;
            case CategoryConstant.EDUCATION:
            case CategoryConstant.TUTORING:
            case CategoryConstant.SPECIALTY_SCHOOLS:
                return R.drawable.education;
            case CategoryConstant.APPLIANCES:
                return R.drawable.appliances;
            case CategoryConstant.ELEVATOR:
                return R.drawable.elevator;
            case CategoryConstant.DESSERTS:
                return R.drawable.desserts;
            case CategoryConstant.CONNECTOR:
            case CategoryConstant.GUEST_SERVICES:
            case CategoryConstant.FACILITY:
            case CategoryConstant.ANTEROOM:
            case CategoryConstant.RESTROOM:
            case CategoryConstant.VIRTUAL_REALITY_CENTERS:
            case CategoryConstant.FIELD_OF_PLAY:
            case CategoryConstant.RECEPTION_DESK:
            case CategoryConstant.FUNCTION_ROOM:
                return R.drawable.facilities;
            case CategoryConstant.ESCALATOR:
                return R.drawable.escalator;
            case CategoryConstant.INFORMATION:
                return R.drawable.information;
            case CategoryConstant.FEMALE:
                return R.drawable.restroom_female;
            case CategoryConstant.MOTHERSROOM:
                return R.drawable.mothers_room;
            case CategoryConstant.ELECTRONICS:
                return R.drawable.electronics;
            case CategoryConstant.ARTS_ENTERTAINMENT:
            case CategoryConstant.MOVIE_THEATERS:
                return R.drawable.arts_entertainment;
            case CategoryConstant.ATM:
                return R.drawable.atm;
            case CategoryConstant.WORKPLACE:
                return R.drawable.workplace;
            case CategoryConstant.UNISEX:
                return R.drawable.unisex;
            case CategoryConstant.RAMP:
                return R.drawable.ramp;
            case CategoryConstant.STAIRS:
                return R.drawable.stairs;
            case CategoryConstant.MEDIA:
                return R.drawable.book;
            case CategoryConstant.SHOES:
                return R.drawable.shoes;
            case CategoryConstant.WATCHES:
                return R.drawable.watches;
            case CategoryConstant.JEWELRY:
                return R.drawable.jewelry;
            case CategoryConstant.MALE:
                return R.drawable.restroom_male;
            case CategoryConstant.DISABLE:
                return R.drawable.disable;
            case CategoryConstant.BABY_GEAR:
                return R.drawable.baby_gear;
            case CategoryConstant.TRANSPORT:
                return R.drawable.transport;
            case CategoryConstant.HEALTH_MEDICAL:
            case CategoryConstant.CLINICS:
                return R.drawable.health_medical;
            case CategoryConstant.LOCAL_SERVICES:
            case CategoryConstant.TELECOMMUNICATIONS:
            case CategoryConstant.SERVICE:
                return R.drawable.local_services;
            case CategoryConstant.AUTOMOTIVE:
                return R.drawable.auto_motive;
            case CategoryConstant.KARAOKE:
                return R.drawable.karaoke;
            case CategoryConstant.SMOKING_AREA:
                return R.drawable.smoking_room;
            case CategoryConstant.MINI_BUS_STATIONS:
                return R.drawable.bus_station;
            case CategoryConstant.CAR_DEALERS:
                return R.drawable.car_wash;
            case CategoryConstant.COMPANY:
            case CategoryConstant.PROFESSIONAL_SERVICES:
                return R.drawable.professional_services;
            case CategoryConstant.BEER_AND_WINE:
            case CategoryConstant.BARS:
                return R.drawable.bars;
            case CategoryConstant.TICKETING:
                return R.drawable.ticketing;
            default:
                return R.drawable.others;
        }
    }
}
