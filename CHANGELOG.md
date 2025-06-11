# Mapxus Map SDK Change Log

## 10.0.0 (2025.06.10)

🎉Features

- We have introduced shared floor data to describe floor areas without unique subordinate buildings. So we have add new functions in map display, venue search, poi search and select methods.
  - Map Display：
    - When users select shared level（level which is not belong to specific one building）, it will only see the current floor name on the floor selector.
    - When users select shared level, it will display all the other levels which are in the buildings related to the shared level and have the same ordinal with current shared level.
  - Selected Method:
    - Add a new selected floor methods.
  - Search:
    - Users can get shared level data in the Venue search response if the venue has shared floor data.
    - When users search POI data with venueId, users can get all POIs result(both in Buildings and on shared floors) in response. And user can see these POIs on shared floor will have sharedFloorId without buildingId.
- Added a 2.5D effect on the map. Users can view the three-dimensional wall of the units when rotating the map.(The 2.5D effect is only available in the default mapxus style (mapxus_v7) of the latest version of the SDK. If users want to upgrade the SDK but do not want to use the 2.5D effect, they can choose to use the defined style or mapxus_mims2_v5 style.)
- Add `sharedFloor` property of `IndoorCodingResult`. Besides, property `floor` and `building` are nullable now.
- Property  `buildingId` of `IndoorLatLng` is nullable now.
- In order to locate user's position in shared level crrectly, Mapxus Positioning SDKs equal or higher than 2.3.1 are recommanded.
- Provided `lineOffset` of `BuildingBorderStyle`.
- Provided `searchCategoriesInSite` of `CategorySearch`.
- Deprecated `searchPoiCategoryInSite` of `PoiSearch`.

💥 Breaking

- Following items have breaking changes:

| Class            | Deprecated                                                   | New                                                          |
| ---------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| MapxusMap        | ~~setFloorSwitchMode()~~                                     |                                                              |
| MapxusMapOptions | ~~floorSwitchMode~~                                          |                                                              |
| MapxusMap        | OnFloorChangedListener.onFloorChange(@Nullable Venue venue, @Nullable IndoorBuilding indoorBuilding, @Nullable **FloorInfo floorInfo**); | OnFloorChangedListener.onFloorChange(@Nullable Venue venue, @Nullable IndoorBuilding indoorBuilding, @Nullable **Floor floor**); |
|                  | OnMapClickedListener.onMapClick(@NonNull LatLng latLng, **@Nullable FloorInfo floorInfo, @Nullable IndoorBuilding indoorBuilding, @Nullable Venue venue**); | OnMapClickedListener.onMapClick(@NonNull LatLng latLng, **@NotNull MapxusSite mapxusSite**); |
|                  | OnMapLongClickedListener.onMapLongClick(@NonNull LatLng latLng, **@Nullable FloorInfo floorInfo, @Nullable IndoorBuilding indoorBuilding, @Nullable Venue venue**); | OnMapLongClickedListener.onMapLongClick(@NonNull LatLng latLng, **@NotNull MapxusSite mapxusSite**); |
|                  | **FloorInfo** getSelectedFloor()                             | **Floor** getSelectedFloor()                                 |
| IndoorLocation   | **floorInfo: FloorInfo**                                     | **floor: Floor**                                             |

🐛Bugs

- Fixed highlight selected building/shared floor display bug.

## 9.5.0 (2025.05.12)

🐛Bugs

- When the language data is not present, the corresponding property of the `MultilingualObject`  will be null instead of an empty string.
- Fixed an bug where indoor tiles were not properly loaded due to locale issues.
- Fixed a rare crash due to a network state switch.
- Fixed an incorrect calculation of route distance in a specific scenario.

🎉Features

- Enhanced the security of data transmission within the SDK.

## 9.4.0 (2025.03.20)

🐛Bugs

- Fixed a bug where marker could not be displayed when using `MapxusPointAnnotation.iconImage`.

🎉Features

- Support zh-Hant-TW in `MultilingualObject`.

## 9.3.0 (2025.02.19)

🐛Bugs

- Fixed a crash that occurred when changing styles while using positioning.

🎉Features

- Deprecated `MapxusUiSettings.setCollapseCopyright()`.
- Provided`MapxusUiSettings.setMapxusLogoEnabled` to enable/disable Mapxus Logo.
- Deprecated `Venue.bbox`.
- Deprecated `IndoorBuilding.bbox`.

## 9.2.0 (2025.01.22)

🐛Bugs

- Fixed some known crash bugs.
- Fixed user location transparency does not change depending on the floor.

🎉Features

- provide `outdoorLineOpacity` setting of `RoutePainterResource`.
## 9.1.0

🐛Bugs

- Fixed parcelization problem of `MultilingualObject` .
- Fixed an issue with some funtions of `VenueSearch` listeners not responding.
## 9.0.0

