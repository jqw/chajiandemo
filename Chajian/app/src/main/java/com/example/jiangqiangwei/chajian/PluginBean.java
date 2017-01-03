package com.example.jiangqiangwei.chajian;

/**
 * Created by jiangqiangwei on 17/1/2.
 */

public class PluginBean {
    //插件名称
    private String label;
    //插件包名
    private String pkgname;

    public PluginBean(String pkgname, String label) {
        this.pkgname = pkgname;
        this.label = label;
    }

    public String getPkgname() {
        return pkgname;
    }

    public void setPkgname(String pkgname) {
        this.pkgname = pkgname;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
