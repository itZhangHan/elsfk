package com.els.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@RequestMapping("show")
	public String show(){
		
		return "ws";
	}
	
	@RequestMapping("hello")
	public String hello(){
		
		return "hello";
	}
	
	@RequestMapping("index")
	public String index(){
		
		return "index";
	}
}
