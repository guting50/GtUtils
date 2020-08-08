[![](https://jitpack.io/v/guting50/GtUtils.svg)](https://jitpack.io/#guting50/GtUtils)
[![API](https://img.shields.io/badge/API-15%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=15) 
[![作者](https://img.shields.io/badge/%E4%BD%9C%E8%80%85-guting50-brightgreen.svg)](https://github.com/guting50)

## 自定义工具库
* [PermissionUtils：请求权限](#PermissionUtils)  
* [DataCleanManager：本应用数据清除管理器](#DataCleanManager)  
* [MuchThreadDown：多线程下载，支持断点续传](#MuchThreadDown)  
* [FileUtils：文件处理工具类](#FileUtils)  
* [AntiShake、OnNoDoubleClickListener：防止点击事件重复调用](#antishakeonnodoubleclicklistener)  
* [BgFLayout、BgTextView：自定义可设置背景的布局](#BgFLayoutbgTextView)  
* [GtSeekBar：自定义SeekBar](#GtSeekBar)  
* [PaintView：自定义手写板控件](#PaintView)  
* [TryLightStatus：修改状态栏背景色](#PaintView)  
* [NestedRecyclerView、FlowLayout：流式布局，当一行显示不下自动跳转到第二行](#NestedRecyclerViewflowLayout)  
* [HaveHeaderRecyclerView：仿美团联动ListView](#HaveHeaderRecyclerView)  
* [floatingeditor：浮动编辑器，在键盘上面方显示](#floatingeditor)  
* [GsonUtils：解决int类型转换成double](#GsonUtils)  
* [LogsUtils：保存日志](#LogsUtils)  
* [DateSelectDialog：日期选择器](#DateSelectDialog)  
* [ImagePagerActivity：查看大图](#ImagePagerActivity)  
* [MultiGridView：九宫格图片显示控件，支持添加图片](#MultiGridView)  
* [SimpleAdapter：RecyclerView通用适配器](#SimpleAdapter)  
* [SpanUtil：一个TextView实现多种样式](#SpanUtil)  
* [TabLayoutUtils：设置TabLayout标签下指示器的宽度](#TabLayoutUtils)  
* [ComputeUtils：计算工具类](#ComputeUtils)  
* [TextWatcherUtils：检查页面中的输入框是否都有值](#TextWatcherUtils)  
* [ClassesReader：类读取器，可以获取所有的类](#ClassesReader)   
* [CacheUtils：缓存工具类](#CacheUtils)     
* [DbHelper、DBClient：数据库工具类](#DbHelperdBClient)  




#### 依赖
将其添加到根build.gradle文件（而不是模块build.gradle文件）中：

```Xml
    allprojects {
        repositories {
            maven { url "https://jitpack.io" }
        }
    }
```
然后，将库添加到模块中 build.gradle
```Xml
    dependencies {
        implementation 'com.github.guting50:GtUtils:Tag'
    }
```
## PermissionUtils
```Java
  PermissionUtils.requestPermission(this, PermissionUtils.CODE_CAMERA, new PermissionUtils.PermissionGrant() {
      @Override
      public void onPermissionGranted(int... requestCode) {
        Toast.makeText(MainActivity.this, "CODE_CAMERA : 允许", Toast.LENGTH_LONG).show();
      }

      @Override
      public void onRefuseGranted() {
        Toast.makeText(MainActivity.this, "CODE_CAMERA : 拒绝", Toast.LENGTH_LONG).show();
      }
      
  });
```
#### 设置拒绝后的弹窗提示语  默认提示语：没有此权限，无法开启这个功能，请开启权限：摄像头
````Java
  PermissionUtils.permissionsHintHead = "没有此权限，无法开启这个功能，请开启权限：";
````

## DataCleanManager
##### 获取缓存大小
````Java
static String getTotalCacheSize(Context context) throws Exception;
````
##### 清除缓存
````Java
static void clearAllCache(Context context);
````

## MuchThreadDown
###### 线程的数量根据文件的大小自动分配，每个线程下载的数据量最多3M;
````Java
  new MuchThreadDown("http://img1.kuaimashi.com/69_1526641455014.mp4", "D:/video/").download(new OnDownloadListener() {
      @Override
      protected void onDownloadComplete(String name, String url, String filePath) {
          System.out.println("下载成功==" + "url:" + url);
      }

      @Override
      protected void onDownloadError(String url, Exception e) {
          System.out.println("下载失败==" + "url:" + url);
      }

      @Override
      protected void onDownloads(String url, int completed, int endIndex) {
          System.out.println("下载中==" + "url:" + url + ":（" + completed + "||" + endIndex + "）");
      }
  });
  //是否显示日志
  public MuchThreadDown isShowLog(boolean showLog);
  //是否单线程下载
  public MuchThreadDown isAlone(boolean alone);
  //是否覆盖下载
  public MuchThreadDown isCover(boolean cover);
````

## AntiShake、OnNoDoubleClickListener
````Java
  if(!AntiShake.check(view.getId())){
      //处理点击事件后的逻辑
  }
  
  view.setOnClickListener(new OnNoDoubleClickListener() {
      @Override
      public void onNoDoubleClick(View v) {
          //处理点击事件后的逻辑
      }
   });
  
````

## BgFLayout、BgTextView  
#### 同类型的有 BgFLayout（帧布局）、BgLLayout（线性布局）、BgCLayout（约束布局）
```Xml
  <com.gt.utils.widget.BgFrameLayout
      android:layout_width="200dp"
      android:layout_height="100dp"
      app:corners_radius="10dp"
      app:solid_color="@color/white"/>
```
全部属性
```Xml
  <declare-styleable name="BgFrameLayout">
          <attr name="solid_color" format="color" /><!--填充色-->
    <attr name="solid_start_color" format="color" /><!--填充渐变开始颜色-->
    <attr name="solid_end_color" format="color" /><!--填充渐变结束颜色-->
    <attr name="solid_gradual_orientation" format="enum"><!--填充渐变方向，默认horizontal-->
        <enum name="horizontal" value="0" />
        <enum name="vertical" value="1" />
        <enum name="diagonal" value="2" />
    </attr>
    <attr name="stroke_color" format="color" /><!--边框颜色-->
    <attr name="stroke_start_color" format="color" /><!--边框渐变开始颜色-->
    <attr name="stroke_end_color" format="color" /><!--边框渐变结束颜色-->
    <attr name="stroke_gradual_orientation" format="enum"><!--边框渐变方向，默认horizontal-->
        <enum name="horizontal" value="0" />
        <enum name="vertical" value="1" />
        <enum name="diagonal" value="2" />
    </attr>
    <attr name="stroke_width" format="dimension" /><!--边框宽度-->
    <attr name="stroke_dash_gap" format="dimension" /><!--间隔-->
    <attr name="stroke_dash_width" format="dimension" /><!--点的大小-->
    <attr name="corners_radius" format="dimension" /><!--圆角弧度-->
    <attr name="corners_radius_left_top" format="dimension" /><!--圆角弧度 左上角-->
    <attr name="corners_radius_right_top" format="dimension" /><!--圆角弧度 右上角-->
    <attr name="corners_radius_left_bottom" format="dimension" /><!--圆角弧度 左下角-->
    <attr name="corners_radius_right_bottom" format="dimension" /><!--圆角弧度 右下角-->

    <!--noEnabled-->
    <attr name="solid_color_no_enabled" format="color" /><!--填充色-->
    <attr name="solid_start_color_no_enabled" format="color" /><!--填充渐变开始颜色-->
    <attr name="solid_end_color_no_enabled" format="color" /><!--填充渐变结束颜色-->
    <attr name="solid_gradual_orientation_no_enabled" format="enum"><!--填充渐变方向，默认horizontal-->
        <enum name="horizontal" value="0" />
        <enum name="vertical" value="1" />
        <enum name="diagonal" value="2" />
    </attr>
    <attr name="stroke_color_no_enabled" format="color" /><!--边框颜色-->
    <attr name="stroke_start_color_no_enabled" format="color" /><!--边框渐变开始颜色-->
    <attr name="stroke_end_color_no_enabled" format="color" /><!--边框渐变结束颜色-->
    <attr name="stroke_gradual_orientation_no_enabled" format="enum"><!--边框渐变方向，默认horizontal-->
        <enum name="horizontal" value="0" />
        <enum name="vertical" value="1" />
        <enum name="diagonal" value="2" />
    </attr>
    <attr name="stroke_width_no_enabled" format="dimension" /><!--边框宽度-->
    <attr name="stroke_dash_gap_no_enabled" format="dimension" /><!--间隔-->
    <attr name="stroke_dash_width_no_enabled" format="dimension" /><!--点的大小-->
    <attr name="corners_radius_no_enabled" format="dimension" /><!--圆角弧度-->
    <attr name="corners_radius_left_top_no_enabled" format="dimension" /><!--圆角弧度 左上角-->
    <attr name="corners_radius_right_top_no_enabled" format="dimension" /><!--圆角弧度 右上角-->
    <attr name="corners_radius_left_bottom_no_enabled" format="dimension" /><!--圆角弧度 左下角-->
    <attr name="corners_radius_right_bottom_no_enabled" format="dimension" /><!--圆角弧度 右下角-->

    <!--checked-->
    <attr name="solid_color_checked" format="color" /><!--填充色-->
    <attr name="solid_start_color_checked" format="color" /><!--填充渐变开始颜色-->
    <attr name="solid_end_color_checked" format="color" /><!--填充渐变结束颜色-->
    <attr name="solid_gradual_orientation_checked" format="enum"><!--填充渐变方向，默认horizontal-->
        <enum name="horizontal" value="0" />
        <enum name="vertical" value="1" />
        <enum name="diagonal" value="2" />
    </attr>
    <attr name="stroke_color_checked" format="color" /><!--边框颜色-->
    <attr name="stroke_start_color_checked" format="color" /><!--边框渐变开始颜色-->
    <attr name="stroke_end_color_checked" format="color" /><!--边框渐变结束颜色-->
    <attr name="stroke_gradual_orientation_checked" format="enum"><!--边框渐变方向，默认horizontal-->
        <enum name="horizontal" value="0" />
        <enum name="vertical" value="1" />
        <enum name="diagonal" value="2" />
    </attr>
    <attr name="stroke_width_checked" format="dimension" /><!--边框宽度-->
    <attr name="stroke_dash_gap_checked" format="dimension" /><!--间隔-->
    <attr name="stroke_dash_width_checked" format="dimension" /><!--点的大小-->
    <attr name="corners_radius_checked" format="dimension" /><!--圆角弧度-->
    <attr name="corners_radius_left_top_checked" format="dimension" /><!--圆角弧度 左上角-->
    <attr name="corners_radius_right_top_checked" format="dimension" /><!--圆角弧度 右上角-->
    <attr name="corners_radius_left_bottom_checked" format="dimension" /><!--圆角弧度 左下角-->
    <attr name="corners_radius_right_bottom_checked" format="dimension" /><!--圆角弧度 右下角-->

    <!--pressed-->
    <attr name="solid_color_pressed" format="color" /><!--填充色-->
    <attr name="solid_start_color_pressed" format="color" /><!--填充渐变开始颜色-->
    <attr name="solid_end_color_pressed" format="color" /><!--填充渐变结束颜色-->
    <attr name="solid_gradual_orientation_pressed" format="enum"><!--填充渐变方向，默认horizontal-->
        <enum name="horizontal" value="0" />
        <enum name="vertical" value="1" />
        <enum name="diagonal" value="2" />
    </attr>
    <attr name="stroke_color_pressed" format="color" /><!--边框颜色-->
    <attr name="stroke_start_color_pressed" format="color" /><!--边框渐变开始颜色-->
    <attr name="stroke_end_color_pressed" format="color" /><!--边框渐变结束颜色-->
    <attr name="stroke_gradual_orientation_pressed" format="enum"><!--边框渐变方向，默认horizontal-->
        <enum name="horizontal" value="0" />
        <enum name="vertical" value="1" />
        <enum name="diagonal" value="2" />
    </attr>
    <attr name="stroke_width_pressed" format="dimension" /><!--边框宽度-->
    <attr name="stroke_dash_gap_pressed" format="dimension" /><!--间隔-->
    <attr name="stroke_dash_width_pressed" format="dimension" /><!--点的大小-->
    <attr name="corners_radius_pressed" format="dimension" /><!--圆角弧度-->
    <attr name="corners_radius_left_top_pressed" format="dimension" /><!--圆角弧度 左上角-->
    <attr name="corners_radius_right_top_pressed" format="dimension" /><!--圆角弧度 右上角-->
    <attr name="corners_radius_left_bottom_pressed" format="dimension" /><!--圆角弧度 左下角-->
    <attr name="corners_radius_right_bottom_pressed" format="dimension" /><!--圆角弧度 右下角-->

    <!--focused-->
    <attr name="solid_color_focused" format="color" /><!--填充色-->
    <attr name="solid_start_color_focused" format="color" /><!--填充渐变开始颜色-->
    <attr name="solid_end_color_focused" format="color" /><!--填充渐变结束颜色-->
    <attr name="solid_gradual_orientation_focused" format="enum"><!--填充渐变方向，默认horizontal-->
        <enum name="horizontal" value="0" />
        <enum name="vertical" value="1" />
        <enum name="diagonal" value="2" />
    </attr>
    <attr name="stroke_color_focused" format="color" /><!--边框颜色-->
    <attr name="stroke_start_color_focused" format="color" /><!--边框渐变开始颜色-->
    <attr name="stroke_end_color_focused" format="color" /><!--边框渐变结束颜色-->
    <attr name="stroke_gradual_orientation_focused" format="enum"><!--边框渐变方向，默认horizontal-->
        <enum name="horizontal" value="0" />
        <enum name="vertical" value="1" />
        <enum name="diagonal" value="2" />
    </attr>
    <attr name="stroke_width_focused" format="dimension" /><!--边框宽度-->
    <attr name="stroke_dash_gap_focused" format="dimension" /><!--间隔-->
    <attr name="stroke_dash_width_focused" format="dimension" /><!--点的大小-->
    <attr name="corners_radius_focused" format="dimension" /><!--圆角弧度-->
    <attr name="corners_radius_left_top_focused" format="dimension" /><!--圆角弧度 左上角-->
    <attr name="corners_radius_right_top_focused" format="dimension" /><!--圆角弧度 右上角-->
    <attr name="corners_radius_left_bottom_focused" format="dimension" /><!--圆角弧度 左下角-->
    <attr name="corners_radius_right_bottom_focused" format="dimension" /><!--圆角弧度 右下角-->      

    <!--noEnabled-->
    <attr name="textColor_no_enabled" format="color" />
    <attr name="textSize_no_enabled" format="dimension" />
    <attr name="text_no_enabled" format="string" />

    <!--checked-->
    <attr name="textColor_checked" format="color" />
    <attr name="textSize_checked" format="dimension" />
    <attr name="text_checked" format="string" />

    <!--pressed-->
    <attr name="textColor_pressed" format="color" />
    <attr name="textSize_pressed" format="dimension" />
    <attr name="text_pressed" format="string" />

    <!--focused-->
    <attr name="textColor_focused" format="color" />
    <attr name="textSize_focused" format="dimension" />
    <attr name="text_focused" format="string" />
  </declare-styleable>
```

## GtSeekBar
```Xml
  <com.gt.utils.widget.GtSeekBar
      android:layout_width="30dp"
      android:layout_height="145dp"
      android:layout_marginTop="20dp"
      app:thumb_color="#FF0000"
      app:progress_future_color="#4d5a74"
      app:progress_past_color="#12b9ff"
      app:seek_orientation="vertical" />
```
```Java
  gtSeekBar.setOnSlideChangeListener(new GtSeekBar.OnSlideChangeListener() {
      @Override
      public void OnSlideChangeListener(View view, int progress) {
          Log.e("拖动过程中的值：", String.valueOf(progress));
      }

      @Override
      public void onSlideStopTouch(View view, int progress) {
          Log.e("停止滑动时的值：", String.valueOf(progress));
      }
  });
```
全部属性
```Xml
  <declare-styleable name="GtSeekBar">
      <attr name="seek_progress" format="integer" /><!--滑块的位置（默认区间0-100）-->
      <attr name="seek_max_count" format="integer" /><!--最小值（默认0）-->
      <attr name="seek_min_count" format="integer" /><!--最大值（默认100）-->
      <attr name="progress_color" format="color" /><!--滑竿的颜色-->
      <attr name="progress_past_color" format="color" /><!--滑竿已滑过的颜色-->
      <attr name="progress_future_color" format="color" /><!--滑竿未滑过的颜色-->
      <attr name="progress_size" format="dimension" /><!--滑竿的大小（宽度或者高度）-->
      <attr name="thumb_color" format="color" /><!--滑块的颜色-->
      <attr name="thumb_image" format="reference" /><!--滑块的图片-->
      <attr name="drag_able" format="boolean" /><!--可拖动的-->
      <attr name="seek_orientation" format="enum"> <!--方向 默认横屏-->
          <enum name="horizontal" value="0" />
          <enum name="vertical" value="1" />
      </attr>
  </declare-styleable>
```

## PaintView
```Xml
  <com.gt.utils.widget.PaintView
      android:layout_width="match_parent"
      android:layout_height="match_parent" />
```
```Java
  paintView.setProhibitTouch(true);//关闭手滑
  paintView.setDraw(false);//只记录滑动过程中的路径，不画在PaintView上
  paintView.drawBackground(bipmap);//把路径画在位图上
  paintView.addPath(path);//添加路径并画出来
  paintView.setOnCurrentDrawListener(new PaintView.OnCurrentDrawListener() {
    @Override
    public void onCurrentDraw() {
        //滑动过程中的监听
    }
  });
```
全部属性
```Xml
  <declare-styleable name="PaintView">
      <attr name="paint_color" format="color" /><!--画笔的颜色-->
      <attr name="paint_stroke_width" format="float" /><!--画笔的宽度-->
      <attr name="paint_type" format="enum"> <!--画笔的类型-->
          <enum name="curve" value="0" /><!--曲线-->
          <enum name="line" value="1" /><!--直线-->
          <enum name="rect" value="2" /><!--矩形-->
          <enum name="circle" value="3" /><!--圆形-->
          <enum name="oval" value="4" /><!--椭圆形-->
      </attr>
  </declare-styleable>
```

## TryLightStatus  
```Java
  static void setStatusBarColor(Activity activity, @ColorInt int color)
```

## NestedRecyclerView、FlowLayout
NestedRecyclerView是RecyclerView的子类，用法与RecyclerView一样，区别是：
```java
    recyclerView.setLayoutManager(new FlowLayoutManager());
```
FlowLayout是ViewGroup的子类
```xml
    <com.gt.utils.widget.FlowLayout
        android:id="@+id/fl_flow"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="300dp"
        android:background="#FFFFFF"
        app:flChildSpacing="auto"
        app:flChildSpacingForLastRow="align"
        app:flRowSpacing="10dp" />
```
```Java
        for (int i = 0; i < 10; i++) {
            TextView tv = new TextView(this);
            tv.setLayoutParams(new ViewGroup.LayoutParams(250, 100));
            tv.setGravity(Gravity.CENTER);
            tv.setBackgroundColor(0xff123456);
            tv.setTextColor(Color.WHITE);
            tv.setText("测试" + i);
            fl_flow.addView(tv);
        }
```

全部属性
```xml
    <declare-styleable name="FlowLayout">
        <attr name="flFlow" format="boolean"/> <!--是否设置为流式布局，默认为true-->
        <attr name="flChildSpacing" format="enum|dimension"><!--设置子视图之间的水平间距-->
            <enum name="auto" value="-65536"/><!--为间隔自动（均匀排列）-->
        </attr>
        <attr name="flMinChildSpacing" format="dimension"/><!--最小子间距-->
        <attr name="flChildSpacingForLastRow" format="enum|dimension"><!--设置最后一行的子视图之间的水平间距-->
            <enum name="auto" value="-65536"/><!--为间隔自动（均匀排列）-->
            <enum name="align" value="-65537"/><!--为间隔对齐-->
        </attr>
        <attr name="flRowSpacing" format="enum|dimension"><!--行间距-->
            <enum name="auto" value="-65536"/><!--为间隔自动（均匀排列）-->
        </attr>
        <attr name="flRtl" format="boolean"/><!--设置是否右对齐，默认false-->
        <attr name="flMaxRows" format="integer"/><!--最大行数-->
        <attr name="android:gravity"/><!--layout_gravity-->
    </declare-styleable>
```

#####  子布局比较复杂的可以使用NestedRecyclerView，便于扩展，简单的可以用FlowLayout，使用起来方便。

## HaveHeaderRecyclerView
单独使用与RecyclerView一样  
adapter继承CustomizeRVBaseAdapter  
注意：adapter数据刷新要调用notifyChanged（），不要调用notifyDataSetChanged（）（坑：RecyclerView.Adapter的notifyDataSetChanged（）不允许继承）  
#####  与列表联动使用
```Java
        recyclerView.setOnManualScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    // 当不滚动时
                    case RecyclerView.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (recyclerView.canScrollVertically(1)) {
                            //左边的列表滚动到底部
                        }
                        // 判断滚动到顶部
                        if (recyclerView.canScrollVertically(-1)) {
                            //左边的列表滚动到顶部
                        }
                        break;
                    default:
                        break;
                }
            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int sectionForPosition = lvRight.getSelectedSectionForPosition();
                //判断当左边当前选中项与sectionForPosition不相等时
                //把左边列表滚动到sectionForPosition位置并做相应的选中处理
            }
        });
```
当左边列表点击选中某一项时，调用setSelectionTitle(int position);  

## floatingeditor
```Java
 /**
     *
     * @param context  上下文
     * @param editorCallback  回调接口
     * @param checkRule  检验规则 可为null
     * @param dimAmount  背景透明度
     * @param isFinishOnTouchOutside  点击空白区域是否关闭编辑框
     */
    public static void openDefaultEditor(Context context, EditorCallback editorCallback, InputCheckRule checkRule, float dimAmount, boolean isFinishOnTouchOutside);
 
 /**
     *
     * @param context  上下文
     * @param editorCallback  回调接口
     * @param holder  编辑框样式
     * @param checkRule  检验规则 可为null
     * @param dimAmount  背景透明度
     * @param isFinishOnTouchOutside  点击空白区域是否关闭编辑框
     */
    public static void openEditor(Context context, EditorCallback editorCallback, EditorHolder holder, InputCheckRule checkRule, float dimAmount, boolean isFinishOnTouchOutside);
```

## DateSelectDialog
```java
    new DateSelectDialog(activity).setOnSelectedListener(data -> {
        //选择后
    }).setDefauleDate(date).setDatePattern(pattern).show();
```

## ImagePagerActivity
```java
     public static void startImagePagerActivity(Activity context, List<String> imgUrls, int position);

    public static void startImagePagerActivity(Activity context, List<String> imgUrls, int position, ImageSize imageSize);
    
    public static void startImagePagerActivity(Activity context, List<String> imgUrls, int position, ImageSize imageSize, int showBn);
    
    public static void startImagePagerActivity(Activity context, List<String> imgUrls,int position, ImageSize imageSize, int showBn, int requestCode);
```

## MultiGridView
```xml
    <com.gt.utils.widget.multigridview.MultiGridView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
```
```xml
    <declare-styleable name="MultiGridView">
        <attr name="android:numColumns"/>
        <attr name="android:horizontalSpacing"/>
        <attr name="android:verticalSpacing"/>
        <attr format="integer" name="maxItems"/>
        <attr format="boolean" name="showEdit"/>
        <attr format="reference" name="addImg"/>
        <attr format="reference" name="delImg"/>
        <attr format="reference" name="defImg"/>
        <attr format="dimension" name="cornersRadius"/>
    </declare-styleable>
```
```java
    //设置初始数据
    public void setFilenamesData(List<String> initData) ;

    // 设置添加回调（点击添加的时候调用自己的媒体选择库，如果不设置会使用默认的媒体库 com.github.guting50:PhotoPicker）
    public void setOnAddListenerr(OnRemoveListener onRemoveListener);

    // 设置添加结束后回调
    public void setOnAddedListener(OnRemoveListener onRemoveListener);
    
    // 获取图片结果
    public List<String> getPaths();
    
    // 更新图片上传进度
    public void updateProgress(final String localPath, final int progress);
    
    //更新完成后设置新的网络图片地址
    public void updateSuccess(String localPath, String url);
```

## SimpleAdapter
```java
    public SimpleAdapter(@NonNull Class<? extends VHolder> holder);

    public SimpleAdapter(List data, @NonNull Class<? extends VHolder> holder);

    public SimpleAdapter(Object obj, @NonNull Class<? extends VHolder> holder);
    
    public SimpleAdapter(Object obj, List data, @NonNull Class<? extends VHolder> holder);
    
```
需要实现抽象类VHolder

## SpanUtil
```java
    SpanUtil.create()
        .addSection("仅再邀请1名好友，即可获得1张20元助力专享券")
        .setStyle("1名", Typeface.BOLD)
        .setForeColor("1名", 0xffEA3A0B)
        .setStyle("20元", Typeface.BOLD)
        .setForeColor("20元", 0xffEA3A0B)
        .showIn(textView);
```
更多样式，请查看源码

## TabLayoutUtils
```java
    public static void reflex(final TabLayout tabLayout, final int mW, final int mH, @ColorInt final int color);
```

## ComputeUtils
#### api详见源码

## TextWatcherUtils
```java
    //当所有的TextView都有值的时候 bnView的enabled = true
    public TextWatcherUtils(View bnView, TextView... textViews);

    //设置检测回调，调用check后的回调方法
    public void setOnCheckedListener(OnCheckedListener onCheckedListener);
    public void check();
    
    public void addView(TextView... textViews);
    public void removeView(TextView... textViews);
```

## ClassesReader  
#### 类读取器，可以获取所有的类，用于实现获取某一个接口或者类的所有子类，或者获取指定注解的类
```java
**
     * 获取应用程序下的所有Dex文件
     *
     * @param context 上下文
     * @return Set<DexFile>
     */
    public static Set<DexFile> applicationDexFile(Context context);  
    public static Set<DexFile> applicationDexFile(String packageCodePath);  
    public static List<Class<?>> reader(Context context);
    
    /**
     * 读取类路径下的所有类
     *
     * @param packageName     包名
     * @param packageCodePath 包路径
     * @return List<Class>
     */
    public static List<Class<?>> reader(String packageName, String packageCodePath);
    
    /**
     * 删除集合中没有指定注解的类
     *
     * @param classes
     * @param annotationType
     */
    public static List<Class<?>> deleteNotAnnotationClass(List<Class<?>> classes, Class<? extends Annotation> annotationType)

```

## CacheUtils
#### 数据缓存到sqltile数据库中，直接缓存到内容中，容易在内存紧张的时候，自动删除数据，所以把数据放在数据库中
```java
public static int put(String key, Object obj);
public static String getVal(String key);
public static <T> T getObj(String key, Class<T> clazz);
public static int delete(String key);
public static int clear();
//其他api详见源码
```

## DbHelper、DBClient
#### 数据库工具类，对ormlite的封装，使用方法与ormlite一样，便捷之处在于数据库、表的创建，更新，和对数据简单的操作
```java
    /**
    * DbHelper 对OrmLiteSqliteOpenHelper（SQLiteOpenHelper）的封装，
    * 当我们创建好数据实体类后，只需要加上ormlite的注解后，调用此方法就会自动去创建数据库和表（创建类名上有DatabaseTable注解的类）
    * 数据库的更新只支持表字段的新增和删除，不支持表字段名的更改，表结构更新后，数据不会丢失
    */
     public static void updateDB(String databaseName, int databaseVersion);
     
    /**
    * DBClient 数据操作的工具类（增、删、查、改）api详见源码
    */
```




