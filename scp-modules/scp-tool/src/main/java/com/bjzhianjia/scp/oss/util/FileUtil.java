package com.bjzhianjia.scp.oss.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Calendar;
import java.util.UUID;

/**
 * FilePathUtil 文件路径工具类.
 * <p>
 *
 * <pre>
 * Modification History:
 * Date             Author      Version         Description
 * ------------------------------------------------------------------
 * 2018/10/25          admin      1.0            ADD
 * </pre>
 *
 * @author admin
 * @version 1.0
 */
public class FileUtil {

    /**
     * 通过日期生成文件路径
     *
     * @return 格式：/yyyy/mm/dd/hh/
     */
    public static String datePath() {
        Calendar calendar = Calendar.getInstance();
        StringBuffer datePath = new StringBuffer();
        datePath.append("/").append(calendar.get(Calendar.YEAR))
                .append("/").append(calendar.get(Calendar.MONTH) + 1)
                .append("/").append(calendar.get(Calendar.DAY_OF_MONTH)).append("/");
        return datePath.toString();
    }

    /**
     * 文件路径
     *
     * @param suffix 后缀
     *               格式： .xxx (.pdf|.ppt|.png)
     * @return 返回上传路径
     * 格式: /yyyy/mm/dd/hh/uuid.${suffix}
     */
    public static String getPath(String suffix) {
        //生成uuid
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //文件路径
        return datePath() + uuid + suffix;
    }

    /**
     * 下载文件
     *
     * @param file 下载的文件
     * @return
     * @throws IOException
     */
    public static ResponseEntity<byte[]> download(File file) throws IOException {
        //设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment;filename=" + file.getName());
        headers.add("Content-Type", "application/octet-stream");
        return new ResponseEntity<>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
    }

    /**
     * 获取本地临时文件
     *
     * @param prefix   文件路径前缀   格式：xx/xx (E:/temp | /data/temp)
     * @param filePath 文件路径（baser64编码）
     * @return
     */
    public static File getLocalStorageFile(String prefix, String filePath) {
        if (org.apache.commons.lang3.StringUtils.isBlank(prefix) || StringUtils.isBlank(filePath)) {
            return null;
        }
        byte[] filePaths = Base64.getUrlDecoder().decode(filePath);
        filePath = prefix + new String(filePaths);
        File file = new File(filePath);
        return file;
    }
}