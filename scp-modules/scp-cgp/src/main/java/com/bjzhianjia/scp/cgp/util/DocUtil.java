package com.bjzhianjia.scp.cgp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

import lombok.extern.log4j.Log4j;

/**
 * @author 尚
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
     * @param tempWordPath
     *            目标文件路径，不包含文件名
     * @param map
     *            参数，key表示word文档中的占位符，value为替换占位符对应的内容
     * @return 目标文件存放后的路径，包含路径与文件名
     */
    public static String getDestUrlAfterReplaceWord(String srcPath, String destFileName,
        String tempWordPath, Map<String, String> map) {

        String[] sp = srcPath.split("\\.");
        String destPath = "";
        // judeDirExists(new File(tempWordPath + File.separator));
        if (destFileName != null) {
            destPath = tempWordPath + destFileName;
        }

        String[] dp = destPath.split("\\.");
        if ((sp.length > 0) && (dp.length > 0)) {
            /** 比较文件扩展名 **/
            if (sp[sp.length - 1].equalsIgnoreCase("docx")) {
                XWPFDocument document = null;
                try {
                    document = new XWPFDocument(POIXMLDocument.openPackage(srcPath));

                    /**
                     * 替换段落中的指定文字
                     */
                    Iterator<XWPFParagraph> itPara = document.getParagraphsIterator();
                    while (itPara.hasNext()) {
                        XWPFParagraph paragraph = (XWPFParagraph) itPara.next();
                        Set<String> set = map.keySet();
                        Iterator<String> iterator = set.iterator();
                        while (iterator.hasNext()) {
                            String key = iterator.next();
                            List<XWPFRun> run = paragraph.getRuns();
                            for (int i = 0; i < run.size(); i++) {
                                String text = run.get(i).getText(0);
                                for (Map.Entry<String, String> e : map.entrySet()) {
                                    if (text != null && text.contains(e.getKey())) {
                                        String replaceRegx = "\\$" + e.getKey() + "\\$";
                                        text = text.replace(replaceRegx, e.getValue());
                                        run.get(i).setText(text, 0);
                                    }
                                }
                            }
                        }
                    }

                    /** 替换段落中的指定文字 **/
                    // Iterator<XWPFParagraph> itPara =
                    // document.getParagraphsIterator();
                    // while (itPara.hasNext()) {
                    // XWPFParagraph paragraph = (XWPFParagraph) itPara.next();
                    // List<XWPFRun> runs = paragraph.getRuns();
                    //
                    // for (int i = 0; i < runs.size(); i++) {
                    // String oneparaString =
                    // runs.get(i).getText(runs.get(i).getTextPosition());
                    // for (Map.Entry<String, String> entry : map.entrySet()) {
                    // String replaceRegx = "\\#$" + entry.getKey() + "\\$";
                    // Pattern pattern = Pattern.compile(replaceRegx);
                    // Matcher matcher = pattern.matcher(oneparaString);
                    //
                    // oneparaString = matcher.replaceAll(entry.getValue());
                    //
                    // // oneparaString =
                    // // oneparaString.replace(entry.getKey(),
                    // // entry.getValue());
                    // }
                    // runs.get(i).setText(oneparaString, 0);
                    // }
                    // }

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

                                        String replaceRegx = "\\$" + e.getKey() + "\\$";
                                        Pattern pattern = Pattern.compile(replaceRegx);
                                        Matcher matcher = pattern.matcher(cellTextString);

                                        cellTextString = matcher.replaceAll(e.getValue());
                                    }
                                }
                                cell.removeParagraph(0);
                                cell.setText(cellTextString);
                            }
                        }
                    }
                    FileOutputStream outStream = null;
                    outStream = new FileOutputStream(destPath);
                    document.write(outStream);
                    outStream.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                } finally {
                    if (document != null) {
                        try {
                            document.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if ((sp[sp.length - 1].equalsIgnoreCase("doc"))
                && (dp[dp.length - 1].equalsIgnoreCase("doc"))) {
                /** doc只能生成doc，如果生成docx会出错 **/
                HWPFDocument document = null;
                try {
                    FileInputStream in = new FileInputStream(new File(srcPath));
                    document = new HWPFDocument(in);
                    Range range = document.getRange();
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        range.replaceText(entry.getKey(), entry.getValue());
                    }
                    FileOutputStream outStream = null;
                    outStream = new FileOutputStream(destPath);
                    document.write(outStream);
                    outStream.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                } finally {
                    if (document != null) {
                        try {
                            document.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
        return destPath;
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

    public static void main(String[] args) {
        String regx = "\\#\\{ssss\\}";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher("花木城asfa#{ssss}");
        System.out.println(matcher.replaceAll("lutuo"));

    }
}
