[![](https://jitpack.io/v/guting50/MyGtUtils.svg)](https://jitpack.io/#guting50/MyGtUtils)
[![API](https://img.shields.io/badge/API-15%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=15) 
[![作者](https://img.shields.io/badge/%E4%BD%9C%E8%80%85-guting50-brightgreen.svg)](https://github.com/guting50)

## 自定义工具库
PermissionUtils：权限请求框架  
DataCleanManager：本应用数据清除管理器  
MuchThreadDown：多线程下载，支持断点续传  
FileUtils：文件处理工具类  
AntiShake、OnNoDoubleClickListener：防止点击事件重复调用  
BgFrameLayout：自定义可设置背景的帧布局  
GtSeekBar：自定义SeekBar  
PaintView：自定义手写板控件  
TryLightStatus：修改状态栏背景色  
NestedRecyclerView、FlowLayout：流式布局，当一行显示不下自动跳转到第二行  
HaveHeaderRecyclerView：仿美团联动ListView  
floatingeditor：浮动编辑器，在键盘上面方显示

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
        implementation 'com.github.guting50:MyGtUtils:Tag'
    }
```
#### PermissionUtils 的使用
```Java
  PermissionUtils.requestPermission(this, PermissionUtils.CODE_CAMERA, new PermissionUtils.PermissionGrant() {
      @Override
      public void onPermissionGranted(int... requestCode) {
          Toast.makeText(MainActivity.this, "CODE_CAMERA : " + ((requestCode[0] == -1) ? "已拒绝" : "已允许"), Toast.LENGTH_LONG).show();
      }
  });
```
#### 设置拒绝后的弹窗提示语  默认提示语：没有此权限，无法开启这个功能，请开启权限：摄像头
````Java
  PermissionUtils.permissionsHintHead = "没有此权限，无法开启这个功能，请开启权限：";
````

#### DataCleanManager 的api
##### 获取缓存大小
````Java
static String getTotalCacheSize(Context context) throws Exception;
````
##### 清除缓存
````Java
static void clearAllCache(Context context);
````

#### MuchThreadDown 的使用
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
````

#### AntiShake、OnNoDoubleClickListener
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

#### BgFrameLayout 的使用
```Xml
  <com.gt.utils.view.BgFrameLayout
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
      <attr name="solid_gradual_change_orientation" format="enum"><!--填充渐变方向，默认diagonal-->
          <enum name="horizontal" value="0" />
          <enum name="vertical" value="1" />
          <enum name="diagonal" value="-1" />
      </attr>
      <attr name="stroke_color" format="color" /><!--边框颜色-->
      <attr name="stroke_start_color" format="color" /><!--边框渐变开始颜色-->
      <attr name="stroke_end_color" format="color" /><!--边框渐变结束颜色-->
      <attr name="stroke_gradual_change_orientation" format="enum"><!--边框渐变方向，默认diagonal-->
          <enum name="horizontal" value="0" />
          <enum name="vertical" value="1" />
          <enum name="diagonal" value="-1" />
      </attr>
      <attr name="stroke_width" format="dimension" /><!--边框宽度-->
      <attr name="stroke_dash_gap" format="dimension" /><!--间隔-->
      <attr name="stroke_dash_width" format="dimension" /><!--点的大小-->
      <attr name="corners_radius" format="dimension" /><!--圆角弧度-->
      <attr name="corners_radius_left_top" format="dimension" /><!--圆角弧度 左上角-->
      <attr name="corners_radius_right_top" format="dimension" /><!--圆角弧度 右上角-->
      <attr name="corners_radius_left_bottom" format="dimension" /><!--圆角弧度 左下角-->
      <attr name="corners_radius_right_bottom" format="dimension" /><!--圆角弧度 右下角-->
  </declare-styleable>
```

#### GtSeekBar 的使用
```Xml
  <com.gt.utils.view.GtSeekBar
      android:layout_width="30dp"
      android:layout_height="145dp
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

#### PaintView 的使用
```Xml
  <com.gt.utils.view.PaintView
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

#### TryLightStatus  
```Java
  static void setStatusBarColor(Activity activity, @ColorInt int color)
```

#### NestedRecyclerView、FlowLayout的使用
NestedRecyclerView是RecyclerView的子类，用法与RecyclerView一样，区别是：
```java
    recyclerView.setLayoutManager(new FlowLayoutManager());
```
FlowLayout是ViewGroup的子类
```xml
    <com.gt.utils.view.FlowLayout
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

#### HaveHeaderRecyclerView的使用  
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

#### floatingeditor
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
