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
    public static String getDestUrlAfterReplaceWord(String srcPath, String destFileName,
        String destPath, Map<String, String> map) throws Exception {

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
            } else if ((sp[sp.length - 1].equalsIgnoreCase("doc"))
                && (dp[dp.length - 1].equalsIgnoreCase("doc"))) {
                /** doc只能生成doc，如果生成docx会出错 **/
                HWPFDocument document = null;
                try {
                    FileInputStream in = new FileInputStream(new File(copyFile));
                    document = new HWPFDocument(in);
                    Range range = document.getRange();
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        range.replaceText(entry.getKey(), entry.getValue());
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
            } else {
                return null;
            }
        } else {
            return null;
        }
        return destFullPath;
    }

    private static void replaceParagraphChar(Map<String, String> map, XWPFParagraph paragraph) {
        List<XWPFRun> runs = paragraph.getRuns();

        for (int i = 0; i < runs.size(); i++) {
            String oneparaString = runs.get(i).getText(runs.get(i).getTextPosition());
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if(oneparaString!=null) {
                    String replaceRegx = "\\$\\{" + entry.getKey() + "}";
                    Pattern pattern = Pattern.compile(replaceRegx);
                    Matcher matcher = pattern.matcher(oneparaString);
                    oneparaString=matcher.replaceAll(entry.getValue());
                }
                
             // String replaceRegx = "\\$\\{" +
                // e.getKey() + "\\}";
                // Pattern pattern =
                // Pattern.compile(replaceRegx);
                // Matcher matcher =
                // pattern.matcher(cellTextString);
                //
                // cellTextString =
                // matcher.replaceAll(e.getValue());
                //
                // cell.removeParagraph(0);
                // cell.setText(cellTextString);
                
                
//                if(map.keySet().contains(oneparaString)) {
//                    oneparaString = oneparaString.replace(replaceRegx,entry.getValue());
//                }else {
//                    if(oneparaString.contains("${")&&oneparaString.contains("}")) {
//                        Pattern p = Pattern.compile("\\$\\{[a-zA-Z]*}"); 
//                        Matcher m = p.matcher(oneparaString);
//                        oneparaString = m.replaceAll("");
//                    }
//                }
            }
            runs.get(i).setText(oneparaString, 0);
        }
    }

    private static String copyFile(String srcPath, String destPath) {
        String copyPath =
            srcPath.substring(0, srcPath.lastIndexOf(".")) + "_copy"
                + srcPath.substring(srcPath.lastIndexOf("."));

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
}
