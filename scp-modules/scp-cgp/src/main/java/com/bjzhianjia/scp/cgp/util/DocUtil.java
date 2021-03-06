package com.bjzhianjia.scp.cgp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

import lombok.extern.log4j.Log4j;

/**
 * DocUtil 用于操作OFFICE文档的工具类.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年9月6日          can      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0
 * @author can
 *
 */
@Log4j
public class DocUtil {

    /**
     * 用于处理word文档<br/>
     * 将word文档中特定占位符用相应内容去填充
     * 
     * @param srcPath
     *            word文档存在路径，包含路径与文件名
     * @param destFileName
     *            目标文件名称，不包含路径
     * @param destPath
     *            目标文件路径，不包含文件名
     * @param map
     *            参数，key表示word文档中的占位符，value为替换占位符对应的内容
     * @return 目标文件存放后的路径，包含路径与文件名
     * @throws Exception
     */
    public static String getDestUrlAfterReplaceWord(String srcPath, String destFileName, String destPath,
        Map<String, String> map) throws Exception {

        String[] sp = srcPath.split("\\.");
        String destFullPath = "";
        if (destFileName != null) {
            destFullPath = destPath + destFileName;
        }

        String copyFile = copyFile(srcPath, destFullPath);

        String[] dp = destFullPath.split("\\.");
        if ((sp.length > 0) && (dp.length > 0)) {
            /** 比较文件扩展名 **/
            if (sp[sp.length - 1].equalsIgnoreCase("docx")) {
                XWPFDocument document = null;
                try {
                    document = new XWPFDocument(POIXMLDocument.openPackage(copyFile));

                    /** 替换段落中的指定文字 **/
                    Iterator<XWPFParagraph> itPara = document.getParagraphsIterator();
                    while (itPara.hasNext()) {
                        XWPFParagraph paragraph = (XWPFParagraph) itPara.next();
                        replaceParagraphChar(map, paragraph);
                    }

                    /** 替换表格中的指定文字 **/
                    Iterator<XWPFTable> itTable = document.getTablesIterator();
                    while (itTable.hasNext()) {
                        XWPFTable table = (XWPFTable) itTable.next();
                        int rcount = table.getNumberOfRows();
                        for (int i = 0; i < rcount; i++) {
                            XWPFTableRow row = table.getRow(i);
                            List<XWPFTableCell> cells = row.getTableCells();
                            for (XWPFTableCell cell : cells) {

                                String cellTextString = cell.getText();
                                for (Map.Entry<String, String> e : map.entrySet()) {
                                    if (cellTextString.contains(e.getKey())) {

                                        /*
                                         * =替换表格中的文字，把每个表格里的内容看作一个普通的段落进行处理
                                         * =当把表格里的cell看作一个整体去对待时，如果每个单元格里有换行(
                                         * =即有段落形式)，则cell.removeParagraph(0)
                                         * =移除的是cell中第一段的内容，
                                         * =不一定是特殊字符所在段落
                                         */
                                        List<XWPFParagraph> paragraphs = cell.getParagraphs();
                                        for (XWPFParagraph paragraph : paragraphs) {
                                            replaceParagraphChar(map, paragraph);
                                        }
                                        /*
                                         * ======================
                                         */
                                    }
                                }
                            }
                        }
                    }
                    FileOutputStream outStream = null;
                    outStream = new FileOutputStream(destFullPath);
                    document.write(outStream);
                    outStream.close();

                } catch (Exception e) {
                    throw e;
                } finally {
                    if (document != null) {
                        try {
                            document.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    // 删除临时文件
                    DocUtil.delete(copyFile);
                }
            } else if ((sp[sp.length - 1].equalsIgnoreCase("doc")) && (dp[dp.length - 1].equalsIgnoreCase("doc"))) {
                /** doc只能生成doc，如果生成docx会出错 **/
                HWPFDocument document = null;
                FileInputStream in = null;
                try {
                    in = new FileInputStream(new File(copyFile));
                    document = new HWPFDocument(in);
                    Range range = document.getRange();
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        /*
                         * By尚
                         * 修改：2018-09-25
                         */
                        String reg = "${" + entry.getKey() + "}";
                        range.replaceText(reg, String.valueOf(entry.getValue()));
                    }
                    FileOutputStream outStream = null;
                    outStream = new FileOutputStream(destFullPath);
                    document.write(outStream);
                    outStream.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                } finally {
                    try {
                        if (document != null) {
                            document.close();
                        }
                        if (in != null) {
                            in.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // 删除临时文件
                    DocUtil.delete(copyFile);
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
        return destFileName;
    }

    private static void replaceParagraphChar(Map<String, String> map, XWPFParagraph paragraph) {
        List<XWPFRun> runs = paragraph.getRuns();

        for (int i = 0; i < runs.size(); i++) {
            String oneparaString = runs.get(i).getText(runs.get(i).getTextPosition());
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (oneparaString != null) {
                    String replaceRegx = "\\$\\{" + entry.getKey() + "}";
                    Pattern pattern = Pattern.compile(replaceRegx);
                    Matcher matcher = pattern.matcher(oneparaString);

                    String rep = String.valueOf(entry.getValue());
                    oneparaString = matcher.replaceAll(rep);
                }
            }
            runs.get(i).setText(oneparaString, 0);
        }
    }

    private static String copyFile(String srcPath, String destPath) {
        String copyPath =
            srcPath.substring(0, srcPath.lastIndexOf(".")) + "_copy" + srcPath.substring(srcPath.lastIndexOf("."));
        log.debug("创建文书模板copy文件，路径为:" + copyPath);

        InputStream is = null;
        OutputStream os = null;

        try {
            is = new FileInputStream(new File(srcPath));
            os = new FileOutputStream(copyPath);
            byte[] bs = new byte[(int) new File(srcPath).length()];

            is.read(bs);

            os.write(bs);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return copyPath;
    }

    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param fileName
     *            要删除的文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            log.debug("文件" + fileName + "不存在，删除失败");
            return false;
        } else {
            if (file.isFile()) return file.delete();
        }
        return false;
    }

    /**
     * 判断文件fileName是否存在
     * 
     * @param fileName
     * @return true:文件存在|false:*
     */
    public static boolean exists(String fileName) {
        File preFile = new File(fileName);
        return preFile.exists();
    }

    /**
     * 删除path路径中带有prefix前缀的文件
     * 
     * @param prefix
     * @param path
     * @return
     */
    public static void deletePrefix(String preffix, String suffix, String path) {
        File files = new File(path);

        String[] names = files.list();
        if (names != null) {
            for (String string : names) {
                if (string.startsWith(preffix) && string.endsWith(suffix)) {
                    delete(path + string);
                }
            }
        }
    }

    /**
     * 删除带特定前缀与后缀的文件
     * 
     * @param preffix
     *            文件前缀
     * @param suffix
     *            文件后缀
     * @param path
     *            文件路
     * @param ignoreFileNameList
     *            不进行删除的文件名列表
     */
    public static void deletePrefix(String preffix, String suffix, String path, List<String> ignoreFileNameList) {
        File files = new File(path);

        String[] names = files.list();
        if (names != null) {
            for (String string : names) {
                if (ignoreFileNameList.contains(string)) {
                    // 说明要删除的文件在忽略的文件名列表中榜上有名，不删除忽略的文件名
                    continue;
                }
                if (string.startsWith(preffix) && string.endsWith(suffix)) {
                    delete(path + string);
                }
            }
        }
    }

    /**
     * 将doc文档转化为pdf文档
     * 
     * @param socDoc
     *            源doc文档，全路径名
     * @param targetDoc
     *            目标路径，全路径名
     * @throws IOException
     */
    public static void WordToPDF(String socDoc, String targetDoc, String openOfficePath, String openOfficeHost,
        Integer openOfficePort) throws IOException {
        // 源文件目录
        File inputFile = new File(socDoc);
        if (!inputFile.exists()) {
//            System.out.println("源文件不存在！");
            return;
        }

        // 输出文件目录
        File outputFile = new File(targetDoc);
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().exists();
        }

        // 调用openoffice服务线程
        StringBuffer commandBuf = new StringBuffer();
        commandBuf.append(openOfficePath).append(" -headless -accept=\"socket,host=").append(openOfficeHost)
            .append(",port=").append(openOfficePort).append(";urp;\"");

        Process p = Runtime.getRuntime().exec(commandBuf.toString());

        // 连接openoffice服务
        OpenOfficeConnection connection = new SocketOpenOfficeConnection("127.0.0.1", 8100);
        connection.connect();

        // 转换
        // DocumentFormat df=new DocumentFormat(null,null, null, ".docx");
        DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
        // converter.convert(inputFile,df, outputFile,null);
        converter.convert(inputFile, outputFile, null);

        // 关闭连接
        connection.disconnect();

        // 关闭进程
        p.destroy();
    }

    /**
     * 将doc文档转化为pdf文档
     * 
     * @param socDoc
     *            源doc文档，全路径名
     * @param targetDoc
     *            目标路径，全路径名
     * @param openOfficeHost
     *            openOffice服务地址
     * @param openOfficePort
     *            openOffice服务端口
     * @throws IOException
     */
    public static void WordToPDF(String socDoc, String targetDoc,String openOfficeHost,
                                 Integer openOfficePort) throws IOException {
        // 源文件目录
        File inputFile = new File(socDoc);
        if (!inputFile.exists()) {
            log.warn("源文件不存在！");
            return;
        }

        // 输出文件目录
        File outputFile = new File(targetDoc);
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().exists();
        }

        // 连接openoffice服务
        log.debug("doc转pdf,开始建立连接：");
        // 将openOffice地址及端口通过配置文件指定
        OpenOfficeConnection connection = new SocketOpenOfficeConnection(openOfficeHost, openOfficePort);
        connection.connect();

        // 转换
        // DocumentFormat df=new DocumentFormat(null,null, null, ".docx");
        DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
        // converter.convert(inputFile,df, outputFile,null);
        converter.convert(inputFile, outputFile, null);

        // 关闭连接
        connection.disconnect();
    }


    /**
     * 获取文件destFileName中的占位符，并将其装入map中
     * 占位符格式如${*}
     * 
     * @param srcFullPath
     * @param destFileName
     * @param map
     * @throws Exception
     */
    public static void getSpecialcharacters(String srcFullPath, Map<String, String> map)
        throws Exception {
        String[] sp = srcFullPath.split("\\.");

        if (sp.length > 0) {
            /** 比较文件扩展名 **/
            if (sp[sp.length - 1].equalsIgnoreCase("docx")) {
                XWPFDocument document = null;
                try {
                    document = new XWPFDocument(POIXMLDocument.openPackage(srcFullPath));

                    // 获取特殊字符列表
                    Iterator<XWPFParagraph> itPara = document.getParagraphsIterator();
                    while (itPara.hasNext()) {
                        XWPFParagraph paragraph = itPara.next();
                        getSpecialCharacterFromParagraph(map, paragraph);
                    }

                    Iterator<XWPFTable> itTable = document.getTablesIterator();
                    while (itTable.hasNext()) {
                        XWPFTable table = itTable.next();
                        int rcount = table.getNumberOfRows();
                        for (int i = 0; i < rcount; i++) {
                            XWPFTableRow row = table.getRow(i);
                            List<XWPFTableCell> cells = row.getTableCells();
                            for (XWPFTableCell cell : cells) {
                                List<XWPFParagraph> paragraphs = cell.getParagraphs();
                                for (XWPFParagraph paragraph : paragraphs) {
                                    getSpecialCharacterFromParagraph(map, paragraph);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    throw e;
                } finally {
                    if (document != null) {
                        try {
                            document.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if (sp[sp.length - 1].equalsIgnoreCase("doc")) {
                /** doc只能生成doc，如果生成docx会出错 **/
                HWPFDocument document = null;
                FileInputStream in = null;
                try {
                    in = new FileInputStream(new File(srcFullPath));
                    document = new HWPFDocument(in);
                    Range range = document.getRange();
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        /*
                         * By尚
                         * 修改：2018-09-25
                         */
                        String reg = "${" + entry.getKey() + "}";
                        range.replaceText(reg, String.valueOf(entry.getValue()));
                    }
                    FileOutputStream outStream = null;
                    outStream = new FileOutputStream(srcFullPath);
                    document.write(outStream);
                    outStream.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (document != null) {
                            document.close();
                        }
                        if (in != null) {
                            in.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
            }
        } else {
        }
    }

    private static void getSpecialCharacterFromParagraph(Map<String, String> map,
        XWPFParagraph paragraph) {
        String paragraphString = paragraph.getText();
        CharSequence charSequence = paragraphString.subSequence(0, paragraphString.length());

        String replaceRegx = "\\$\\{[a-zA-Z_0-9]{1,}}";// 匹配${任意数量字母或数字或下划线}格式的字符串
        Pattern pattern = Pattern.compile(replaceRegx);
        Matcher matcher = pattern.matcher(charSequence);

        int startPosition = 0;
        while (matcher.find(startPosition)) {
            // ${*},将占位符中间的有用字符串截取出来
            String specialCharKey =
                matcher.group().substring(matcher.group().indexOf("{") + 1,
                    matcher.group().length() - 1);

            if (!map.keySet().contains(specialCharKey)) {
                // 如果map集中还未包含该特殊字符
                map.put(specialCharKey, "");
                log.debug("字段"+specialCharKey+"被添加进map集");
            }
            startPosition = matcher.end();
        }
    }
}
