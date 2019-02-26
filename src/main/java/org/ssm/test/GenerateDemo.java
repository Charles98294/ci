package org.ssm.test;


import java.io.IOException;

import org.hydrakyoufeng.generate.JsGenerater;
import org.hydrakyoufeng.generate.SSMGenerater;


public class GenerateDemo {

	public static void myBatis() {
		new SSMGenerater().getMyBatisString("ci", "tDemo", "Demo", "org.hydrakyoufeng.ci.demo", "E://ci");
	}
	
	public static void js() {
		try {
			new JsGenerater().buildJSString("e://nm/js.html","resultData","msgDiv",true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
//		js();
		myBatis(); 
	}
}