💥 Breaking

- All funtions, classes, parameters was marked by `@Deprecated`annotation from version 5.2.0 to version 8.7.0 are deleted.
- Parameter `buildingId` of `RoutePlanningPoint` is nolongger needed now.
- Some classes are rewritten in Kotlin. Although their functionalities are preserved, there might be slight differences in the way they are called.

🎉Features

- `RoutePainterResource` provides `lineWidth`, `dashedLineWidth`, `inactiveRouteOpacity` settings now.
- `MapViewProvider.setLanguage` provides some new language options.
  - `MapLanguage.AR`
  - `MapLanguage.FIL`
  - `MapLanguage.ID`
  - `MapLanguage.TH`
  - `MapLanguage.VI`
  - `MapLanguage.PT`
- Following items are deprecated now:

| Class                 | Deprecated                     | New                                 |
| --------------------- | ------------------------------ | ----------------------------------- |
| MapViewProvider       | setBuildingAutoSwitch          | IMapxusMap.setBuildingAutoSwitch    |
|                       | setBuildingGestureSwitch       | IMapxusMap.setBuildingGestureSwitch |
| MapboxMapViewProvider | setHiddenOutdoor               | IMapxusMap.setHiddenOutdoor         |
| MapxusMapOption       | getFloorSwitchMode             |                                     |
|                       | setFloorSwitchMode             |                                     |
| FloorSwitchMode       | the whole Class was deprecated |                                     |
| IMapxusMap            | setFloorSwitchMode             |                                     |
| RoutePainterResource  | dashLineColor                  | dashedLineColor                     |
|                       | arrowIconSize                  | lineSymbolSize                      |
|                       | arrowIcon                      | lineSymbol                          |
|                       | hiddenTranslucentPaths         | inactiveRouteOpacity                |
|                       | setDashLineColor               | setDashedLineColor                  |
|                       | setArrowIconSize               | setLineSymbolSize                   |
|                       | setArrowIcon                   | setLineSymbol                       |



## 8.8.1

🐛Bugs

- Fixed outdoor positioning crash while user is using Follow Mode or Heading Mode.

## 8.8.0

🎉Features

- Provide `CategorySearch` to search category by bbox.
- Provide fil, id, pt, th, vi, ar for `MultilingualObject`.
- Optimize HeadMode performance.

## 8.7.0

🐛Bugs

- Fixed ambiguous declaration of `RoutePainterResourse`'s functions'.

🎉Features

- New apis of some models' multilingual datas are now provided. Therefore the old apis are deprecated!

