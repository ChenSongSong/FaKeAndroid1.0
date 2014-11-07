package com.blackswan.fake.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class CityListView extends ListView implements OnScrollListener
{

	 //隐藏状态
    public static final int PINNED_HEADER_GONE = 0;
    //显示状态
    public static final int PINNED_HEADER_VISIBLE = 1;
   // 表示mHeaderView在上升状态
    public static final int PINNED_HEADER_PUSHED_UP = 2;
    //表示mHeaderView在下拉状态
    public static final int PINNED_HEADER_PUSHED_DOWN = 3;
    
    private View mHeaderView;//最上边的头标签
    private int mHeaderViewId;//头标签的id -- R.id.XXXX
    private boolean mHeaderViewVisible = false;//是否显示头标签
    private int mHeaderViewY = 0;//头标签应该显示的Y轴坐标
    private int mHeaderViewAlpha = 255;//头标签的透明度
    private int lastBottom = 0;//保存头标签应该显示的Y轴坐标
    
    private String mHeaderViewText = "A";
    private int mHeaderViewstate = PINNED_HEADER_VISIBLE;
    
    private int mHeaderViewWidth;
    private int mHeaderViewHeight;
    
    public CityListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnScrollListener(this);//给listview设置OnScrollListener
    }

    public CityListView(Context context) {
        super(context);
        this.setOnScrollListener(this);//给listview设置OnScrollListener
    }

    public CityListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setOnScrollListener(this);//给listview设置OnScrollListener
    }
    
    /**
     * 设置头标签控件
     * @param context 上下文对象
     * @param layout 头标签的layout
     * @param viewId头标签的ID
     */
    public void setPinnedHeaderView(Context context, int layout, int textViewId) {
        //根据控件layout和上下文对象生成头标签VIew
        mHeaderView = LayoutInflater.from(context).inflate(layout, this, false);
        mHeaderViewId = textViewId;//保存文本控件的id
        // Disable vertical fading when the pinned header is present
        // TODO change ListView to allow separate measures for top and bottom fading edge;
        // in this particular case we would like to disable the top, but not the bottom edge.
        if (mHeaderView != null) {
            setFadingEdgeLength(0);//设置边缘退色，没有设置的拖动listview上下边缘会出现颜色
        }
        requestLayout();//调整布局
     }

     @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHeaderView != null) {
            measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
            mHeaderViewWidth = mHeaderView.getMeasuredWidth();//获得头标签的宽高
            mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        }
    }
     
     @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mHeaderView != null) {
            mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);//配置下头标签的显示位置
            configureHeaderView();//配置下头标签（显示位置，颜色，透明度字体等）
        }
    }
    
     @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mHeaderViewVisible && mHeaderView != null) {
            drawChild(canvas, mHeaderView, getDrawingTime());//绘制头标签到listview上
        }
    }
     
     /**
      * 配置头标签（显示位置，颜色，透明度字体等）
      * @param position
      */
     public void configureHeaderView() {
            if (mHeaderView == null) {
                return;
            }
            switch (mHeaderViewstate) {
                case PINNED_HEADER_GONE: {//头标签不显示
                    mHeaderViewVisible = false;
                    break;
                }
                case PINNED_HEADER_VISIBLE: {//头表签显示（默认状态）
                    configurePinnedHeader(mHeaderView, 255);//设置头标签的文字和透明度等
                    if (mHeaderView.getTop() != 0) {
                        mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);//设置头标签的显示位置（最初的默认原始状态）
                    }
                    mHeaderViewVisible = true;
                    break;
                }
                case PINNED_HEADER_PUSHED_UP: {//头标签上升状态
                     mHeaderView.layout(0, mHeaderViewY, mHeaderViewWidth, mHeaderViewHeight + mHeaderViewY);
                     mHeaderViewVisible = true;
                     break;
                }
                case PINNED_HEADER_PUSHED_DOWN://头标签的下拉状态
                     mHeaderView.layout(0, mHeaderViewY, mHeaderViewWidth, mHeaderViewHeight + mHeaderViewY);
                     mHeaderViewVisible = true;
                     break;
            }
        }
     /**
      * 设置头标签的显示文本和状态
      * @param mHeaderViewText 文本内容
      * @param mHeaderViewstate 标签状态
      */
     public void setHeaderViewTextAndState(String mHeaderViewText, int mHeaderViewstate)
     {
         this.mHeaderViewText = mHeaderViewText;
         this.mHeaderViewstate = mHeaderViewstate;
         refreshView(getChildCount());//刷新头标签
     }
     
    /**
     * 设置是否显示头标签是否显示
     * @param mHeaderViewVisible
     */
    public void setmHeaderViewVisible(boolean mHeaderViewVisible) {
        this.mHeaderViewVisible = mHeaderViewVisible;
    }
     /**
      * 配置头标签
      * @param header 头标签
      * @param alpha 透明度
      */
    public void configurePinnedHeader(View header, int alpha) {
        TextView lSectionHeader = (TextView)header;
        lSectionHeader.setText(mHeaderViewText);
        lSectionHeader.setBackgroundColor(Color.rgb(154, 171, 194));
        lSectionHeader.setTextColor(Color.rgb(255, 255, 255));
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
        refreshView(visibleItemCount);//刷新头标签
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // TODO Auto-generated method stub
//      System.out.println("scrollState :" + scrollState);
    }
    /**
     * 刷新头标签
     * @param visibleItemCount listview显示的列数
     */
    private void refreshView(int visibleItemCount)
    {
        
        View firstView = getChildAt(0);//获得显示的第一个控件
        View secondView = null;
        if(visibleItemCount > 2)
        {
            secondView = getChildAt(1);//获得显示的第二个控件
        }
        if(secondView == null)//无第二项，也就没有拖动的必要，不用进行对头标签的绘制变化
        {
            configureHeaderView();//刷新头标签，然后return
            return;
        }
        //获得第一和第二个项的标签
        View firstTvCatalog = firstView.findViewById(mHeaderViewId);
        View secondTvCatalog = secondView.findViewById(mHeaderViewId);
        if(firstTvCatalog == null || secondTvCatalog == null)//无
        {
            configureHeaderView();//刷新头标签，然后return
            return;
        }
        
        if(secondTvCatalog.getVisibility() == View.GONE && firstTvCatalog.getVisibility() == View.GONE)
        {
            mHeaderViewstate = PINNED_HEADER_VISIBLE;//恢复默认的状态
        }
        
        if (firstView != null) {//第一项存在
            mHeaderViewText = (String) firstTvCatalog.getTag();//获得第一项的标签保存的文本
            int firstBottom = firstView.getBottom();//获得第一项的底部距离顶部的高度
            int headerHeight = mHeaderView.getHeight();//获得头标签的高度
          //当第一项的底部距离顶部的高度比 头标签的高度小时，说明头标签的位置需要表化来使下一个含标签的项与头标签粘在一起
            if (firstBottom < headerHeight) {
                mHeaderViewY = (firstBottom - headerHeight);//头标签显示的Y轴位置
                //透明度根据头标签升的高度来定
                mHeaderViewAlpha = 255 * (headerHeight + mHeaderViewY) / headerHeight;
            } else {
                mHeaderViewY = 0;
                mHeaderViewAlpha = 255;
            }
           
            if(secondTvCatalog.getVisibility() == View.VISIBLE)//第二项的标签存在
            {
                ////头标签的top大小和mHeaderViewY不等。说明需要移动头标签
                if (mHeaderView.getTop() != mHeaderViewY) {
                    if(lastBottom > mHeaderViewY)//说明是头标签上移
                    {
                        mHeaderViewstate = PINNED_HEADER_PUSHED_UP;
                    }else if(lastBottom < mHeaderViewY)//头标签下拉
                    {
                        mHeaderViewstate = PINNED_HEADER_PUSHED_DOWN;
                    }else//没动 维持原状态
                    {
                        
                    }
                    
                    lastBottom = mHeaderViewY;
                }else if(headerHeight == firstBottom)//mHeaderView底部与下一个标签顶部重合
                {
                    mHeaderViewstate = PINNED_HEADER_VISIBLE;
                }
            } 
            
            if(firstTvCatalog.getVisibility() == View.VISIBLE)
            {
                if(firstView.getTop() == 0)//mHeaderView与listview的标签重合
                {
                    mHeaderViewstate = PINNED_HEADER_VISIBLE;
                }
                if(secondTvCatalog.getVisibility() == View.GONE)//firstTvCatalog显示，secondTvCatalog不显示，说明不用维持头标签的变化，设置默认状态
                {
                    mHeaderViewstate = PINNED_HEADER_VISIBLE;
                }
            }
            configurePinnedHeader(mHeaderView, mHeaderViewAlpha);
            configureHeaderView();
        }
    }
}
