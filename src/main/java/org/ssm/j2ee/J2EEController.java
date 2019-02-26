package org.ssm.j2ee;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hydrakyoufeng.j2ee.HKFController;
import org.hydrakyoufeng.j2ee.RequestHandler;
import org.springframework.web.bind.annotation.RequestMapping;

public abstract class J2EEController implements HKFController {
	
	@Resource(name="requestAdapter")
	private RequestHandler requestHandler;

	@Override
	public RequestHandler getRequestHandler() {
		// TODO Auto-generated method stub
		return requestHandler;
	}
	
	@RequestMapping("{method}.action")
	public void startAction(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			doAction(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
