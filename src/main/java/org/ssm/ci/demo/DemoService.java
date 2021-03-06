package org.ssm.ci.demo;

import java.util.List;
import java.util.Map;

import org.hydrakyoufeng.lang.HKFKeys;
import org.hydrakyoufeng.lang.HKFValues;
import org.hydrakyoufeng.tool.collection.MapTool;
import org.hydrakyoufeng.tool.collection.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ssm.j2ee.CiToolkit;

/**
 * 逻辑处理类，框架中推荐该类的所有方法定义均为：Map<String,Object> methodName(Map<String,Object> param)
 * 其中methodName通过框架的解析，例如：XXX/{methodName}.action，Controller会自动根据{methodName}来调用该类中的相同名字的方法。
 * 方法的输入参数Map，是封装过的Map，用于封装用户在Http中的请求参数，同时也将Session和Application也封装进该Map中，
 * 使用session_scope和application_scope可以方便地访问到session和application。
 * 例：session_scope.name等于同操作session域中的name属性（需要搭配MapTool的get或者put等方法使用）。
 * 同SpringMVC一样，方法的输入参数也可以直接加上HttpSession，HttpServletRequest等参数，框架也会直接将这些对象传入进来。
 * 推荐返回为Map<String,Object>类型，这样在处理HTTP请求时，可以方便地传入到request的作用域中。其中url的key表示request的跳转地址，url中使用redirect:开关会完成重定向。
 * 如果是返回json对象，对返回类型无什么要求，框架会自动将结果转换为json对象。
 * @author HydraKyouFeng
 */
@Service("demoService")
public class DemoService {

	/**
	 * 使用的数据持久层对象
	 */
	private DemoDao dao;

	/**
	 * 设置数据持久层对象（通过Spring框架注入）
	 * @param dao
	 */
	@Autowired
	public void setDao(DemoDao dao) {
		this.dao = dao;
	}
	
	/**
	 * 查询Demo列表
	 * @param param
	 * @return
	 */
	public Map<String,Object> queryList(Map<String,Object> param) {
		CiToolkit.pageInit(param);
		Map<String,Object> result;
		try {
			List<?> resultData = dao.queryList(param);
			result = MapUtil.popHashMap(1,HKFValues.MESSAGE_SUCCESS);
			result.put(HKFKeys.COUNT, dao.queryCount(param));
			result.put(HKFKeys.RESULT_DATA,resultData);
			// MapTool.putArray(result,new String[] {"canInsert","canPower","canUpdate"},CiToolkit.hasPower(param, 5,6,10));
		} catch (Exception e) {
			e.printStackTrace();
			result = MapUtil.popHashMap(HKFValues.STATUS_EXCEPTION_SERVER,"数据库异常");
		}
		CiToolkit.pageAttribute(param,result);
		result.put(HKFKeys.URL, "/jsp/demo/list.jsp");
		return result;
	}

	/**
	 * 增加Demo数据
	 * 分为两步，第一部分:跳转到增加页面（判断用户提交的参数中method不是submit时）
	 * 第二部分：处理用户提交的增加表单（判断用户提交的参数中method是submit时）
	 * @param param
	 * @return
	 */
	public Map<String,Object> insert(Map<String,Object> param) {
		int status = 0;
		String message;
		int resultData;
		String url;
		if (MapTool.getString(param, "method").equals("submit")) {
			try {
				resultData = dao.insert(param);
				if (resultData > 0) {
					status = 1;
					message = "增加成功，影响行数：" + resultData;
					url = "redirect:queryList.action";
				} else {
					status = HKFValues.STATUS_FAILED;
					message = "增加失败";
					url = "/jsp/demo/insert.jsp";
				}
			} catch (Exception e) {
				e.printStackTrace();
				status = HKFValues.STATUS_EXCEPTION_SERVER;
				resultData = 0;
				message = "数据库异常";
				url = "/jsp/demo/insert.jsp";
			}
			Map<String,Object> result = MapUtil.popHashMap(status,message);
			result.put(HKFKeys.RESULT_DATA,resultData);
			result.put(HKFKeys.URL, url);
			return result;
		} else {
			Map<String,Object> result = MapUtil.popHashMap(status,HKFValues.MESSAGE_SUCCESS);
			result.put(HKFKeys.URL, "/jsp/demo/insert.jsp");
			return result;
		}
	}
	
	/**
	 * 修改Demo数据
	 * 分为两步，第一部分:跳转到修改页面（判断用户提交的参数中method不是submit时）
	 * 第二部分：处理用户提交的修改表单（判断用户提交的参数中method是submit时）
	 * @param param
	 * @return
	 */
	public Map<String,Object> update(Map<String,Object> param) {
		int status = 0;
		String message;		
		String url;
		Object resultData = null;
		if (MapTool.getString(param, "method").equals("submit")) {
			try {
				int effects = dao.update(param);
				if (effects > 0) {
					status = 1;
					message = "修改成功，影响行数：" + resultData;
					url = "redirect:queryList.action";
					MapTool.remove(param, "session_scope.pk_demo");
				} else {
					status = HKFValues.STATUS_FAILED;
					message = "未找到相应数据";
					resultData = param;
					url = "/jsp/demo/update.jsp";
				}
			} catch (Exception e) {
				e.printStackTrace();
				resultData = 0;
				message = "数据库异常";
				resultData = param;
				url = "/jsp/demo/update.jsp";
			}
			Map<String,Object> result = MapUtil.popHashMap(status,message);
			if (null != resultData) {
				result.put(HKFKeys.RESULT_DATA,resultData);
			}
			result.put(HKFKeys.URL, url);
			return result;
		} else {
			try {
				resultData = dao.queryRow(param);
				if (null == resultData) {
					status = HKFValues.STATUS_FAILED;
					message = "未找到相应数据";
					url = "redirect:queryList.action";
				} else {
					MapTool.copy(param, "id", "session_scope.pk_demo");
					status = 1;
					message = HKFValues.MESSAGE_SUCCESS;
					url = "/jsp/demo/update.jsp";
				}
			} catch (Exception e) {
				e.printStackTrace();
				status = HKFValues.STATUS_EXCEPTION_SERVER;
				message = "数据库异常";
				url = "/jsp/demo/list.jsp";
			}
			Map<String,Object> result = MapUtil.popHashMap(status,message);
			result.put(HKFKeys.URL, url);
			if (null != resultData) {
				result.put(HKFKeys.RESULT_DATA, resultData);
			}
			return result;
		}
	}
	
	/**
	 * 删除Demo数据
	 * @param param 用户的请求参数
	 * @return 返回结果（详情见类注释）
	 */
	public Map<String,Object> delete(Map<String,Object> param) {
		int status = 0;
		String message;
		int effects = 0;
		try {
			effects = dao.delete(param);
			if (effects > 0) {
				status = 1;
				message = "删除成功，影响行数：" + effects;
			} else {
				status = HKFValues.STATUS_FAILED;
				message = "删除失败";
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = HKFValues.STATUS_EXCEPTION_SERVER;
			message = "数据库异常";
		}
		Map<String,Object> result = MapUtil.popHashMap(status,message);
		result.put(HKFKeys.URL, "redirect:queryList.action");
		return result;
	}
}