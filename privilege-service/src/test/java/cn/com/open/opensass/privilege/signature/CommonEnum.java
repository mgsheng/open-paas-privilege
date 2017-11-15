package cn.com.open.opensass.privilege.signature;

/**
 * 项目名称 : project.
 * 创建日期 : 2017/8/29 17:47.
 * 版本 : 1.0.
 * 修改历史 :
 * 1. [2017/8/29][17:47]创建文件 by JACKIE.
 */
public enum CommonEnum {
    APP_ID("8"),
    APP_KEY("df6bda1a157d49cea82447c3e925dd6d"),
    APP_SECRET("67d73cec5d6b4c8a8a9883748f4066fe"),

    ;

    String code="";
    String display="";

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    CommonEnum(String display) {
        this(display,"");
    }

    CommonEnum(String display, String code) {
        this.display = display;
        this.code = code;
    }
}
