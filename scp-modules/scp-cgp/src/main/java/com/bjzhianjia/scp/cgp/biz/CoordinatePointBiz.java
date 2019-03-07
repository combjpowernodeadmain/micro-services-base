package com.bjzhianjia.scp.cgp.biz;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.entity.AreaGrid;
import com.bjzhianjia.scp.cgp.entity.CoordinatePoint;
import com.bjzhianjia.scp.cgp.mapper.CoordinatePointMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.util.PointConvertUtil;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author By Shang
 * @version 2019-03-01 11:14:02
 * @email ${email}
 */
@Service
@Slf4j
public class CoordinatePointBiz extends BusinessBiz<CoordinatePointMapper, CoordinatePoint> {

    @Autowired
    private AreaGridBiz areaGridBiz;

    public ObjectRestResponse<Void> convertArcGisPointToAreaGrid(String mapTypeFrom, String mapTypeTo, String gLevel,
        String outputFilePath, boolean isPrintToDist) {
        ObjectRestResponse<Void> restResult = new ObjectRestResponse<>();
        log.info("正在转换网格，这可能需要一些时间。。。");

        /*
         * 1-> 获取gLevel等级的网格--distinct
         * 2-> 逐个对gLevel下的坐标点进行处理
         * 3-> 将处理后的结果保存到数据及打出文件到本地硬盘
         */

        List<CoordinatePoint> coordinatePoints = getDistinctCoordinatePoints(gLevel);
        if (BeanUtil.isNotEmpty(coordinatePoints)) {
            coordinatePoints.forEach(coordinatePoint -> {
                List<CoordinatePoint> certainCoordinateList = getCoordinatePointsByCode(coordinatePoint);
                if (BeanUtil.isNotEmpty(certainCoordinateList)) {
                    log.info(
                        "正在转换网格:" + certainCoordinateList.get(0).getCName() + ",坐标点数为:" + certainCoordinateList.size());

                    List<String> dataRowList03 = new ArrayList<>();
                    certainCoordinateList.forEach(certainCoordinate -> {
                        dataRowList03.add(
                            certainCoordinate.getFid() + "," + certainCoordinate.getPointX() + "," + certainCoordinate
                                .getPointY());
                    });

                    AreaGrid areaGridLevel = new AreaGrid();
                    areaGridLevel.setGridName(certainCoordinateList.get(0).getCName());
                    areaGridLevel.setGridCode(String.valueOf(certainCoordinateList.get(0).getCurrentCode()));

                    // 查询当前网格的父级网格
                    Map<String, String> conditions = new HashMap<>();
                    conditions.put("gridCode", certainCoordinateList.get(0).getParentCode());
                    List<AreaGrid> areaGridByMap = areaGridBiz.getByMap(conditions);
                    AreaGrid parentAreaGrid = null;
                    StringBuilder currentAreaGridFileName = new StringBuilder();
                    if (BeanUtil.isNotEmpty(areaGridByMap)) {
                        // 网格code唯一
                        parentAreaGrid = areaGridByMap.get(0);
                    }
                    if (parentAreaGrid == null) {
                        areaGridLevel.setGridParent(-1);
                    } else {
                        areaGridLevel.setGridParent(parentAreaGrid.getId());
                        currentAreaGridFileName.append(parentAreaGrid.getGridName());
                    }

                    currentAreaGridFileName.append(",").append(certainCoordinateList.get(0).getCName());

                    List<HashMap> hashMaps03 = PointConvertUtil
                        .printJsonfile(dataRowList03, currentAreaGridFileName.toString(), outputFilePath, mapTypeFrom,
                            mapTypeTo,isPrintToDist);

                    String s03 = JSONObject.toJSONString(hashMaps03);
                    log.debug(coordinatePoint.getCName() + "==>网格MapInfo:" + s03);

                    areaGridLevel.setGridLevel(gLevel);
                    areaGridLevel.setCrtTime(new Date());
                    areaGridLevel.setMapInfo(s03);
                    areaGridBiz.insertSelective(areaGridLevel);
                }
            });
        }

        restResult.setStatus(200);
        restResult.setMessage("坐标转换完成");
        return restResult;
    }

    private List<CoordinatePoint> getCoordinatePointsByCode(CoordinatePoint coordinatePoint) {
        Example example = new Example(CoordinatePoint.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("currentCode", coordinatePoint.getCurrentCode());
        return this.selectByExample(example);
    }

    private List<CoordinatePoint> getDistinctCoordinatePoints(String gLevel) {
        Example example = new Example(CoordinatePoint.class).selectProperties("currentCode");
        example.setDistinct(true);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("gLevel", gLevel);
        return this.selectByExample(example);
    }
}