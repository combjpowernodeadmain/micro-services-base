package com.bjzhianjia.scp.merge.demo.entity;

import com.bjzhianjia.scp.merge.annonation.MergeField;
import com.bjzhianjia.scp.merge.demo.feign.IDataFeign;

/**
 * @author scp
 * @create 2018/2/1.
 */
public class User {
    private String name;

    @MergeField(key="sex_key", feign = IDataFeign.class,method = "getGenders")
    private String sex;

    @MergeField(feign = IDataFeign.class,method = "getCities",isValueNeedMerge = true)
    private String city;

    public User(String name, String sex, String city) {
        this.name = name;
        this.sex = sex;
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public User(String name) {
        this.name = name;
    }

    public User(String name, String sex) {
        this.name = name;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
