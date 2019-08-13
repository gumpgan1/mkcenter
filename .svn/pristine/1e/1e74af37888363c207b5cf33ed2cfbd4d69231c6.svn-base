/**
 * @author Chnyge Lin
 */
package com.mybatis.generator;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mktech.entity.DbLimo;
import com.mktech.service.impl.DbLimoServiceImpl;

/**
 * @author Chnyge Lin
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:*spring-mvc.xml" })
public class JunitRun {
	
	@Autowired
	private DbLimoServiceImpl dbLimoService;

	@Test
	public void test() {
		int min = 1;
		int max = 10;
		List<DbLimo> list = dbLimoService.selectByKeyRange(min, max);
		for (DbLimo dbLimo : list) {
			System.out.println(min + " : " + dbLimo.toString());
		}
	}

}