| Class              | Deprecated                                                      | New                                                                |
|--------------------|-----------------------------------------------------------------|--------------------------------------------------------------------|
| Venue              | `setVenueNameMap`                                               | `setNameMap`                                                       |
|                    | `getVenueName`                                                  | `getNameMap().getDefault()`                                        |
|                    | `getVenueNameEn`                                                | `getNameMap().getEn()`                                             |
|                    | `getVenueNameCn`                                                | `getNameMap().getZhHans()`                                         |
|                    | `getVenueNameZh`                                                | `getNameMap().getZhHant()`                                         |
|                    | `getVenueNameJa`                                                | `getNameMap().getJa()`                                             |
|                    | `getVenueNameKo`                                                | `getNameMap().getKo()`                                             |
|                    | `setVenueAddressMap`                                            | `setAddressMap`                                                    |
|                    | `getAddress`                                                    | `getAddressMap().getDefault()`                                     |
|                    | `getAddressEn`                                                  | `getAddressMap().getEn()`                                          |
|                    | `getAddressCn`                                                  | `getAddressMap().getZhHans()`                                      |
|                    | `getAddressZh`                                                  | `getAddressMap().getZhHant()`                                      |
|                    | `getAddressJa`                                                  | `getAddressMap().getJa()`                                          |
|                    | `getAddressKo`                                                  | `getAddressMap().getKo()`                                          |
| IndoorBuilding     | `getAddrName`                                                   | `getAddressMap().getDefault()`                                     |
|                    | `setAddrName`                                                   | `setAddressMap`                                                    |
|                    | `getAddrNameEn`                                                 | `getAddressMap().getEn()`                                          |
|                    | `setAddrNameEn`                                                 | `setAddressMap`                                                    |
|                    | `getAddrNameCn`                                                 | `getAddressMap().getZhHans()`                                      |
|                    | `setAddrNameCn`                                                 | `setAddressMap`                                                    |
|                    | `getAddrNameZh`                                                 | `getAddressMap().getZhHant()`                                      |
|                    | `setAddrNameZh`                                                 | `setAddressMap`                                                    |
|                    | `getAddrNameJa`                                                 | `getAddressMap().getJa()`                                          |
|                    | `setAddrNameJa`                                                 | `setAddressMap`                                                    |
|                    | `getAddrNameKo`                                                 | `getAddressMap().getKo()`                                          |
|                    | `setAddrNameKo`                                                 | `setAddressMap`                                                    |
|                    | `getBuildingName`                                               | `getBuildingNameMap().getDefault()`                                |
|                    | `setBuildingName`                                               | `setBuildingNameMap`                                               |
|                    | `getNameEn`                                                     | `getBuildingNameMap().getEn()`                                     |
|                    | `setNameEn`                                                     | `setBuildingNameMap`                                               |
|                    | `getNameCn`                                                     | `getBuildingNameMap().getZhHans()`                                 |
|                    | `setNameCn`                                                     | `setBuildingNameMap`                                               |
|                    | `getNameZh`                                                     | `getBuildingNameMap().getZhHant()`                                 |
|                    | `setNameZh`                                                     | `setBuildingNameMap`                                               |
|                    | `getNameJa`                                                     | `getBuildingNameMap().getJa()`                                     |
|                    | `setNameJa`                                                     | `setBuildingNameMap`                                               |
|                    | `getNameKo`                                                     | `getBuildingNameMap().getKo()`                                     |
|                    | `setNameKo`                                                     | `setBuildingNameMap`                                               |
| Poi                | `getName`                                                       | `getNameMap().getDefault()`                                        |
|                    | `setName`                                                       | `setNameMap`                                                       |
|                    | `getNameEn`                                                     | `getNameMap().getEn()`                                             |
|                    | `setNameEn`                                                     | `setNameMap`                                                       |
|                    | `getNameCn`                                                     | `getNameMap().getZhHans()`                                         |
|                    | `setNameCn`                                                     | `setNameMap`                                                       |
|                    | `getNameZh`                                                     | `getNameMap().getZhHant()`                                         |
|                    | `setNameZh`                                                     | `setNameMap`                                                       |
|                    | `getNameJa`                                                     | `getNameMap().getJa()`                                             |
|                    | `setNameJa`                                                     | `setNameMap`                                                       |
|                    | `getNameKo`                                                     | `getNameMap().getKo()`                                             |
|                    | `setNameKo`                                                     | `setNameMap`                                                       |
|                    | `getAccessibilityDetail`                                        | `getAccessibilityDetailMap().getDefault()`                         |
|                    | `setAccessibilityDetail`                                        | `setAccessibilityDetailMap`                                        |
|                    | `getAccessibilityDetailEn`                                      | `getAccessibilityDetailMap().getEn()`                              |
|                    | `setAccessibilityDetailEn`                                      | `setAccessibilityDetailMap`                                        |
|                    | `getAccessibilityDetailCn`                                      | `getAccessibilityDetailMap().getZhHans()`                          |
|                    | `setAccessibilityDetailCn`                                      | `setAccessibilityDetailMap`                                        |
|                    | `getAccessibilityDetailZh`                                      | `getAccessibilityDetailMap().getZhHant()`                          |
|                    | `setAccessibilityDetailZh`                                      | `setAccessibilityDetailMap`                                        |
|                    | `getAccessibilityDetailJa`                                      | `getAccessibilityDetailMap().getJa()`                              |
|                    | `setAccessibilityDetailJa`                                      | `setAccessibilityDetailMap`                                        |
|                    | `getAccessibilityDetailKo`                                      | `getAccessibilityDetailMap().getKo()`                              |
|                    | `setAccessibilityDetailKo`                                      | `setAccessibilityDetailMap`                                        |
| VenueInfo          | `setVenueNameMap`                                               | `setNameMap`                                                       |
|                    | `getVenueName`                                                  | `getNameMap().getDefault()`                                        |
|                    | `getVenueNameEn`                                                | `getNameMap().getEn()`                                             |
|                    | `getVenueNameZh`                                                | `getNameMap().getZhHant()`                                         |
|                    | `getVenueNameCn`                                                | `getNameMap().getZhHans()`                                         |
|                    | `getVenueNameJa`                                                | `getNameMap().getJa()`                                             |
|                    | `getVenueNameKo`                                                | `getNameMap().getKo()`                                             |
|                    | `setVenueAddressMap`                                            | `setAddressMap`                                                    |
|                    | `getAddress`                                                    | `getAddressMap().getDefault()`                                     |
|                    | `getAddressEn`                                                  | `getAddressMap().getEn()`                                          |
|                    | `getAddressZh`                                                  | `getAddressMap().getZhHant()`                                      |
|                    | `getAddressCn`                                                  | `getAddressMap().getZhHans()`                                      |
|                    | `getAddressJa`                                                  | `getAddressMap().getJa()`                                          |
|                    | `getAddressKo`                                                  | `getAddressMap().getKo()`                                          |
| IndoorBuildingInfo | `getNameDefault`                                                | `getNameMap().getDefault()`                                        |
|                    | `setNameDefault`                                                | `setNameMap`                                                       |
|                    | `getNameEn`                                                     | `getNameMap().getEn()`                                             |
|                    | `setNameEn`                                                     | `setNameMap`                                                       |
|                    | `getNameCn`                                                     | `getNameMap().getZhHans()`                                         |
|                    | `setNameCn`                                                     | `setNameMap`                                                       |
|                    | `getNameZh`                                                     | `getNameMap().getZhHant()`                                         |
|                    | `setNameZh`                                                     | `setNameMap`                                                       |
|                    | `getNameJa`                                                     | `getNameMap().getJa()`                                             |
|                    | `setNameJa`                                                     | `setNameMap`                                                       |
|                    | `getNameKo`                                                     | `getNameMap().getKo()`                                             |
|                    | `setNameKo`                                                     | `setNameMap`                                                       |
|                    | `setBuildingNameMap(`*`Map`*`<String, String> buildingNameMap)` | `setBuildingNamesMap(MultilingualObject<String> buildingNamesMap)` |
|                    | *`Map`*`<String, String> getBuildingNameMap()`                  | `MultilingualObject<String> getBuildingNamesMap()`                 |
|                    | `getBuildingNameDefault`                                        | `getBuildingNameMap().getDefault()`                                |
|                    | `getBuildingNameEn`                                             | `getBuildingNameMap().getEn()`                                     |
|                    | `getBuildingNameCn`                                             | `getBuildingNameMap().getZhHans()`                                 |
|                    | `getBuildingNameZh`                                             | `getBuildingNameMap().getZhHant()`                                 |
|                    | `getBuildingNameJa`                                             | `getBuildingNameMap().getJa()`                                     |
|                    | `getBuildingNameKo`                                             | `getBuildingNameMap().getKo()`                                     |
|                    | `getAddressDefault`                                             | `getAddressMap().getDefault()`                                     |
|                    | `getAddressEn`                                                  | `getAddressMap().getEn()`                                          |
|                    | `getAddressCn`                                                  | `getAddressMap().getZhHans()`                                      |
|                    | `getAddressZh`                                                  | `getAddressMap().getZhHant()`                                      |
|                    | `getAddressJa`                                                  | `getAddressMap().getJa()`                                          |
|                    | `getAddressKo`                                                  | `getAddressMap().getKo()`                                          |
|                    | `setAddressDefault`                                             | `setAddressMap`                                                    |
|                    | `setAddressEn`                                                  | `setAddressMap`                                                    |
|                    | `setAddressCn`                                                  | `setAddressMap`                                                    |
|                    | `setAddressZh`                                                  | `setAddressMap`                                                    |
|                    | `setAddressJa`                                                  | `setAddressMap`                                                    |
|                    | `setAddressKo`                                                  | `setAddressMap`                                                    |
|                    | `getVenueNameDefault`                                           | `getVenueNameMap().getDefault()`                                   |
|                    | `getVenueNameEn`                                                | `getVenueNameMap().getEn()`                                        |
|                    | `getVenueNameCn`                                                | `getVenueNameMap().getZhHans()`                                    |
|                    | `getVenueNameZh`                                                | `getVenueNameMap().getZhHant()`                                    |
|                    | `getVenueNameJa`                                                | `getVenueNameMap().getJa()`                                        |
|                    | `getVenueNameKo`                                                | `getVenueNameMap().getKo()`                                        |
|                    | `setVenueNameDefault`                                           | `setVenueNameMap`                                                  |
|                    | `setVenueNameEn`                                                | `setVenueNameMap`                                                  |
|                    | `setVenueNameCn`                                                | `setVenueNameMap`                                                  |
|                    | `setVenueNameZh`                                                | `setVenueNameMap`                                                  |
|                    | `setVenueNameJa`                                                | `setVenueNameMap`                                                  |
|                    | `setVenueNameKo`                                                | `setVenueNameMap`                                                  |
| PoiInfo            | `getNameDefault`                                                | `getNameMap().getDefault()`                                        |
|                    | `setNameDefault`                                                | `setNameMap`                                                       |
|                    | `getNameEn`                                                     | `getNameMap().getEn()`                                             |
|                    | `setNameEn`                                                     | `setNameMap`                                                       |
|                    | `getNameCn`                                                     | `getNameMap().getZhHans()`                                         |
|                    | `setNameCn`                                                     | `setNameMap`                                                       |
|                    | `getNameZh`                                                     | `getNameMap().getZhHant()`                                         |
|                    | `setNameZh`                                                     | `setNameMap`                                                       |
|                    | `getNameJa`                                                     | `getNameMap().getJa()`                                             |
|                    | `setNameJa`                                                     | `setNameMap`                                                       |
|                    | `getNameKo`                                                     | `getNameMap().getKo()`                                             |
|                    | `setNameKo`                                                     | `setNameMap`                                                       |
|                    | `getAccessibilityDetailDefault`                                 | `getAccessibilityDetailMap().getDefault()`                         |
|                    | `setAccessibilityDetailDefault`                                 | `setAccessibilityDetailMap`                                        |
|                    | `getAccessibilityDetailEn`                                      | `getAccessibilityDetailMap().getEn()`                              |
|                    | `setAccessibilityDetailEn`                                      | `setAccessibilityDetailMap`                                        |
|                    | `getAccessibilityDetailCn`                                      | `getAccessibilityDetailMap().getZhHans()`                          |
|                    | `setAccessibilityDetailCn`                                      | `setAccessibilityDetailMap`                                        |
|                    | `getAccessibilityDetailZh`                                      | `getAccessibilityDetailMap().getZhHant()`                          |
|                    | `setAccessibilityDetailZh`                                      | `setAccessibilityDetailMap`                                        |
|                    | `getAccessibilityDetailJa`                                      | `getAccessibilityDetailMap().getJa()`                              |
|                    | `setAccessibilityDetailJa`                                      | `setAccessibilityDetailMap`                                        |
|                    | `getAccessibilityDetailKo`                                      | `getAccessibilityDetailMap().getKo()`                              |
|                    | `setAccessibilityDetailKo`                                      | `setAccessibilityDetailMap`                                        |
|                    | `getDescriptionDefault`                                         | `getDescriptionMap().getDefault()`                                 |
|                    | `getDescriptionEn`                                              | `getDescriptionMap().getEn()`                                      |
|                    | `getDescriptionCn`                                              | `getDescriptionMap().getZhHans()`                                  |
|                    | `getDescriptionZh`                                              | `getDescriptionMap().getZhHant()`                                  |
|                    | `getDescriptionJa`                                              | `getDescriptionMap().getJa()`                                      |
|                    | `getDescriptionKo`                                              | `getDescriptionMap().getKo()`                                      |
|                    | `setDescriptions`                                               | `setDescriptionMap`                                                |

