package com.els.service;

import org.springframework.stereotype.Service;

import com.els.bean.HomeResult;

@Service
public class HomeService {

	
	
	
	 
	public HomeResult createHome(String uid) {
		// TODO Auto-generated method stub
		HomeResult homeInfo=mapper.selectById(uid);
		return homeInfo;
	}

	public HomeResult inHome(String uid, String hid) {
		// TODO Auto-generated method stub
		return null;
	}

	 

	
	
}
