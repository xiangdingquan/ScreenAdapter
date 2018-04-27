#### ScreenAdapter

    屏幕适配方案 采用百分比适配思路进行屏幕适配
#### 原理说明
    
    采用百分比适配原理，进行屏幕适配 
    
    首先  我们加载布局之后都会走到这个方法
    
```java
  @Override
    public void setContentView(int resId) {
        ensureSubDecor();
        ViewGroup contentParent = (ViewGroup) mSubDecor.findViewById(android.R.id.content);
        contentParent.removeAllViews();
        LayoutInflater.from(mContext).inflate(resId, contentParent);
        mOriginalWindowCallback.onContentChanged();
    }
```   
    继续查看
    
```java
   public View inflate(@LayoutRes int resource, @Nullable ViewGroup root, boolean attachToRoot) {
          final Resources res = getContext().getResources();
          if (DEBUG) {
              Log.d(TAG, "INFLATING from resource: \"" + res.getResourceName(resource) + "\" ("
                      + Integer.toHexString(resource) + ")");
          }
  
          final XmlResourceParser parser = res.getLayout(resource);
          try {
              return inflate(parser, root, attachToRoot);
          } finally {
              parser.close();
          }
      }
```    
    这里将布局文件转换成对应的View对象
    
    继续查看解析xml的方法
```java


    XmlResourceParser loadXmlResourceParser(@AnyRes int id, @NonNull String type)
            throws NotFoundException {
        final TypedValue value = obtainTempTypedValue();
        try {
            final ResourcesImpl impl = mResourcesImpl;
            impl.getValue(id, value, true);
            if (value.type == TypedValue.TYPE_STRING) {
                return impl.loadXmlResourceParser(value.string.toString(), id,
                        value.assetCookie, type);
            }
            throw new NotFoundException("Resource ID #0x" + Integer.toHexString(id)
                    + " type #0x" + Integer.toHexString(value.type) + " is not valid");
        } finally {
            releaseTempTypedValue(value);
        }
    }
```    
    最后防线布局中所有的尺寸最后都在这里进行计算转换为px
    

```java
 public static float applyDimension(int unit, float value,
                                       DisplayMetrics metrics)
    {
        switch (unit) {
        case COMPLEX_UNIT_PX:
            return value;
        case COMPLEX_UNIT_DIP:
            return value * metrics.density;
        case COMPLEX_UNIT_SP:
            return value * metrics.scaledDensity;
        case COMPLEX_UNIT_PT:
            return value * metrics.xdpi * (1.0f/72);
        case COMPLEX_UNIT_IN:
            return value * metrics.xdpi;
        case COMPLEX_UNIT_MM:
            return value * metrics.xdpi * (1.0f/25.4f);
        }
        return 0;
    }
```   

   那么我们修改这个metrics对象对应的一个相对长度  即可达到百分比适配的目的
   具体见代码
   



