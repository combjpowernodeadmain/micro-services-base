package com.bjzhianjia.scp.cgp.util;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class PercentUtil {


    /**
     * 获取百分率
     *
     * @param num   所求占比数据
     * @param total 总数据量
     * @param scale 精确到第几位
     * @return
     */
    public static String accuracy(double num, double total, int scale) {
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
        //可以设置精确几位小数
        df.setMaximumFractionDigits(scale);
        //模式 例如四舍五入
        df.setRoundingMode(RoundingMode.HALF_UP);
        double accuracy_num = num / total * 100;
        return df.format(accuracy_num) + "%";
    }
}
