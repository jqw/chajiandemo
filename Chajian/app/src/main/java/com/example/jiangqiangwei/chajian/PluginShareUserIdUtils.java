package com.example.jiangqiangwei.chajian;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.PathClassLoader;

/**
 * Created by jiangqiangwei on 17/1/2.
 */

public class PluginShareUserIdUtils {
    private Context context;
    private static PluginShareUserIdUtils pluginShareUserIdUtils;

    private PluginShareUserIdUtils(Context context) {
        this.context = context;
    }

    public static PluginShareUserIdUtils getPluginShareUserIdUtils(Context context) {
        synchronized (PluginShareUserIdUtils.class) {
            if (pluginShareUserIdUtils == null) {
                pluginShareUserIdUtils = new PluginShareUserIdUtils(context);
                return pluginShareUserIdUtils;
            }
            return pluginShareUserIdUtils;
        }
    }

    /**
     * 查找手机内所有的插件
     *
     * @return 返回一个插件List
     */
    public List<PluginBean> findAllPlugin(String shareuserid) {
        List<PluginBean> plugins = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        //通过包管理器查找所有已安装的apk文件
        List<PackageInfo> packageInfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (PackageInfo info : packageInfos) {
            //得到当前apk的包名
            String pkgName = info.packageName;
            //得到当前apk的sharedUserId
            String shareUesrId = info.sharedUserId;
            //判断这个apk是否是我们应用程序的插件
            if (shareUesrId != null && shareUesrId.equals(shareuserid) && !pkgName.equals(context.getPackageName())) {
                String label = pm.getApplicationLabel(info.applicationInfo).toString();//得到插件apk的名称
                PluginBean bean = new PluginBean(label, pkgName);
                plugins.add(bean);
            }
        }
        return plugins;
    }

    /**
     * 加载已安装的apk
     *
     * @param packageName   应用的包名
     * @param pluginContext 插件app的上下文
     * @return 对应资源的id
     */
    public int dynamicLoadApk(String packageName, Context pluginContext, String resname) throws Exception {
        //第一个参数为包含dex的apk或者jar的路径
        Log.v("zxy", pluginContext.getPackageResourcePath());
        PathClassLoader pathClassLoader = new PathClassLoader(pluginContext.getPackageResourcePath(), ClassLoader.getSystemClassLoader());
//        Class<?> clazz = pathClassLoader.loadClass(packageName + ".R$mipmap");//通过使用自身的加载器反射出mipmap类进而使用该类的功能
        //参数：1、类的全名，2、是否初始化类，3、加载时使用的类加载器
        Class<?> clazz = Class.forName(packageName + ".R$mipmap", true, pathClassLoader);
        //使用上述两种方式都可以，这里我们得到R类中的内部类mipmap，通过它得到对应的图片id，进而给我们使用
        Field field = clazz.getDeclaredField(resname);
        int resourceId = field.getInt(R.mipmap.class);
        return resourceId;
    }
}
