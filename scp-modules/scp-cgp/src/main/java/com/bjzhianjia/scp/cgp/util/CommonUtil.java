package com.bjzhianjia.scp.cgp.util;

import java.util.Random;

import com.bjzhianjia.scp.cgp.entity.Result;

/**
 * 公共工具类
 * @author 尚
 *
 */
public class CommonUtil {
	/**
	 * 生成立案单编号
	 * @author 尚
	 */
	public static Result<String> generateCaseCode(String oldCode) {
		Result<String> result = new Result<>();
		result.setIsSuccess(false);

		String caseCode = "";
		boolean flag;
		int isLoop = 0;
		do {
			// 当生成事件编号过程出现错误时，则再次调用生成编号的方法，避免因生成编号异常造成程序中断
			flag = false;
			try {
				caseCode = generateCaseCode01(oldCode);
			} catch (Exception e) {
				flag = true;
			}
			isLoop++;
			if (isLoop >= 100) {
				// 如果连续100次生成编号都异常，则也退出程序，避免造成死循环
				result.setIsSuccess(false);
				result.setMessage("生成事件编号失败");
				return result;
			}
		} while (flag);

		result.setIsSuccess(true);
		result.setData(caseCode);
		return result;
	}

	private static String generateCaseCode01(String oldCode) throws Exception {
		// 前缀
		String codePrefix = DateUtil.dateFromDateToStr(new java.util.Date(), "yyyyMM");

		// 事件序号
		String seqMid = "";
		if (oldCode == null) {
			seqMid = "00001";
		} else {
			String seqStr = oldCode.substring(6, 11);
			seqMid = String.format("%05d", Integer.valueOf(seqStr) + 1);
		}

		// 后缀
		Random random = new Random();
		int suffix = random.nextInt(1000);
		String suffixStr=String.format("%03d", suffix);

		StringBuffer caseInfo = new StringBuffer();
		caseInfo.append(codePrefix).append(seqMid).append(suffixStr);

		return caseInfo.toString();
	}
}
