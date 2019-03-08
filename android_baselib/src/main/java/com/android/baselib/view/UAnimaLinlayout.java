package com.android.baselib.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

/**
 * Author:37636
 * QQ:376368673
 * Time:2018/11/8
 * Description:This is 可以拖动 隐藏 显示的 LinearLayout
 */
public class UAnimaLinlayout extends LinearLayout implements View.OnTouchListener {
    //移动方向  true = 向下 ，反之 向上
   private  boolean isMove = true;
    //记录下次动画开始的位置
    float lastY = 0;
    //移动的距离
    float moveY = 0;
    //当前view的高度
    float viewH = 0;
    //是否执行动画
    boolean isAnima = true;
    private GestureDetector gesture; //手势识别
    //露出部分的高度
    int headHeight = 100;
    //回弹距离
    int resDistance = 100;
    /**
     * 设置露出的头部高度
     *
     * @param headHeight
     */
    public void setHeadHeight(int headHeight) {
        this.headHeight = headHeight;
//        ViewGroup.LayoutParams params =  getLayoutParams();
//        params.height = headHeight;
//        setLayoutParams(params);
    }
    //获取当前动画将要执行的状态 向上 还是 向下
    public  boolean getAnimaStatus(){
        return isMove;
    }
    public void setResDistance(int resDistance) {
        this.resDistance = resDistance;
    }

    public UAnimaLinlayout(Context context) {
        super(context);
    }

    public UAnimaLinlayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public UAnimaLinlayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //初始化view 添加动画
    public void addAnima(Context context) {
        gesture = new GestureDetector(context, new MyOnGestureListener());
        setOnTouchListener(this);
    }

//    public void addAnimaView(View view){
//        view.setOnTouchListener((v, event) -> {
//            if (isMove){
//                return false;
//            }
//            float starY = 0;
//            float endY = 0;
//            switch (event.getAction()){
//                case MotionEvent.ACTION_DOWN:
//                    starY =  event.getRawY();
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    endY = event.getRawY();
//                    break;
//                case MotionEvent.ACTION_UP:
//                    break;
//            }
//            moveY = endY-starY;
//            if (isAnima)
//                if (isMove && moveY > 0) {
//                    if (moveY < resDistance) {
//                        Log.e("onScroll", "    lastY = " + lastY);
//                        startAnimator(lastY, moveY);
//                        lastY = moveY;
//                    } else {
//                        isAnima = false;
//                        Log.e("onScroll", "    lastY = " + lastY);
//                        startAnimator(lastY, viewH - headHeight);
//                        isMove = !isMove;
//                        lastY = viewH - headHeight;
//                    }
//                } else if (!isMove && moveY < 0) {
//                    if (Math.abs(moveY) < resDistance) {
//                        startAnimator(lastY, viewH - headHeight + moveY);
//                        lastY = viewH - headHeight + moveY;
//                    } else {
//                        startAnimator(lastY, 0);
//                        isMove = !isMove;
//                        lastY = 0;
//                    }
//                }
//            return  true;
//        });
//    }

