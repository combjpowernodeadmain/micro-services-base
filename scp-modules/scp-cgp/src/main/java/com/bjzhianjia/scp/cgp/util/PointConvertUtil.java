package com.bjzhianjia.scp.cgp.util;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author By 尚
 * @date 2019/2/25 12:09
 */
public class PointConvertUtil {

    /**
     * 转换成百度坐标
     * 元数据格式： 数据,x，y
     * 1-5 百度
     */
    public static List<HashMap> printJsonfile1To5(List<String> dataRowList, String outputFileName,
                                                  String outputFilePath,boolean isPrintToDist) {
        try {
            return printJsonFile(dataRowList, outputFileName, outputFilePath,
                    "&from=1&to=5&ak=0kiosjwGbns2RbnvmfYtT6Cc", isPrintToDist);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 转换成高德坐标
     * 元数据格式： 数据,x，y
     * 1-3 高德
     */
    public static List<HashMap> printJsonfile1To3(List<String> dataRowList, String outputFileName,
                                                  String outputFilePath,boolean isPrintToDist) {
        try {
            return printJsonFile(dataRowList, outputFileName, outputFilePath,
                    "&from=1&to=3&ak=0kiosjwGbns2RbnvmfYtT6Cc", isPrintToDist);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 转换成百度坐标
     * 元数据格式： 数据,x，y
     */

    /**
     * 自定义坐标
     *
     * @param dataRowList
     * @param outputFileName
     * @param outputFilePath
     * @param from           源坐标系
     * @param to             目标坐标系
     * @param isPrintToDist
     * @return
     */
    public static List<HashMap> printJsonfile(List<String> dataRowList, String outputFileName, String outputFilePath,
        String from, String to, boolean isPrintToDist) {
        try {
            return printJsonFile(dataRowList, outputFileName, outputFilePath,
                    "&from=" + from + "&to=" + to + "&ak=0kiosjwGbns2RbnvmfYtT6Cc",isPrintToDist);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static List<HashMap> printJsonFile(List<String> dataRowList, String outputFileName, String outputPath,
        String s, boolean isPrintToDist) throws InterruptedException, IOException {
        String url = "http://api.map.baidu.com/geoconv/v1/?";

        int temp = 0;
        List<String> tempList = new ArrayList<>();
        List<HashMap> tlist = new ArrayList<>();
        List<Double[]> dList = new ArrayList<>();
        for (int i = 0; i < dataRowList.size(); i++) {
            if (temp == 50 || i == dataRowList.size() - 1) {
                try {
                    CloseableHttpClient client = null;
                    CloseableHttpResponse response = null;
                    try {
                        HttpGet httpGet = new HttpGet(
                                url + "coords=" + org.apache.commons.lang3.StringUtils
                                        .join(tempList.iterator(), ";") + s);

                        client = HttpClients.createDefault();
                        response = client.execute(httpGet);
                        response.getEntity();
                        HttpEntity entity = response.getEntity();
                        String result = EntityUtils.toString(entity);
                        HashMap hashMap = new ObjectMapper().readValue(result, HashMap.class);
                        hashMap.get("status");
                        List<HashMap> ll = (ArrayList<HashMap>) hashMap.get("result");
                        List<HashMap> tml = new ArrayList<>();
                        for (int l = 0; l < ll.size(); l++) {
                            HashMap hashMap1 = ll.get(l);
                            HashMap<String, String> newT = new HashMap<>();
                            Double[] coordinates=new Double[2];
                            String x = hashMap1.get("x").toString();
                            String y = hashMap1.get("y").toString();
                            newT.put("lng", x);
                            newT.put("lat", y);

                            coordinates[0]=Double.valueOf(x);
                            coordinates[1]=Double.valueOf(y);
                            tml.add(newT);
                            dList.add(coordinates);
                        }

                        tlist.addAll(tml);
                    } finally {
                        if (response != null) {
                            response.close();
                        }
                        if (client != null) {
                            client.close();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Thread.sleep(5000);
                tempList.clear();
                temp = 0;
            }
            if (!StringUtils.isEmpty(dataRowList.get(i))) {
                String[] split = dataRowList.get(i).split(",");
                tempList.add(split[1].trim() + "," + split[2].trim());
            }

            temp++;
        }
        if(isPrintToDist){
            Files.write(Paths.get(outputPath + "json_"+outputFileName + ".txt"), JSONArray.toJSON(tlist).toString().getBytes());
            Files.write(Paths.get(outputPath + "array_"+outputFileName + ".txt"), JSONArray.toJSON(dList).toString().getBytes());
        }
        return tlist;
    }
}