## 8.6.0

🐛Bugs

- Fixed NPE caused by IndoorBuildingInfo.getBuildingNameXXX.

🎉Features

- IllegalArgumentException will be thrown when a null or empty id is passed to DetailSearchOption.id().
- The id list passed to DetailSearchOption.ids() will filter out all null or empty elements and throw an IllegalArgumentException if the filtered list size is 0.
- Route planning drawing supports the multi-icon feature by using the `waypointIconList` attribute of RoutePainterResource.
- The `waypointIcon` attribute of RoutePainterResource is now deprecated!

## 8.5.0

🐛Bugs

- Fixed waypoints function drawing bug.
- Fixed map locale bug,

🎉Features

- PoiInfo from Poi Search by id add descriptions with locale.
- IndoorBuildingInfo add buildName properties.
- Some functions/properties was deprecated.

| Class/Protocol | Deprecated Interfaces/Properties | Replacement Class/Interfaces/Properties |
|:--------------:|:--------------------------------:|:---------------------------------------:|
|    PoiInfo     |           description            |         getDescriptionDefault()         |

## 8.4.0

🎉Features

* Update Auth SDK.
* Add RoutePlanningVehicle 'EMERGENCY'.
* Poi Search add exclude_categories parameter.
* Poi Search add PoiSearchOrderBy parameter 'DEFAULT_NAME'.
* Route planning search , add waypoints functino, and some function was deprecated.

