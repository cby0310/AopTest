package com.example.lib.aop;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.View;
import android.widget.Toast;


import com.example.lib.aop.annotation.NetCheck;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Created by pc on 2017/9/16.
 */
@Aspect
public class SectionAspect {

    @Around("checkNetBehavior()")
    public Object checkNet(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MyLog.e("checkNet");
        // 做埋点  日志上传  权限检测（我写的，RxPermission , easyPermission） 网络检测
        // 网络检测；
        // 1.获取 CheckNet 注解  NDK  图片压缩  C++ 调用Java 方法
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        NetCheck check = methodSignature.getMethod().getAnnotation(NetCheck.class);
        if (check != null) {
            // 2.判断有没有网络  怎么样获取 context?
            Object object = proceedingJoinPoint.getThis();// View Activity Fragment ； getThis() 当前切点方法所在的类
            Context context = getContext(object);
            if (context != null) {
                if (isNetworkAvailable(context)) {
                    // 3.没有网络不要往下执行
                    Toast.makeText(context, "请检查您的网络", Toast.LENGTH_LONG).show();
                    return null;
                }
            }
        }

        return proceedingJoinPoint.proceed();
    }


    /**
     * 通过对象获取上下文
     *
     * @param object
     * @return
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private Context getContext(Object object) {
        if (object instanceof Activity) {
            return (Activity) object;
        } else if (object instanceof Fragment) {
            Fragment fragment = (Fragment) object;
            return fragment.getActivity();
        } else if (object instanceof View) {
            View view = (View) object;
            return view.getContext();
        }
        return null;
    }


    /**
     * 检查当前网络是否可用
     *
     * @return
     */
    private static boolean isNetworkAvailable(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * 找到处理的切点
     * * *(..)  可以处理所有的方法
     */

//    @Pointcut("execution(* *..Activity+.*(..))")
    @Pointcut("execution(@com.example.lib.aop.annotation.NetCheck * *(..))")
    public void checkNetBehavior() {

    }
}
