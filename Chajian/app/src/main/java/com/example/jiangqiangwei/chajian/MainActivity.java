package com.example.jiangqiangwei.chajian;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

/**
 * 针对PathClassLoader的使用
 * 记载已经安装在手机里面的apk
 */
public class MainActivity extends AppCompatActivity {

    private ImageView imgShow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgShow = (ImageView) findViewById(R.id.img_show);
    }

    public void onclick(View view){
        Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();
        List<PluginBean> allPlugin = PluginShareUserIdUtils.getPluginShareUserIdUtils(this)
                .findAllPlugin("com.shareone.chajian");
        //只安装了一个插件
        if(allPlugin.size()>0){
            PluginBean pluginBean = allPlugin.get(0);
            Log.i(">>>>>",">>>>>"+pluginBean.getLabel()+">>>>"+pluginBean.getPkgname());
            Context plugnContext;
            try {
                plugnContext = this.createPackageContext(pluginBean.getPkgname(), CONTEXT_IGNORE_SECURITY | CONTEXT_INCLUDE_CODE);
                try {
                    //根据插件的包名，环境变量，以及资源的名称查找出资源
                    int resouceId = PluginShareUserIdUtils.getPluginShareUserIdUtils(this).dynamicLoadApk(pluginBean.getPkgname()
                    ,plugnContext,"one");
                    Log.i(">>>>>",">>>>>"+resouceId);
                    //显示在这个app上面
                        imgShow.setImageResource(resouceId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        }else {
            Toast.makeText(this, "找不到安装的插件", Toast.LENGTH_SHORT).show();
        }

    }
}
