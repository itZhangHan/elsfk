package com.els.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.els.bean.HomeResult;
import com.els.service.HomeService;

@Controller
@RequestMapping("/index")
public class IndexController {

	HomeService sc = new HomeService();

	@RequestMapping(value = "/getTransfer", method = RequestMethod.GET)
	@ResponseBody
	public HomeResult getTransfer(@RequestParam(value = "uid", defaultValue = "0") String uid,
			@RequestParam(value = "hid", defaultValue = "0") String hid,String callback) {
		System.out.println(uid);
		System.out.println(hid);
		//等于空 新建房间返回房间信息。
		if(hid == null ){
			HomeResult newHome=sc.createHome(uid);
			return newHome;
		}else{
			//不等于空的话加入房间，
			HomeResult inHome=sc.inHome(uid,hid);
			return inHome;
		}
		 
	}


}
