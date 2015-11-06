package com.kcj.phonesuperviser.view.swipeback;

/**
 * @ClassName: SwipeBackActivityBase
 * @Description: 滑动退出基类
 * @author:
 * @date:
 */
public interface SwipeBackActivityBase {

	/**
     * @return the SwipeBackLayout associated with this activity.
     */
    public abstract SwipeBackLayout getSwipeBackLayout();

    public abstract void setSwipeBackEnable(boolean enable);

    /**
     * Scroll out contentView and finish the activity
     */
    public abstract void scrollToFinishActivity();
	
}