    //执行动画
    public void startAnima() {
        if (isMove) {
            startAnimator(0F, getHeight() - headHeight);
            lastY = getHeight() - headHeight;
        } else {
            startAnimator(getHeight() - headHeight, 0F);
            lastY = 0;
        }
        isMove = !isMove;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //添加手势监听
        boolean touch = gesture.onTouchEvent(motionEvent);
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            if (isAnima)
                if (isMove && moveY > 0 && moveY < resDistance) {
                    startAnimator(lastY, 0);
                    lastY = 0;
                } else if (!isMove && moveY < 0 && Math.abs(moveY) < resDistance) {
                    startAnimator(lastY, viewH - headHeight);
                    lastY = viewH - headHeight;
                } else {

                }
        }
        return touch;
    }

    private class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener implements GestureDetector.OnDoubleTapListener {
        @Override//此方法必须重写且返回真，否则onFling不起效
        public boolean onDown(MotionEvent e) {
            //因为 这个方法只执行一次 所以在这里初始化
            isAnima = true;
            //当前View的高度
            viewH = getHeight();
            return true;
        }


        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            moveY = e2.getRawY() - e1.getRawY();
            //当拖动到view的一半时 结束拖动动作
            Log.e("onScroll", "    moveY = " + moveY);
            //动画开关
            if (isAnima)
                if (isMove && moveY > 0) {
                    if (moveY < resDistance) {
                        Log.e("onScroll", "    lastY = " + lastY);
                        startAnimator(lastY, moveY);
                        lastY = moveY;
                    } else {
                        isAnima = false;
                        Log.e("onScroll", "    lastY = " + lastY);
                        startAnimator(lastY, viewH - headHeight);
                        isMove = !isMove;
                        lastY = viewH - headHeight;
                    }
                } else if (!isMove && moveY < 0) {
                    if (Math.abs(moveY) < resDistance) {
                        startAnimator(lastY, viewH - headHeight + moveY);
                        lastY = viewH - headHeight + moveY;
                    } else {
                        startAnimator(lastY, 0);
                        isMove = !isMove;
                        lastY = 0;
                    }
                } else {
//                    return false;
                }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            //用户手指松开（UP事件）的时候如果没有执行onScroll()和onLongPress()
            // 这两个回调的话，就会回调这个，说明这是一个点击抬起事件，
            // 但是不能区分是否双击事件的抬起。
            startAnima();
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            //可以确认（通过单击DOWN后300ms没有下一个DOWN事件确认）这不是一个双击事件，而是一个单击事件的时候会回调。
            Log.e("onSingleTapConfirmed", "");
//            startAnima();
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            //可以确认这是一个双击事件的时候回调
            Log.e("onDoubleTap", "");
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            //onDoubleTap()回调之后的输入事件（DOWN、MOVE、UP）都会回调这个方法
            // （这个方法可以实现一些双击后的控制，如让View双击后变得可拖动等）
            Log.e("onDoubleTapEvent", "");
            return super.onDoubleTapEvent(e);
        }

        @Override
        public void onShowPress(MotionEvent e) {
            //用户按下按键后100ms（根据Android7.0源码）还没有松开或者移动就会回调，
            // 官方在源码的解释是说一般用于告诉用户已经识别按下事件的回调
            // （我暂时想不出有什么用途，因为这个回调触发之后还会触发其他的，不像长按）
            Log.e("onShowPress", "onShowPress = " + e.getRawY());
            super.onShowPress(e);
        }


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //用户执行抛操作之后的回调，MOVE事件之后手松开（UP事件）
            // 那一瞬间的x或者y方向速度，如果达到一定数值（源码默认是每秒50px），
            // 就是抛操作（也就是快速滑动的时候松手会有这个回调，
            // 因此基本上有onFling必然有onScroll）。
            // e1：第1个ACTION_DOWN MotionEvent
            // e2：最后一个ACTION_MOVE MotionEvent
            // velocityX：X轴上的移动速度，像素/秒
            // velocityY：Y轴上的移动速度，像素/秒
            Log.e("onFling", "velocityX = " + velocityX + "    velocityY = " + velocityY);
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    ObjectAnimator objectAnimator;

    private void startAnimator(float startY, float endY) {
        //linearLayout.setTranslationY(moveY);
        objectAnimator = ObjectAnimator.ofFloat(this, "translationY", startY, endY);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setDuration(100);
        objectAnimator.start();
    }

    //所有点击事件都先交给子控件处理
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        switch(ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                //父容器禁止拦截
//                getParent().requestDisallowInterceptTouchEvent(false);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if(事件交给父容器的条件) {
//                    getParent().requestDisallowInterceptTouchEvent(false);
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                break;
//            default:
//                break;
//        }
        return super.dispatchTouchEvent(ev);
    }


    //所有点击事件都先经过父容器拦截处理
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float starY = 0;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //必须返回false，否则子控件永远无法拿到焦点
                starY = ev.getRawY();
                return false;
            case MotionEvent.ACTION_MOVE:
//                if(事件交给子控件的条件) {
//                    return false;
//                } else {
//                    return super.onInterceptTouchEvent(ev);
//                }
                return false;
            case MotionEvent.ACTION_UP:
                //必须返回false,否则子控件永远无法拿到焦点
                return false;
            default:
                return super.onInterceptTouchEvent(ev);
        }

    }

}
