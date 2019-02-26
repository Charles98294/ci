package org.ssm.ci.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.ssm.j2ee.J2EEController;

@Controller
@RequestMapping("demo")
public class DemoController extends J2EEController {

	@Autowired
	private DemoService service;

	@Override
	public Object getService() {
		return service;
	}
}