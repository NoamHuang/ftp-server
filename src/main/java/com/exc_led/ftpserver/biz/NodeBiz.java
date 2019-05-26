package com.exc_led.ftpserver.biz;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.exc_led.ftpserver.dao.NodeMapper;

/** 
* 调度服务器
* @see		
* @author 	Huang Min
* @date 	2017年11月28日 下午4:00:00
* @version 	1.0.0
* @since 	JDK1.8
*/
@Service
public class NodeBiz {
	@Resource
	private NodeMapper nodeMapper;
	public String findNumByIp(String ip) {
		return nodeMapper.findNumByIp(ip);
	}
}