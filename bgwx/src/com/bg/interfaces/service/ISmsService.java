package com.bg.interfaces.service;

import com.bg.interfaces.entity.SendSmsReq;
import com.bg.interfaces.entity.SendSmsRsp;

public interface ISmsService {
	SendSmsRsp sendSms(SendSmsReq req) throws Exception;
}
