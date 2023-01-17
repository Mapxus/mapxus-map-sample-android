# Mapxus Map SDK Change Log

## 5.2.1

### Bugs
* Fix can not see building without 'building name'.
* Fix the cache of other building floors outside the current building is invalid.

## 5.2.0

### Features
* New privatization logic.
* Set the default maximum zoom of the map to 22.
* Mark some variables as deprecated in RoutePlanningInstructionSign.java.

## 5.1.0

### Features
* Update MAPPYBEE Style url.
* User can see all indoor map of ordinal=0 or nearest 0 instead of the gray building extent.

## 5.0.4

### Bugs
* Fix can not open building venue when use 'selectBuilding' method sometimes.
* Fix can not switch floor correctly when camera mode change from none to follow user.

## 5.0.3

### Bugs
* Fix can not switch building when use function focus on key cross building in RoutePainter class.

## 5.0.2

### Bugs
* Optimize point annotation logic.

### Features
* Add building type field in IndoorBuilding model.
* User can choose whether to see the full route on the map view ， when use Route Shortener.

## 5.0.1

### Bugs
* Fix the bug can not deal with the Building with multi polygon geometry.

## 5.0.0

### Features
* MapLibre GL replaces Mapbox GL as the new map rendering engine.
* Delete the code marked @Deprecated.
* MapxusPointAnnotation replace SymbolMarker.
* RoutePainter replace  WalkRouteOverlay.
* selectBuilding selectFloor replace switchBuilding switchFloor.
* There are also some data structure changes, please refer to our api doc

## 4.2.6

### Bugs
* Optimize switching building logic.

## 4.2.5

### Bugs
* Fix bugs , incomplete logo display when this map is zoomed out.

### Features
* New route painter 'RoutePainter' to replace 'WalkRouteOverlay'.
* New switch building method 'selectBuilding' and switch floor method 'selectFloor'.
* Developer can get a callback when setting the follow user mode.

## 4.2.4

### Bugs
* Developer can switch between different overlap indoor buildings by clicking on the map.

## 4.2.3

### Features
* Update address of default style.

## 4.2.2

### Bugs
* Fix can not response poi click event when turen off building gesture switch.

## 4.2.1

### Bugs
* Fix gesture interaction switch is not working in some extreme situations.

## 4.2.0

### Bugs
* Fix Exception when call Positioning Module.

## 4.1.9

### Features
* Add floor name on Poi object.

## 4.1.8

### Features
* Migrate public repo from Jcenter to Maven Central.

## 4.1.7

### Features
* set a new parameter(hiddenTranslucentPaths) in WalkRouteResource to choose show the route of the current floor of way finding results or all floors.

## 4.1.6

### Bugs
* Fix ArrayIndexOutOfBoundsException when navigating.

## 4.1.5

### Bugs
* Fix bugs , current navigation path instruction's distance not updated with current location.

### Features
* New mapxus map logo.
* Developer can get two parameters to set the bottom padding mapxus logo and open street map text.

## 4.1.4

### Bugs
* Fix bugs , can not get the current navigation path instruction.

## 4.1.3

### Bugs
* Fix bugs , incomplete display of floor selector text.
* Fix bugs , when initializing map, the building did not set the underground as the default floor.

### Features
* Now you can customize the distance in navigation's setOnReachListener callback.

## 4.1.2

### Features
* Floor selector can set selected font color
* Open api to set maximum allowable drift distance and maximum allowable drift times in Navigation function
* Add adsorption failed callback in Navigation function.

## 4.1.1

### Features
* Floor selector can set font color and selected box background color

## 4.1.0

### Features
* Add multilingual street names and venue id in IndoorBuilding model

## 4.0.9

### Features
* Add get the latest introductions api when in navigation mode.
* Add change navigation track resource size function.
* Compatible with Android 11.
* Use alias to customize style instead of full address.

## 4.0.8

### Bugs
* Fix bugs caused by location layer.

### Features
* Dynamically change the opacity of the building base.
* Add change navigation track resource function.

## 4.0.7

### Bugs
* Fix Nullpointer Exception in new marker function.

## 4.0.6

### Features
* New marker function.
* Make new map style as default

## 4.0.5

### Features
* New map style.

## 4.0.4

### Features
* Add navigation function include route shortener , route adsorber and callback when the destination is reached.

## 4.0.3

### Bugs
* Fix bugs caused by caching.

### Features
* Distinguish indoor and outdoor line colors when drawing navigation lines.

## 4.0.2

### Bugs
* Fix map rotation animation problem when positioning in heading mode.
* Fix the visibility of the building selector.
* Fix the callback of finishing drawing on the map when the point is outdoor.
* Fix can not search by multiple id (include building and poi) problem.

### Features
* Add set map label language function.
* Add setBuildingGestureSwitch method to turn off the ability to switch buildings by clicking on a map.
* Add setBuildingAutoSwitch method to turn off the ability to switch buildings when moving on the map.
* Add long click on the map callback.
* Add region field in IndoorBuildingInfo.
* Add poi search result sorting function (just in search nearby).
* Make compass icon is displayed by default.

## 4.0.1

### Features
* Add venue name and venue id in building search callback.

## 4.0.0

### Features
* Migrate to AndroidX.
* Use mapxus positioning SDK 2.0.0.
* Simplify Mapxus SDK dependencies.


## 3.2.6

### Bugs
* Fix can not use pointsearch.
* Fix Buildingsearch parsing exception.

### Features
* Add multi-language tag named "accessibilityDetail" of POI.

* Now you can get multi-language title and description of POI category search API in SDK.


## 3.2.5

### Bugs
* Fix multi-language data return.

### Features
* Add Korean and Japanese support.


## 3.2.4

### Bugs
* Fix can not show some building data.(Add multiple layouts to search building data)


## 3.2.3

### Bugs
* Fix floor filtering rules.

### Features
* Upgrade to mims2.


## 3.2.2

### Features
* Add buildingName tag in pointinfo class.


## 3.2.1

### Features
* Add init route method in WalkRouteOverlay class.
* Highlight instruction when selected.
* Update wayfinding connector icon.
* Users can use pointsearch to search buildings and pois at the same time.


## 3.2.0

### Bugs
* Fix wayfinding instructions.(the first point of instruction not match it's instruction)
* Fix map TalkBack.(do not read the logo)

### Features
* User can config the map element language and not dependence on system language.
* When we trigger the even of buildingChange,we can get the properties like 'bbox' and 'mult language name' from building.
* When we click the poi.We can get the buildingId、floor、type and location.
* Wayfinding add vehicle param.Support foot and wheelchair.
