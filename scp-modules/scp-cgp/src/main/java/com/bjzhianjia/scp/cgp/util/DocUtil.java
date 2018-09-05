package com.bjzhianjia.scp.cgp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

/**
 * @author 尚
 */
public class DocUtil {

    /**
     * 用于处理word文档<br/>
     * 将word文档中特定占位符用相应内容去填充
     * @param srcPath word文档存在路径，包含路径与文件名
     * @param destFileName 目标文件名称，不包含路径
     * @param tempWordPath 目标文件路径，不包含文件名
     * @param map 参数，key表示word文档中的占位符，value为替换占位符对应的内容
     * @return 目标文件存放后的路径，包含路径与文件名
     */
    public static String getDestUrlAfterReplaceWord(String srcPath, String destFileName,String tempWordPath,
        Map<String, String> map) {

        String[] sp = srcPath.split("\\.");
        String destPath = "";
        // judeDirExists(new File(tempWordPath + File.separator));
        if (destFileName != null) {
            destPath = tempWordPath + File.separator + destFileName;
        }
        String[] dp = destPath.split("\\.");
        if ((sp.length > 0) && (dp.length > 0)) {
            /** 比较文件扩展名 **/
            if (sp[sp.length - 1].equalsIgnoreCase("docx")) {
                XWPFDocument document = null;
                try {
                    document = new XWPFDocument(POIXMLDocument.openPackage(srcPath));
                    /** 替换段落中的指定文字 **/
                    Iterator<XWPFParagraph> itPara = document.getParagraphsIterator();
                    while (itPara.hasNext()) {
                        XWPFParagraph paragraph = (XWPFParagraph) itPara.next();
                        List<XWPFRun> runs = paragraph.getRuns();
                        for (int i = 0; i < runs.size(); i++) {
                            String oneparaString =
                                runs.get(i).getText(runs.get(i).getTextPosition());
                            for (Map.Entry<String, String> entry : map.entrySet()) {
                                oneparaString =
                                    oneparaString.replace(entry.getKey(), entry.getValue());
                            }
                            runs.get(i).setText(oneparaString, 0);
                        }
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
                                    if (cellTextString.contains(e.getKey())) cellTextString =
                                        cellTextString.replace(e.getKey(), e.getValue());
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
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
        return destPath;
    }

}
