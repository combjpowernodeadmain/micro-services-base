package com.bjzhianjia.scp.cgp.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bjzhianjia.scp.cgp.biz.CoordinatePointBiz;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("coordinatePoint")
public class CoordinatePointController {

    @Autowired
    private CoordinatePointBiz coordinatePointBiz;

    @PostMapping("/convert")
    @ApiOperation("将坐标转换为网格")
    public ObjectRestResponse<Void> convertArcGisPointToAreaGrid(
        @RequestParam(value = "mapTypeFrom") @ApiParam(value = "转换起始类型") String mapTypeFrom,
        @RequestParam(value = "mapTypeTo") @ApiParam(value = "转换目标类型") String mapTypeTo,
        @RequestParam(value = "gLevel") @ApiParam(value = "待转换网格等级") String gLevel,
        @RequestParam(value = "outputFilePath", defaultValue = "D:\\") @ApiParam(value = "坐标文件输出路径，默认'D:\'")
            String outputFilePath,
        @RequestParam(value = "isPrintToDist", defaultValue = "false") @ApiParam(value = "是否输出到硬盘,默认为false")
            boolean isPrintToDist) {
        return coordinatePointBiz
            .convertArcGisPointToAreaGrid(mapTypeFrom, mapTypeTo, gLevel, outputFilePath, isPrintToDist);
    }
}