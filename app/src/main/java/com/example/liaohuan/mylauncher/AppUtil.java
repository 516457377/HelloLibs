package com.example.liaohuan.mylauncher;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

//import com.android.launcher3.AppInfo;
//import com.android.launcher3.compat.LauncherActivityInfoCompat;

/************************
 * @author: gin.chen
 * @version: 2016-1-27 PM 7:58:28
 * @deprecated: Tools for App
 ************************/
public class AppUtil {

	/***************************
	 * @deprecated: Start app
	 * @author: gin.chen
	 * @version ： 2016-1-27 下午7:59:44
	 * @return: The result for start app
	 ***************************/
    public static boolean startApp(Context context, String pkgName,
            String className) {
        if ("com.android.settings".equals(pkgName) && "com.android.settings.McastActivity".equals(className)) {
            
        }
        boolean isStart = true;
        try {
            try {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(pkgName, className));
                intent.setAction(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                isStart = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isStart;
    }

    /***************************
     * @deprecated: Start app
     * @author: gin.chen
     * @version ： 2016-1-27 下午7:59:44
     * @return: The result for start app
     ***************************/
    public static boolean startApp(Context context, String pkgName) {
        boolean isStart = true;
        try {
            if ("com.hpplay.happyplay.aw".equals(pkgName)) {
                pauseMusic(context);
            }
            Intent intent = null;
            if ("com.android.settings".equals(pkgName)) {
                intent = context.getPackageManager()
                        .getLaunchIntentForPackage(pkgName);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }else{
                intent = getIntentByPkg(context, pkgName);
            }
            if (intent != null) {
                context.startActivity(intent);
            }else{
                isStart = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isStart;
    }

	/***************************
	 * @author: gin.chen
	 * @version： 2016-1-27 下午8:03:11
	 ***************************/
	public static void uninstallApp(Context context, String pkgName) {
		Intent intent = new Intent();
		Uri uri = Uri.parse("package:" + pkgName);// 获取删除包名的URI
		intent.setAction(Intent.ACTION_DELETE);// 设置我们要执行的卸载动作
		intent.setData(uri);// 设置获取到的URI
		context.startActivity(intent);
	}
	
	public static boolean isServiceRunning(Context context,String serviceName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> serviceList = manager.getRunningServices(Integer.MAX_VALUE);
        if (serviceList == null || serviceList.size() == 0) {
            return false;
        }
        for (RunningServiceInfo service : serviceList) {
            if (serviceName.equals(service.service.getClassName().trim())) {
                return true;
            }
        }
        return false;
    }

	public static final String ACTION_CHANGE_SOURCE = "source.switch.from.storage";
	public static void pauseMusic(Context context){
        boolean isMusicServiceRun = AppUtil.isServiceRunning(context,"com.jrm.localmm.ui.music.MediaService");
        boolean isApeMusicServiceRun = AppUtil.isServiceRunning(context,"com.jrm.localmm.ui.music.MediaApeService");
//        LogUtil.i("isMusicServiceRun:" + isMusicServiceRun);
        if (isMusicServiceRun || isApeMusicServiceRun) {
            context.sendBroadcast(new Intent("action_music_pause"));
        }
    }
	
	public static void stopMusic(Context context){
        boolean isMusicServiceRun = AppUtil.isServiceRunning(context,"com.jrm.localmm.ui.music.MediaService");
        boolean isApeMusicServiceRun = AppUtil.isServiceRunning(context,"com.jrm.localmm.ui.music.MediaApeService");
//        LogUtil.i("isMusicServiceRun:" + isMusicServiceRun);
        if (isMusicServiceRun || isApeMusicServiceRun) {
            context.sendBroadcast(new Intent(ACTION_CHANGE_SOURCE));
        }
    }
	
	public static boolean isMusicServiceExist(Context context){
	    boolean isMusicServiceRun = AppUtil.isServiceRunning(context,"com.jrm.localmm.ui.music.MediaService");
        boolean isApeMusicServiceRun = AppUtil.isServiceRunning(context,"com.jrm.localmm.ui.music.MediaApeService");
        return isMusicServiceRun || isApeMusicServiceRun;
	}
	
//	public static void startFloatService(Context context){
//	    boolean isFloatServiceRunning = AppUtil.isServiceRunning(context,
//                "com.android.settings" + ".floatwindow.FloatWindowService");
//	    LogUtil.i("isFloatServiceRunning isrunning:" + isFloatServiceRunning);
//        if (isFloatServiceRunning == false) {
//            Intent navIntent = new Intent("com.android.settings.floatwindow");
//            navIntent.setPackage("com.android.settings");
//            context.startService(navIntent);
//        }
//	}
	
	public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object o = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = (Integer) field.get(o);
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
//            LogUtil.i("statusBarHeight:" + statusBarHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }
	
	public static Intent getIntentByPkg(Context context,String pkgName){
	    Intent intent = null;
	    try {
            ActivityManager manager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            ComponentName appTopActivity = null;
            List<RunningTaskInfo> infos = manager.getRunningTasks(100);// 获取当前正在运行的应用列表
            for (RunningTaskInfo info : infos) {
//                System.out.println("topActivity:"
//                        + info.topActivity.getPackageName() + "  baseActivity:"
//                        + info.baseActivity.getPackageName());
                // 判断原app是否还在运行
                if (info.topActivity.getPackageName().equals(pkgName)
                        && info.baseActivity.getPackageName().equals(pkgName)) {
                    if (info.topActivity.getPackageName().equals("com.android.settings") &&
                            info.topActivity.getClassName().equals("com.android.settings.McastActivity")) {
                        appTopActivity = new ComponentName(pkgName, "com.android.settings.Settings");
                    }else
                        appTopActivity = info.topActivity;
                    break;
                }
            }
            if (appTopActivity != null) {
                intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 在receiver或者service里新建activity都要添加这个属性，
                intent.setComponent(appTopActivity);
                // 使用addFlags,而不是setFlags
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 清除掉Task栈需要显示Activity之上的其他activity
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); // 加上这个才不会新建立一个Activity，而是显示旧的
            } else {
                intent = context.getPackageManager()
                        .getLaunchIntentForPackage(pkgName);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return intent;
	}
	
	public static void startBrowserApp(Context context){
	    startApp(context, getBrowserPkg());
	}
	
	public static String getBrowserPkg(){
//	    if (Build.ID.toLowerCase(Locale.getDefault()).startsWith("vd")) {
//            return "com.android.browser";
//        }
	    return "com.android.browser";//com.chaozhuo.browser
	}
	
	public static boolean startFileApp(Context context){
        return startApp(context, "com.coresmore.mfile");
    }

    public static void startApp(Context context, String pkgName, String extra, int value) {
        try {
            if ("com.hpplay.happyplay.aw".equals(pkgName)) {
                pauseMusic(context);
            }
            Intent intent = null;
            if ("com.android.settings".equals(pkgName)) {
                intent = context.getPackageManager()
                        .getLaunchIntentForPackage(pkgName);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }else{
                intent = getIntentByPkg(context, pkgName);
            }
            if (intent != null) {
                intent.putExtra(extra, value);
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void startApp(Context context, Intent intent, Bundle bundle) {
        try {
            String pkgName = intent.getComponent().getPackageName();
            if ("com.hpplay.happyplay.aw".equals(pkgName)) {
                pauseMusic(context);
            }
            if ("com.android.settings".equals(pkgName) && 
                    "com.android.settings.McastActivity".equals(intent.getComponent().getClassName())) {
                
            }else{
                if ("com.android.settings".equals(pkgName)) {
                    intent = context.getPackageManager()
                            .getLaunchIntentForPackage(pkgName);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }else{
                    intent = getIntentByPkg(context, pkgName);
                }
            }
            if (intent != null) {
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static String[] blackList = {"com.coresmore.setlogo","com.android.launcher3","com.android.camera2",
            "com.android.music","com.mstar.android.tv.app","com.mstar.tv.tvplayer.ui","mstar.factorymenu.ui",
            "com.android.gallery3d","com.android.dialer"};//
//    public static void filterList(List<LauncherActivityInfoCompat> apps) {
//        List<String> list = Arrays.asList(blackList);
//        for (int i = 0; i < apps.size(); i++) {
//            LauncherActivityInfoCompat mLauncherActivityInfoCompat = apps.get(i);
//            String pkgName = mLauncherActivityInfoCompat.getApplicationInfo().packageName;
//            if (Build.ID.startsWith("JJS") && pkgName.equals("com.android.launcher3")) {
//                continue;
//            }
//            if (list.contains(pkgName)) {
//                apps.remove(i);
//                i--;
//                continue;
//            }
//        }
//    }
    
//    public static void filterList(ArrayList<AppInfo> apps) {
//        List<String> list = Arrays.asList(blackList);
//        for (int i = 0; i < apps.size(); i++) {
//            AppInfo appInfo = apps.get(i);
//            String pkgName = appInfo.getIntent().getComponent().getPackageName();
//            if (Build.ID.startsWith("JJS") && pkgName.equals("com.android.launcher3")) {
//                continue;
//            }
//            Log.d("======", "filter AppInfo pkgName:" + pkgName);
//            if (list.contains(pkgName)) {
//                apps.remove(i);
//                i--;
//                continue;
//            }
//        }
//    }
    
    public static final int OPTION_STATE_VIDEO = 0x02;
    public static final int OPTION_STATE_SONG = 0x03;
    public static final int OPTION_STATE_PICTURE = 0x04;
    public static void startMedia(Context context,int dataType){
        if (dataType == OPTION_STATE_SONG) {
            if (AppUtil.isMusicServiceExist(context)) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.jrm.localmm", 
                        "com.jrm.localmm.ui.music.MusicPlayerActivity"));
                context.startActivity(intent);
            }else{
                Intent intent = context.getPackageManager()
                        .getLaunchIntentForPackage("com.jrm.localmm");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("dataType", dataType);
                context.startActivity(intent);
            }
        }else{
            if (AppUtil.isMusicServiceExist(context)) {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 在receiver或者service里新建activity都要添加这个属性，
                intent.setComponent(new ComponentName("com.jrm.localmm", 
                        "com.jrm.localmm.ui.main.FileBrowserActivity"));
                // 使用addFlags,而不是setFlags
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 清除掉Task栈需要显示Activity之上的其他activity
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); 
                intent.putExtra("dataType", dataType);
                context.startActivity(intent);
            }else{
                Intent intent = context.getPackageManager()
                        .getLaunchIntentForPackage("com.jrm.localmm");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("dataType", dataType);
                context.startActivity(intent);
            }
        }
    }
    
    public static boolean isInstallApp(Context context,String packageName){  
        PackageManager mPackageManager = context.getPackageManager();
        try {  
            mPackageManager.getApplicationInfo(packageName,PackageManager.GET_UNINSTALLED_PACKAGES);  
            return true;  
        } catch (NameNotFoundException e) {  
            return false;  
        }  catch (Exception e) {  
            return false;  
        }  
    }  
}
