package com.bg.trans.service;

import com.bg.trans.entity.gyzj.EntoutEntity;
import com.bg.trans.entity.gyzj.EntstatEntity;

public interface ITransGYZJService {
	EntstatEntity getEntstatPeople(String name,String id);
	
	EntstatEntity getEntstatOrg(String name);
	
	EntoutEntity getEntoutPeople(String name,String id);
	
	EntoutEntity getEntoutOrg(String name);
	
	String transformMoney(int level);
}