|    Class/Protocol    |                                                                      Deprecated Interfaces/Properties                                                                      |      Replacement Class/Interfaces/Properties      |
|:--------------------:|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:-------------------------------------------------:|
|    RoutePlanning     | route(@NonNull RoutePlanningPoint fromPoint, @NonNull RoutePlanningPoint toPoint,@NonNull @RoutePlanningLocale.Locale String routePlanningLocale, @NonNull Boolean toDoor) | route(@NonNull RoutePlanningQueryRequest request) |
|                      |                                 route(@NonNull RoutePlanningPoint fromPoint, @NonNull RoutePlanningPoint toPoint, @NonNull Boolean toDoor)                                 | route(@NonNull RoutePlanningQueryRequest request) |
|                      |                                                                route(@NonNull RoutePlanningRequest request)                                                                | route(@NonNull RoutePlanningQueryRequest request) |
|                      |                               route(@NonNull RoutePlanningRequest request, @NonNull RoutePlanningResultListener routePlanningResultListener)                               | route(@NonNull RoutePlanningQueryRequest request) |
| RoutePlanningRequest |                                                                                   toDoor                                                                                   |                                                   |
| RoutePlanningRequest |                                                                                                                                                                            |             RoutePlanningQueryRequest             |

## 8.3.0

🎉Features

* POI orientation search Added the distance search type 'Gate'.
* Use indoor coding to optimize the situation when you are indoors but receive gps location.

