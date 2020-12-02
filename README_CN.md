# Mapxus Map sample app for Android

This is a sample project to demonstrate how to use mapxus map android sdk.

[English Version](./README.md)

# 前言

我们强烈建议使用最新的稳定版Android Studio打开此项目。

在运行此项目之前，请在项目根目录中创建** secret.properties **文件，并以以下格式填写应用程序appid和secret：

	appid=
	secret=

如果您没有appid和secret，请与我们联系<support@mapxus.com>。

## 关于 Mapxus Map SDK

以下所有示例均基于最新的Mapxus Map SDK版本，请在您自己项目的builid.gradle文件中添加sdk依赖项：
```groovy
dependencies {
    	implementation 'com.mapxus.map:mapxusmap:[版本列表](./CHANGELOG.md)'
	}
```

有关Mapxus室内地图和定位服务的更多信息，请访问我们的网站： [https://www.mapxus.com/](https://www.mapxus.com/).


## 地图创建

### 创建地图（代码）

* 文件名：[SimpleMapViewActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/mapcreation/SimpleMapViewActivity.java)

* 简介：通过代码创建地图

* 详述：

  * 使用代码创建地图，并在xml文件中设置地图的中心地理坐标和缩放等级


### 用Fragment显示地图


* 文件名：[SupportMapFragmentActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/mapcreation/SupportMapFragmentActivity.java)

* 简介：在Fragment中创建地图

* 详述：

  * 使用代码在在Fragment中创建地图创建地图，并设置地图的中心地理坐标和缩放等级.


### 创建地图（指定初始建筑，楼层及建筑自适应边距）


* 文件名：[MapxusMapInitWithBuildingActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/mapcreation/MapxusMapInitWithBuildingActivity.java)

* 简介：创建地图时在设置的边距范围内最大化显示指定的建筑并切换到设置楼层

* 详述：

	* 使用指定的建筑ID、楼层及建筑自适应边距创建参数类[MapxusMapInitWithBuildingParamsActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/mapcreation/MapxusMapInitWithBuildingParamsActivity.java)实例
    * 通过生成的MapxusMapInitWithBuildingParamsActivity实例初始化地图
   

### 创建地图（指定初始POI及地图缩放等级）


* 文件名：[MapxusMapInitWithPoiActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/mapcreation/MapxusMapInitWithPoiActivity.java)

* 简介：创建地图时在地图中心显示指定的POI，并设置地图缩放等级

* 详述：
	* 使用指定的建筑ID、楼层及建筑自适应边距创建参数类[MapxusMapInitWithPoiParamsActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/mapcreation/MapxusMapInitWithPoiParamsActivity.java)实例
    * 通过生成的MapxusMapInitWithPoiParamsActivity实例初始化地图
     

## 地图交互

### 室内地图控件交互

* 文件名：[IndoorMapControllerActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/mapinteraction/IndoorMapControllerActivity.java)

* 简介：室内地图控件的位置

* 详述：

  * 设置是否一直隐藏室内地图控件
  
  * 设置室内地图控件位置：左边，右边，左上角，右上角，左下角，右下角

### 修改地图外观

* 文件名：[MapStyleSettingActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/mapinteraction/MapStyleSettingActivity.java)

* 简介：选择地图样式、标注语言及控制室外地图隐藏

* 详述：

  * 设置是否隐藏室外地图
  * 更换地图样式
  * 更换地图标注语言

### 切换建筑的手势交互

* 文件名：[GestureInteractionSwitchBuildingActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/mapinteraction/GestureInteractionSwitchBuildingActivity.java)

* 简介：设置切换建筑手势

* 详述：

  * 设置是否支持点击屏幕切换建筑
  * 设置是否支持室内建筑移动到屏幕中心区域自动切换建筑

### 方法交互（切换室内场景）

* 文件名：[FocusOnIndoorSceneActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/mapinteraction/FocusOnIndoorSceneActivity.java)

* 简介：代码设置聚焦的室内场景

* 详述：

  * 设置地图聚焦的建筑与楼层
  * 设置聚焦效果：不缩放、通过动画缩放、无动画缩放
  * 设置建筑自适应边距
  
### 事件交互（监听点击）

* 文件名：[ClickEventListenerActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/mapinteraction/ClickEventListenerActivity.java)

* 简介：监听单击、长按地图事件，单击POI事件

* 详述：

	* 单击地图POI，触发 `- onIndoorPoiClick(Poi poi)` 回调方法
    * 单击地图空白，触发 `-onMapClick(LatLng latLng, String floor, String buildingId, String floorId)` 回调方法
    * 长按地图，触发 `-onMapLongClick(LatLng latLng, String floor, String buildingId, String floorId)` 回调方法

### 事件交互（监听室内场景切换）

* 文件名：[IndoorSceneSwitchingListenerActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/mapinteraction/IndoorSceneSwitchingListenerActivity.java)

* 简介：监听室内场景切换事件。

* 详述：

    * 切换室内场景时，触发  `-onFloorChange(IndoorBuilding indoorBuilding, String floor)`  回调方法
   
### 事件交互（监听进出室内场景）

* 文件名：[IndoorSceneInAndOutListenerActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/mapinteraction/IndoorSceneInAndOutListenerActivity.java)

* 简介：监听进出室内场景事件

* 详述：

    * 进出室内场景时，触发 `-onBuildingChange(IndoorBuilding indoorBuilding)` 回调方法
   

## 地图上绘制

### 按楼层绘制标注

* 文件名：[DrawMarkerActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/mapediting/DrawMarkerActivity.java)

* 简介：当前场景只显示对应楼层的标注

* 详述：

   * 创建多个不同楼层的Mapxus Marker实例，并添加到地图上

### 按楼层绘制多边形

* 文件名：[DrawPolygonActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/mapediting/DrawPolygonActivity.java)

* 简介：当前场景只显示对应楼层的绘制多边形

* 详述：

   * 创建layer实例
   * 添加layer到mapview
   * 监听楼层切换，过滤layer数据
   
## 室内定位效果展示

### 室内定位效果展示

* 文件名：[LocationProviderActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/indoorpositioning/LocationProviderActivity.java)

* 简介：展示室内定位及地图跟随的效果

* 详述：

  * 实时显示当前定位经纬度，楼层与水平精度
  * 切换不同的定位跟随模式

* 使用前

   *  请在您自己项目的builid.gradle文件中添加positioning sdk依赖项：
```groovy
dependencies {
    	implementation 'com.mapxus.positioning:positioning:2.0.4'
	}
```


## 地图数据检索

### 全球范围内搜索建筑

* 文件名：[SearchBuildingGlobalActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/searchservices/SearchBuildingGlobalActivity.java)

* 简介：在全球范围内搜索建筑

* 详述：

     * 创建检索参数类 GlobalSearchOption的实例
     * 创建检索类 BuildingSearch的实例
     * 通过 `-onGetBuildingResult(BuildingResult buildingResult)` 回调方法获取检索结果
   
### 指定矩形区域内搜索建筑

* 文件名：[SearchBuildingInboundActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/searchservices/SearchBuildingInboundActivity.java)

* 简介：在指定的矩形区域内搜索建筑

* 详述：

     * 创建检索参数类 BoundSearchOptiont的实例
     * 创建检索类 BuildingSearcht的实例
     * 通过`-onGetBuildingResult(BuildingResult buildingResult)`回调方法获取检索结果


### 在附近搜索建筑

* 文件名：[SearchBuildingInboundActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/searchservices/SearchBuildingInboundActivity.java)

* 简介：在指定的圆形区域内搜索建筑

* 详述：

     * 创建检索参数类 NearbySearchOption的实例
     * 创建检索类 BuildingSearch的实例
     * 通过 `-onGetBuildingResult(BuildingResult buildingResult)` 回调方法获取检索结果

### 指定建筑ID搜索建筑

* 文件名：[SearchBuilding详述Activity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/searchservices/SearchBuilding详述Activity.java)

* 简介：按建筑ID搜索建筑

* 详述：

     * 创建检索参数类 SearchOption的实例
     * 创建检索类  BuildingSearch的实例
     * 通过 `-onGetBuildingDetailResult(BuildingDetailResult building详述Result)` 回调方法获取检索结果

### 获取指定场景内包含的POI分类

* 文件名：[SearchPoiCategoriesInBuildingActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/searchservices/SearchPoiCategoriesInBuildingActivity.java)

* 简介：获取指定建筑或楼层内所有包含的POI类别

* 详述：

     * 创建检索参数类 PoiCategorySearchOption的实例
     * 创建检索类PoiSearch的实例
     * 通过 `-onPoiCategoriesResult(PoiCategoryResult poiCategoryResult)` 回调方法获取检索结果

### 指定场景内搜索POI

* 文件名：[SearchPoiInBuildingActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/searchservices/SearchPoiInBuildingActivity.java)

* 简介：在指定场景内搜索POI，获取指定建筑或楼层内所有的POI

* 详述：

     * 创建检索参数类 PoiInBuildingSearchOption的实例
     * 创建检索类PoiSearch的实例
     * 通过 `-onGetPoiResult(PoiResult poiResult)` 回调方法获取检索结果
     
### 指定矩形区域内搜索POI

* 文件名：[SearchPoiInboundActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/searchservices/SearchPoiInboundActivity.java)

* 简介：在指定的矩形区域内搜索POI

* 详述：

     * 创建检索参数类 PoiBoundSearchOption的实例
     * 创建检索类PoiSearch的实例
     * 通过 `-onGetPoiResult(PoiResult poiResult)` 回调方法获取检索结果


### 周边POI搜索

* 文件名：[SearchPoiNearbyActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/searchservices/SearchPoiNearbyActivity.java)

* 简介：在指定圆形区域内搜索POI

* 详述：

     * 创建检索参数类 PoiNearbySearchOption的实例
     * 创建检索类PoiSearch的实例
     * 通过 `-onGetPoiResult(PoiResult poiResult)` 回调方法获取检索结果

### 指定POI ID搜索

* 文件名：[SearchPoi详述Activity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/searchservices/SearchPoi详述Activity.java)

* 简介：按POI ID搜索POI

* 详述：

     * 创建检索参数类SearchOption的实例
     * 创建检索类PoiSearch的实例
     * 通过 `-onGetPoiDetailResult(PoiDetailResult poi详述Result)` 回调方法获取检索结果

## 集成案例

### 周边环境识别

* 文件名：[SearchPoiWithOrientationActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/integrationcases/SearchPoiWithOrientationActivity.java)

* 简介：制作虚拟定位并识别定位周边POI信息。

* 详述：

     * 设置模拟定位点
     * 创建检索参数类 PoiOrientationSearchOption的实例
     * 创建检索类PoiSearch的实例
     * 通过 `-onGetPoiResult(PoiResult poiResult)` 回调方法获取检索结果


### 路线规划与路线吸附

* 文件名：[RoutePlanningActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/integrationcases/route/RoutePlanningActivity.java)

* 简介：检索起始点间的路线，并实现路网吸附、缩短功能

* 详述：

     * 创建检索参数类 RoutePlanningRequest的实例
     * 创建检索类 RoutePlanning的实例
     * 通过 `-onGetRoutePlanningResult(RoutePlanningResult routePlanningResult)` 回调方法获取检索结果
     * 创建 WalkRouteOverlay的实例 ，进行线路绘制
     * 创建 Navigation的实例 ，进行路线吸附
     * 创建 RouteShortener的实例 ，进行定位跟随缩短路线

### Visual map

* 文件名：[DisplayVisualActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/integrationcases/DisplayVisualActivity.java)

* 简介：介绍Visual的集成

* 详述：

     * 创建 VisualPolylineOverlay 的实例在地图上绘制信息点 points on a map.
     * 创建 MapxusVisual 的实例展示Visual map
     
* 使用前

   *  请在您自己项目的builid.gradle文件中添加visual map sdk依赖项：
```groovy
dependencies {
    	implementation 'com.mapxus.visual:mapxusvisual:0.2.3'
	}
```


### Explore搜索

* 文件名：[ExploreBuildingActivity](app/src/main/java/com/mapxus/mapxusmapandroiddemo/examples/integrationcases/explore/ExploreBuildingActivity.java)

* 简介：介绍建筑内POI常见搜索的集成

* 详述：

     * 选中某个室内建筑  
     * 创建检索参数类 PoiCategorySearchOption的实例
     * 创建检索类 PoiSearch的实例
     * 通过 `-onPoiCategoriesResult(PoiCategoryResult poiCategoryResult)` 回调方法获取检索结果
     * 通过指定类别创建检索参数类 PoiInBuildingSearchOption的实例
     * 通过 `-onGetPoiResult(PoiResult poiResult)` 回调方法获取检索结果
     * 点击POI查看详情