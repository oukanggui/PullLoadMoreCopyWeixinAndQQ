# PullLoadMoreCopyWeixinAndQQ
仿微信小程序和QQ头部下拉展开更多布局项目，主要参考来源于Github项目[PullLoadXiaochengxu](https://github.com/LucianZhang/PullLoadXiaochengxu)；
本项目改造主要是优化中间内容布局是ListView、RecyclerView或ScrollView等可滚动view时与头部或底部滑动存在的滑动冲突问题，以及参考QQ，处理头部收起以及底部收起时的回弹效果<br>
**效果演示如下：**<br>
![](https://github.com/oukanggui/PullLoadMoreCopyWeixinAndQQ/blob/master/app/gif/yanshi.gif)<br>
关键点是确定事件拦截的临界点，处理好事件分发，本人已在代码中添加了详细的注解说明，有兴趣的同行可以Review查看，共同学习<br>
**事件拦截与消耗代码处理，具体流程可以查看代码中的Log说明，完整代码可以下载demo查看**<br>
```java
@Override
public final boolean onInterceptTouchEvent(MotionEvent event) {
        Log.d(TAG, "onInterceptTouchEvent");
        if (!isInterceptTouchEventEnabled()) {
            return false;
        }
        if (!isPullLoadEnabled() && !isPullRefreshEnabled()) {
            return false;
        }
        final int action = event.getAction();
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mIsHandledTouchEvent = false;
            return false;
        }
        if (action != MotionEvent.ACTION_DOWN && mIsHandledTouchEvent) {
            return true;
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onInterceptTouchEvent,MotionEvent.ACTION_DOWN");
                mLastMotionX = event.getX();
                mLastMotionY = event.getY();
                mIsHandledTouchEvent = false;
                break;

            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "onInterceptTouchEvent,MotionEvent.ACTION_MOVE");
                final float deltaX = event.getX() - mLastMotionX;
                final float deltaY = event.getY() - mLastMotionY;
                final float absDiff = Math.abs(deltaY);
                // 位移差大于mTouchSlop，这是为了防止快速拖动引发刷新
//                if ((absDiff > mTouchSlop)) {
                mLastMotionX = event.getX();
                mLastMotionY = event.getY();
                // 第一步，先处理处理是否处于横滑状态
                if (Math.abs(deltaX) >= Math.abs(deltaY)) {
                    Log.e(TAG, "onInterceptTouchEvent,MotionEvent.ACTION_MOVE,当前处于横滑状态,不拦截，交由父类去处理");
                    return false;
                }
                mPullDown = deltaY > 0;

                if (mPullDown) {
                    Log.d(TAG, "当前处于下拉操作");
                    //下拉需要考虑如下情况：
                    // 1、头部处理
                    // 1）刷新View已到达顶部，判断是否需要
                    if (isPullRefreshEnabled() && checkIsContentViewScrollToTop((int) event.getX(), (int) event.getY())) {
                        Log.d(TAG, "当前处于下拉操作，且内容view已到达顶部，拦截，自己消耗处理，展开头部");
                        return true;
                    } else if (isPullLoadEnabled() && mFooterLayout.getState() == IExtendLayout.State.arrivedListHeight && checkIsContentViewScrollToBottom((int) event.getX(), (int) event.getY())) {
                        Log.d(TAG, "当前处于下拉操作，且内容view已底部且已是展开状态，拦截，自己消耗处理，收缩底部");
                        return true;
                    } else {
                        Log.d(TAG, "当前处于下拉操作，且内容view没有到达顶部/到达底部，不拦截，给子view进行处理,滚动列表");
                        return false;
                    }
                } else {
                    Log.d(TAG, "当前处于上拉操作");
                    if (isPullRefreshEnabled() && mHeaderLayout.getState() == IExtendLayout.State.arrivedListHeight && checkIsContentViewScrollToTop((int) event.getX(), (int) event.getY())) {
                        Log.d(TAG, "当前处于上拉操作，头部已完全展开，且内容view已到达顶部，拦截，自己消耗处理，用于头部收缩");
                        return true;
                    } else if (isPullLoadEnabled() && checkIsContentViewScrollToBottom((int) event.getX(), (int) event.getY())) {
                        Log.d(TAG, "当前处于上拉操作，内容view已到达底部，拦截，自己消耗处理，用于底部展开");
                        return true;
                    } else {
                        Log.d(TAG, "当前处于上拉操作，且内容view没有到达顶部/到达底部，不拦截，给子view进行处理,滚动列表");
                        return false;
                    }
                }
//                }else{
//                    Log.d(TAG,"absDiff <= mTouchSlop,认为不滚动，不处理");
//                }

            case MotionEvent.ACTION_UP:
                Log.d(TAG, "onInterceptTouchEvent,MotionEvent.ACTION_UP");
                break;

            default:
                break;
        }
        return super.onInterceptTouchEvent(event);
    }
    
    @Override
    public final boolean onTouchEvent(MotionEvent ev) {
        boolean handled = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onTouchEvent,MotionEvent.ACTION_DOWN");
                mLastMotionY = ev.getY();
                mIsHandledTouchEvent = false;
                break;

            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getY() - mLastMotionY;
                mLastMotionY = ev.getY();
                // deltaY = 0时，不重置mPullDown标志位，mPullDown标志位值沿用deltaY != 0时的状态值
                if (deltaY != 0) {
                    mPullDown = deltaY > 0;
                }
                Log.d(TAG, "onTouchEvent,MotionEvent.ACTION_MOVE,deltaY = " + deltaY);
                if (isPullRefreshEnabled() && isReadyForPullDown(deltaY)) {
                    // 处理头部滑动
                    Log.d(TAG, "onTouchEvent,MotionEvent.ACTION_MOVE,处理头部滑动");
                    pullHeaderLayout(deltaY / mOffsetRadio);
                    handled = true;
                    if (null != mFooterLayout && 0 != mFooterHeight) {
                        mFooterLayout.setState(IExtendLayout.State.RESET);
                    }
                } else if (isPullLoadEnabled() && isReadyForPullUp(deltaY)) {
                    Log.d(TAG, "onTouchEvent,MotionEvent.ACTION_MOVE,处理底部滑动");
                    // 上拉
                    pullFooterLayout(deltaY / mOffsetRadio);
                    handled = true;
                    if (null != mHeaderLayout && 0 != mHeaderHeight) {
                        mHeaderLayout.setState(IExtendLayout.State.RESET);
                    }
                } else {
                    mIsHandledTouchEvent = false;
                }
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "onTouchEvent,MotionEvent.ACTION_UP");
//                if (mIsHandledTouchEvent) {
//                    mIsHandledTouchEvent = false;
                // 当第一个显示出来时
                if (isReadyForPullDown(0)) {
                    Log.d(TAG, "onTouchEvent,MotionEvent.ACTION_UP，resetHeaderLayout");
                    if (mPullDown) {
                        // 弹性展开头部
                        resetHeaderLayout();
                    } else {
                        // 往上拉时，收缩头部，不弹性会弹
                        collapseHeaderLayout();
                    }
                } else if (isReadyForPullUp(0)) {
                    Log.d(TAG, "onTouchEvent,MotionEvent.ACTION_UP，resetFooterLayout");
                    if (mPullDown) {
                        collapseFooterLayout();
                    } else {
                        resetFooterLayout();
                    }
                }
//                }
                break;

            default:
                break;
        }
        return handled;
    }
  ```  