🐛Bugs

* Fixed bugs that select floor by id can not select target when visible box does not contain it.

## 8.2.1

🎉Features

* Optimized the adsorption algorithm for building to building navigation.

## 8.2.0

🐛Bugs

* Fixed when in building floor switch mode , the mask layer show abnormality.
* Fixed Route adsorb can not use with floor id.
* Fixed Click map exception when the venue has holes.

🎉Features

* MapxusPointAnnotation add floor id as member variable
* PoiOrientationSearchOption add ordinal as member variable.

## 8.1.0

🐛Bugs

* Fixed location camera move bug.
* Fixed poi click event , can not selecte poi when the current building does not match the poi building.
* Fixed selecteBuilding , selecteFloor function , no history floors in use.

🎉Features

* Poi search , add venue Id as parameter, and some function was deprecated.

> searchPoiCategoryInBuilding -> searchPoiCategoryInSite

> searchInBuilding -> searchInSite

> PoiInBuildingSearchOption -> PoiInSiteSearchOption

* All search function , add a one-to-one result callback.
* When the indoor map are overlaped together , the current selected building can be selected as the foreground.
* The user can choose whether to display the grey mask of the building through the parameters.

## 8.0.0

🐛Bugs

* Fixed bug when switch follow user mode , the blue point did not move to right place.

🎉Features

* Route Painter , add shuttle bus logic.
* Update Building Selector Icon.
* Add 'selectVenueById' funciton.
* Add "default display" logic.
* Modify the SDK method to use floorcode section (use floorId instead).

|        Class/Protocol        |                                           Deprecated Interfaces/Properties                                            |                               Replacement Class/Interfaces/Properties                               |
|:----------------------------:|:---------------------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------:|
| MapxusPointAnnotationOptions |                                                         floor                                                         |                                               floorId                                               |
| MapxusPointAnnotationOptions |                                                      buildingId                                                       |                                                                                                     |
|    MapxusPointAnnotation     |                                                         floor                                                         |                                               floorId                                               |
|    MapxusPointAnnotation     |                                                      buildingId                                                       |                                                                                                     |
|       MapxusMapOptions       |                                                         floor                                                         |                                               floorId                                               |
|          MapxusMap           |                                                     currentFloor                                                      |                                            selectedFloor                                            |
|          MapxusMap           |                                                 currentIndoorBuilding                                                 |                                         selectedBuildingId                                          |
|          MapxusMap           |                                          selectFloor(@NonNull String floor)                                           |                              selectFloorById(@NonNull String floorId)                               |
|          MapxusMap           |                selectFloor(@NonNull String floor, @MapxusMapZoomMode.Mode int zoomMode, Insets insets)                |    selectFloorById(@NonNull String floorId, @MapxusMapZoomMode.Mode int zoomMode, Insets insets)    |
|          MapxusMap           |                                      selectBuilding(@NonNull String buildingId)                                       |                           selectBuildingById(@NonNull String buildingId)                            |
|          MapxusMap           |            selectBuilding(@NonNull String buildingId, @MapxusMapZoomMode.Mode int zoomMode, Insets insets)            | selectBuildingById(@NonNull String buildingId, @MapxusMapZoomMode.Mode int zoomMode, Insets insets) |
|          MapxusMap           |                           selectBuilding(@NonNull String buildingId, @NonNull String floor)                           |                                                                                                     |
|          MapxusMap           | selectBuilding(@NonNull String buildingId, @NonNull String floor, @MapxusMapZoomMode.Mode int zoomMode, Insetsinsets) |                                                                                                     |
|        IndoorBuilding        |                                                      groundFloor                                                      |                                        defaultDisplayFloorId                                        |
|         IndoorLatLng         |                                                         floor                                                         |                                               floorId                                               |
|        IndoorLocation        |                                                         floor                                                         |                                              floorInfo                                              |
|      IndoorBuildingInfo      |                                                      groundFloor                                                      |                                        defaultDisplayFloorId                                        |
|        InstructionDto        |                                                         floor                                                         |                                               floorId                                               |
|  PoiInBuildingSearchOption   |                                                        mFloor                                                         |                                              mFloorId                                               |
|   PoiCategorySearchOption    |                                                        mFloor                                                         |                                              mFloorId                                               |
|        CameraPosition        |                                                         floor                                                         |                                               floorId                                               |
|       MapxusUiSettings       |                               setLogoBottomMargin() setOpenStreetSourceBottomMargins()                                |                                     setCopyrightBottomMargin()                                      |
|      NearbySearchOption      |                                                        mRadius                                                        |                                              mDistance                                              |
|        RouteAdsorber         |                                                    onReachListener                                                    |                                                                                                     |
|          Paragraph           |                                                         floor                                                         |                                               floorId                                               |
|         RoutePainter         |                                                   changeOnBuilding                                                    |                                                                                                     |

