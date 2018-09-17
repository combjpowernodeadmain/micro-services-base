package com.bjzhianjia.scp.cgp.entity;

/**
 * 
 * Point 经纬度实体类.
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
public class Point {

    public double lng;

    public double lat;

    public Point() {}

    /**
     * 
     * @param lng
     *            经度
     * @param lat
     *   纬度
     */
    public Point(double lng, double lat) {
        this.lng = lng;
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
