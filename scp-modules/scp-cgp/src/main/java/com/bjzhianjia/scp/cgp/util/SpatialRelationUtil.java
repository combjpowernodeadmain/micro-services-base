package com.bjzhianjia.scp.cgp.util;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.bjzhianjia.scp.cgp.entity.Point;

/**
 * 
 * SpatialRelationUtil.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年9月16日          chenshuai      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0
 * @author chenshuai
 *
 */
public class SpatialRelationUtil {

    private SpatialRelationUtil() {}

    /**
     * 返回一个点是否在一个多边形区域内
     *
     * @param mPoints
     *            多边形坐标点列表
     * @param point
     *            待判断点
     * @return true 多边形包含这个点,false 多边形未包含这个点。
     */
    public static boolean isPolygonContainsPoint(List<Point> mPoints, Point point) {
        int nCross = 0;
        for (int i = 0; i < mPoints.size(); i++) {
            Point p1 = mPoints.get(i);
            Point p2 = mPoints.get((i + 1) % mPoints.size());
            // 取多边形任意一个边,做点point的水平延长线,求解与当前边的交点个数
            // p1p2是水平线段,要么没有交点,要么有无限个交点
            if (p1.lat == p2.lat) continue;
            // point 在p1p2 底部 --> 无交点
            if (point.lat < Math.min(p1.lat, p2.lat)) continue;
            // point 在p1p2 顶部 --> 无交点
            if (point.lat >= Math.max(p1.lat, p2.lat)) continue;
            // 求解 point点水平线与当前p1p2边的交点的 lng 坐标
            double lng = (point.lat - p1.lat) * (p2.lng - p1.lng) / (p2.lat - p1.lat) + p1.lng;
            if (lng > point.lng) // 当x=point.x时,说明point在p1p2线段上
                nCross++; // 只统计单边交点
        }
        // 单边交点为偶数，点在多边形之外 ---
        return (nCross % 2 == 1);
    }

    /**
     * 返回一个点是否在一个多边形边界上
     *
     * @param mPoints
     *            多边形坐标点列表
     * @param point
     *            待判断点
     * @return true 点在多边形边上,false 点不在多边形边上。
     */
    public static boolean isPointInPolygonBoundary(List<Point> mPoints, Point point) {
        for (int i = 0; i < mPoints.size(); i++) {
            Point p1 = mPoints.get(i);
            Point p2 = mPoints.get((i + 1) % mPoints.size());
            // 取多边形任意一个边,做点point的水平延长线,求解与当前边的交点个数

            // point 在p1p2 底部 --> 无交点
            if (point.lat < Math.min(p1.lat, p2.lat)) continue;
            // point 在p1p2 顶部 --> 无交点
            if (point.lat > Math.max(p1.lat, p2.lat)) continue;

            // p1p2是水平线段,要么没有交点,要么有无限个交点
            if (p1.lat == p2.lat) {
                double minX = Math.min(p1.lng, p2.lng);
                double maxX = Math.max(p1.lng, p2.lng);
                // point在水平线段p1p2上,直接return true
                if ((point.lat == p1.lat) && (point.lng >= minX && point.lng <= maxX)) {
                    return true;
                }
            } else { // 求解交点
                double lng = (point.lat - p1.lat) * (p2.lng - p1.lng) / (p2.lat - p1.lat) + p1.lng;
                if (lng == point.lng) // 当x=point.x时,说明point在p1p2线段上
                    return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        String data =
            "[{\"lng\":120.121505,\"lat\":31.997819},{\"lng\":120.119206,\"lat\":31.994696},{\"lng\":120.116475,\"lat\":31.993716},{\"lng\":120.113457,\"lat\":31.989306},{\"lng\":120.121362,\"lat\":31.97926},{\"lng\":120.129123,\"lat\":31.977668},{\"lng\":120.14443,\"lat\":31.977116},{\"lng\":120.155066,\"lat\":31.981037},{\"lng\":120.140765,\"lat\":31.989306},{\"lng\":120.14019,\"lat\":31.99292},{\"lng\":120.137747,\"lat\":31.997942},{\"lng\":120.13171,\"lat\":32.001555},{\"lng\":120.126967,\"lat\":32.004617},{\"lng\":120.119134,\"lat\":32.005597},{\"lng\":120.121505,\"lat\":31.999841},{\"lng\":120.121505,\"lat\":31.999841}]";

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            JSONArray array = JSONArray.parseArray(data);
            List<Point> listPoint = array.toJavaList(Point.class);
            double lng = 120.121105;
            double lat = 31.994696;
            Point point = new Point(lng, lat);
             //System.out.println();
            SpatialRelationUtil.isPolygonContainsPoint(listPoint, point);
        }
        System.out.println((System.currentTimeMillis() - start));
    }
}