* Update Maplibre SDK to 10.2.0 version

  💥 Breaking: Changed resourcePrefix to `maplibre_`
  from `mapbox_` ([#647](https://github.com/maplibre/maplibre-native/pull/647)) and renamed resources accordingly. Note
  that this is a breaking change since the names of public resources were renamed as well. Replaced Mapbox logo with
  MapLibre logo.

  > To migrate:  
  > If you use any of the public Android resources, you will get an error that they can not be found. Replace the prefix
  of each, e.g. `R.style.mapbox_LocationComponent` -> `R.style.maplibre_LocationComponent`.

## 7.1.1

🐛Bugs

* Fixed bug on map click listener.
* Fixed bug init with poi id can not show correct floor.

🎉Features

* Nearby search in building or in venue ,the distance can be a decimal.

## 7.1.0

🐛Bugs

* Fixed bug where getImplMapAsync function only returns mapxusmap object the first time.
* Fix overlapped levels problem.
* Fix can not see building without 'building name'.

🎉Features

* MinSdkVersion changed from 21 to 24.
* Optimize the map display to prevent floors from overlapping during initialization.
* New style for displaying copyright icons.
* New display solution for wayfinding results within one Venue.
* New display solution of Floor Selector to show multiple floor name.
* New search error return information.
* Mapxus Point Annotation incoming images are now supported as bitmap
* New Floor Selector style.
* Add a Building outline when building is selected and support change its style.
* Optimize the Route planning api.
* Support venue related search functions.
* Added the setting of indoor floor switching mode。
* Optimize the RouteAdsorber.
* Add default display floor logic.
* New floor change ,map click , map long click event.
* Add defaultDisplayedFloorId property to IndoorBuilding and IndoorBuildingInfo.
* Deprecated some function and object.

|    Class/Protocol    |     Deprecated Interfaces/Properties      |      Replacement Class/Interfaces/Properties      |
|:--------------------:|:-----------------------------------------:|:-------------------------------------------------:|
| RoutePlanningRequest | fromBuilding，fromFloor，toBuilding，toFloor | fromBuildingId，fromFloorId，toBuildingId，toFloorId |
|  RoutePlanningPoint  |                   floor                   |                      floorId                      |
|    InstructionDto    |                   floor                   |                      floorId                      |
|      MapxusMap       |           OnFloorChangeListener           |              OnFloorChangedListener               |
|                      |            OnMapClickListener             |               OnMapClickedListener                |
|                      |          OnMapLongClickListener           |             OnMapLongClickedListener              |
|  IndoorBuildingInfo  |                groundFloor                |                                                   |
|    IndoorBuilding    |                groundFloor                |                                                   |

## 5.2.2

🐛Bugs

* Fixed bug with custom floor selector cache.

## 5.2.1

🐛Bugs

* Fix can not see building without 'building name'.
* Fix the cache of other building floors outside the current building is invalid.

## 5.2.0

🎉Features

* New privatization logic.
* Set the default maximum zoom of the map to 22.
* Mark some variables as deprecated in RoutePlanningInstructionSign.java.

## 5.1.0

🎉Features

* Update MAPPYBEE Style url.
* User can see all indoor map of ordinal=0 or nearest 0 instead of the gray building extent.

## 5.0.4

🐛Bugs

* Fix can not open building venue when use 'selectBuilding' method sometimes.
* Fix can not switch floor correctly when camera mode change from none to follow user.

## 5.0.3

🐛Bugs

* Fix can not switch building when use function focus on key cross building in RoutePainter class.

## 5.0.2

🐛Bugs

* Optimize point annotation logic.

🎉Features

* Add building type field in IndoorBuilding model.
* User can choose whether to see the full route on the map view ， when use Route Shortener.

## 5.0.1

🐛Bugs

* Fix the bug can not deal with the Building with multi polygon geometry.

## 5.0.0

🎉Features

* MapLibre GL replaces Mapbox GL as the new map rendering engine.
* Delete the code marked @Deprecated.
* MapxusPointAnnotation replace SymbolMarker.
* RoutePainter replace WalkRouteOverlay.
* selectBuilding selectFloor replace switchBuilding switchFloor.
* There are also some data structure changes, please refer to our api doc

## 4.2.6

🐛Bugs

* Optimize switching building logic.

## 4.2.5

🐛Bugs

* Fix bugs , incomplete logo display when this map is zoomed out.

🎉Features

* New route painter 'RoutePainter' to replace 'WalkRouteOverlay'.
* New switch building method 'selectBuilding' and switch floor method 'selectFloor'.
* Developer can get a callback when setting the follow user mode.

## 4.2.4

🐛Bugs

* Developer can switch between different overlap indoor buildings by clicking on the map.

## 4.2.3

🎉Features

* Update address of default style.

## 4.2.2

🐛Bugs

* Fix can not response poi click event when turen off building gesture switch.

## 4.2.1

🐛Bugs

* Fix gesture interaction switch is not working in some extreme situations.

## 4.2.0

🐛Bugs

* Fix Exception when call Positioning Module.

## 4.1.9

🎉Features

* Add floor name on Poi object.

## 4.1.8

🎉Features

* Migrate public repo from Jcenter to Maven Central.

## 4.1.7

🎉Features

* set a new parameter(hiddenTranslucentPaths) in WalkRouteResource to choose show the route of the
  current floor of way finding results or all floors.

## 4.1.6

🐛Bugs

* Fix ArrayIndexOutOfBoundsException when navigating.

## 4.1.5

🐛Bugs

* Fix bugs , current navigation path instruction's distance not updated with current location.

🎉Features

* New mapxus map logo.
* Developer can get two parameters to set the bottom padding mapxus logo and open street map text.

## 4.1.4

🐛Bugs

* Fix bugs , can not get the current navigation path instruction.

## 4.1.3

🐛Bugs

* Fix bugs , incomplete display of floor selector text.
* Fix bugs , when initializing map, the building did not set the underground as the default floor.

🎉Features

* Now you can customize the distance in navigation's setOnReachListener callback.

## 4.1.2

🎉Features

* Floor selector can set selected font color
* Open api to set maximum allowable drift distance and maximum allowable drift times in Navigation
  function
* Add adsorption failed callback in Navigation function.

## 4.1.1

🎉Features

* Floor selector can set font color and selected box background color

## 4.1.0

🎉Features

* Add multilingual street names and venue id in IndoorBuilding model

## 4.0.9

🎉Features

* Add get the latest introductions api when in navigation mode.
* Add change navigation track resource size function.
* Compatible with Android 11.
* Use alias to customize style instead of full address.

## 4.0.8

🐛Bugs

* Fix bugs caused by location layer.

🎉Features

* Dynamically change the opacity of the building base.
* Add change navigation track resource function.

## 4.0.7

🐛Bugs

* Fix Nullpointer Exception in new marker function.

## 4.0.6

🎉Features

* New marker function.
* Make new map style as default

## 4.0.5

🎉Features

* New map style.

## 4.0.4

🎉Features

* Add navigation function include route shortener , route adsorber and callback when the destination
  is reached.

## 4.0.3

🐛Bugs

* Fix bugs caused by caching.

🎉Features

* Distinguish indoor and outdoor line colors when drawing navigation lines.

## 4.0.2

🐛Bugs

* Fix map rotation animation problem when positioning in heading mode.
* Fix the visibility of the building selector.
* Fix the callback of finishing drawing on the map when the point is outdoor.
* Fix can not search by multiple id (include building and poi) problem.

🎉Features

* Add set map label language function.
* Add setBuildingGestureSwitch method to turn off the ability to switch buildings by clicking on a
  map.
* Add setBuildingAutoSwitch method to turn off the ability to switch buildings when moving on the
  map.
* Add long click on the map callback.
* Add region field in IndoorBuildingInfo.
* Add poi search result sorting function (just in search nearby).
* Make compass icon is displayed by default.

## 4.0.1

🎉Features

* Add venue name and venue id in building search callback.

## 4.0.0

🎉Features

* Migrate to AndroidX.
* Use mapxus positioning SDK 2.0.0.
* Simplify Mapxus SDK dependencies.

## 3.2.6

🐛Bugs

* Fix can not use pointsearch.
* Fix Buildingsearch parsing exception.

🎉Features

* Add multi-language tag named "accessibilityDetail" of POI.

* Now you can get multi-language title and description of POI category search API in SDK.

## 3.2.5

🐛Bugs

* Fix multi-language data return.

🎉Features

* Add Korean and Japanese support.

## 3.2.4

🐛Bugs

* Fix can not show some building data.(Add multiple layouts to search building data)

## 3.2.3

🐛Bugs

* Fix floor filtering rules.

🎉Features

* Upgrade to mims2.

## 3.2.2

🎉Features

* Add buildingName tag in pointinfo class.

## 3.2.1

🎉Features

* Add init route method in WalkRouteOverlay class.
* Highlight instruction when selected.
* Update wayfinding connector icon.
* Users can use pointsearch to search buildings and pois at the same time.

## 3.2.0

🐛Bugs

* Fix wayfinding instructions.(the first point of instruction not match it's instruction)
* Fix map TalkBack.(do not read the logo)

🎉Features

* User can config the map element language and not dependence on system language.
* When we trigger the even of buildingChange,we can get the properties like 'bbox' and 'mult
  language name' from building.
* When we click the poi.We can get the buildingId、floor、type and location.
* Wayfinding add vehicle param.Support foot and wheelchair.

