package com.bjzhianjia.scp.party.vo;

import com.bjzhianjia.scp.party.entity.ExportLog;

/**
 * @author By 尚
 * @date 2019/3/29 9:34
 */
public class ExportLogVo extends ExportLog {

    private String partyMemName;

    private String exportStatusName;

    private String importStatusName;

    public String getPartyMemName() {
        return partyMemName;
    }

    public void setPartyMemName(String partyMemName) {
        this.partyMemName = partyMemName;
    }

    public String getExportStatusName() {
        return exportStatusName;
    }

    public void setExportStatusName(String exportStatusName) {
        this.exportStatusName = exportStatusName;
    }

    public String getImportStatusName() {
        return importStatusName;
    }

    public void setImportStatusName(String importStatusName) {
        this.importStatusName = importStatusName;
    }
}
