/**
 * @author Chnyge Lin
 */
package com.mybatis.generator;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;


/**
 * @author Chnyge Lin
 *
 */
public class MybatisDemoRun {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
				List<String> warnings = new ArrayList<String>();
		        boolean overwrite = true;
		        String genCfg = "/mbgConfiguration.xml";
		        File configFile = new File(MybatisDemoRun.class.getResource(genCfg).getFile());
		        ConfigurationParser cp = new ConfigurationParser(warnings);
		        Configuration config = null;
		        try {
		            config = cp.parseConfiguration(configFile);
		        } catch (IOException e) {
		            e.printStackTrace();
		        } catch (XMLParserException e) {
		            e.printStackTrace();
		        }
		        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		        MyBatisGenerator myBatisGenerator = null;
		        try {
		            myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
		            System.out.println("创建成功！");
		        } catch (InvalidConfigurationException e) {
		            e.printStackTrace();
		        }
		        try {
		            myBatisGenerator.generate(null);
		        } catch (SQLException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
	}

}
