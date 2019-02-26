package org.ssm.j2ee;

import java.util.List;
import java.util.Map;

import org.hydrakyoufeng.lang.HKFKeys;
import org.hydrakyoufeng.tool.collection.MapTool;

public class CiToolkit {

	public static int ROWS = 10;
	
	public static int ADMIN_ROWS = 30;
	
	/**
	 * 分页功能，初始化参数的分页参数
	 * @param param
	 */
	public static void pageInit(Map<String,Object> param) {
		String uri = MapTool.getString(param, "properties.uri");
		String contextPath = MapTool.getString(param, "properties.contextPath");
		int rows;
		if (uri.startsWith(contextPath + "/admin/")) {
			rows = MapTool.getInt(param, HKFKeys.ROWS,ADMIN_ROWS);
		} else {
			rows = MapTool.getInt(param, HKFKeys.ROWS,ROWS);
		}
		int page = MapTool.getInt(param, HKFKeys.PAGE);
		if (0 == page) {
			page = 1;
		}
		param.put("page_switch", 1);
		param.put(HKFKeys.ROWS, rows);
		param.put(HKFKeys.PAGE, page);
		param.put(HKFKeys.BEGIN, (page - 1) * rows);
		param.put(HKFKeys.END, page * rows);
		param.putIfAbsent(HKFKeys.ORDER, "dtUpdate desc");
	}
	
	public static void pageAttribute(Map<String,Object> param,Map<String,Object> result) {
		int count = MapTool.getInt(result, HKFKeys.COUNT,1);
		int rows = MapTool.getInt(param, HKFKeys.ROWS,15);
		int totalPage = count / rows;
		if (count % rows != 0) {
			totalPage ++;
		}
		MapTool.putIfAbsent(result, HKFKeys.COUNT, 0);
		MapTool.putAll(result, param, HKFKeys.ROWS,HKFKeys.PAGE,HKFKeys.ORDER);
		result.put("totalPage",totalPage);
	}
	
	/**
	 * 获取当前用户在传入的power主键中有无权限
	 * @param param
	 * @param pk_tPowers
	 * @return
	 */
	public static boolean[] hasPower(Map<String,Object> param,int...pk_tPowers) {
		boolean[] b = new boolean[pk_tPowers.length];
		List<Map<String,Object>> powerList = MapTool.get(param, "session_scope.loginAdmin.powerList");
		if (null == powerList) {
			return b;
		}
		for (int i = 0;i < pk_tPowers.length;i ++) {
			for (Map<String,Object> power : powerList) {
				if (MapTool.getInt(power,"pk_tPower") == pk_tPowers[i]) {
					b[i] = true;
					break;
				}
			}
		}
		return b;
	}
}
