package org.ploin.pmf.impl;

import static org.junit.Assert.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.ploin.pmf.IPropertiesLoader;
import org.ploin.pmf.MailFactoryException;
import org.ploin.pmf.entity.TemplateConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Here are the testcases for the standardimplementation of the IPropertiesLoader interface.
 * <p/>
 * $LastChangedBy: r.reiz $<br>
 * $Revision: 80 $<br>
 * $Date: 2010-03-18 11:39:35 +0100 (Thu, 18 Mar 2010) $<br>
 */
public class PropertiesLoaderTest {

	private static Log log = LogFactory.getLog(PropertiesLoaderTest.class);
	
	private IPropertiesLoader propertiesLoader;

	/**
	 * Here we setup the TestScenario.
	 */
	@Before
	public void init(){
		propertiesLoader = new PropertiesLoader();
		propertiesLoader.setDirectory("businesslogic/mail");
	}



	/**
	 * In this testcase we test the getter and setter for directory.
	 */
	@Test
	public void getterAndSetterForDirectory(){
		propertiesLoader.setDirectory(null);
		assertNull(propertiesLoader.getDirectory());
		propertiesLoader.setDirectory("businesslogic/mail");
		assertEquals(propertiesLoader.getDirectory(), "businesslogic/mail");
	}

	/**
	 * In this testcase we test the getter and setter for propFile.
	 */
	@Test
	public void getterAndSetterForPropFile(){
		propertiesLoader.setPropFile(null);
		assertNull(propertiesLoader.getPropFile());
		propertiesLoader.setPropFile("mail.properties");
		assertEquals(propertiesLoader.getPropFile(), "mail.properties");
	}



	/**
	 * In this testcase we fetch a message, who are existing in the
	 * client, but not in the directory and not in the root. We expect
	 * NULL.
	 */
	@Test
	public void getMessage1(){
		try {
			propertiesLoader.getValue("just.in.mvv");
			fail();
		} catch (MailFactoryException e) {
			log.debug(e);
		}
	}

	/**
	 * In this testcase we fetch a message, who are in the
	 * directory but not in the root. We expect "JustDirectory".
	 * @throws MailFactoryException 
	 */
	@Test
	public void getMessage2() throws MailFactoryException{
		String value = propertiesLoader.getValue("just.in.directory");
		assertNotNull(value);
		assertEquals(value, "JustDirectory");
	}

	/**
	 * In this testcase we fetch a message, who are in the
	 * root but not in the directory. We expect "JustDirectory".
	 * @throws MailFactoryException 
	 */
	@Test
	public void getMessage3() throws MailFactoryException{
		String value = propertiesLoader.getValue("just.in.root");
		assertNotNull(value);
		assertEquals(value, "JustRoot");
	}

	/**
	 * In this testcase we fetch a message, who are in the
	 * root and in the directory. We expect to fetch "directory", because
	 * the framework search from down to up.
	 * @throws MailFactoryException 
	 */
	@Test
	public void getMessage4() throws MailFactoryException{
		String value = propertiesLoader.getValue("in.all.areas");
		assertNotNull(value);
		assertEquals(value, "directory");
	}

	/**
	 * In this testcase we fetch a message, with the key null.
	 * We expect an Exception. 
	 */
	@Test
	public void getMessage5(){
		try {
			propertiesLoader.getValue(null);
			fail();
		} catch (MailFactoryException e) {
			log.debug(e);
		}
	}

	/**
	 * In this testcase we fetch a message, with the key "".
	 * We expect an Exception.
	 */
	@Test
	public void getMessage6(){
		try {
			propertiesLoader.getValue("");
			fail();
		} catch (MailFactoryException e) {
			log.debug(e);
		}
	}



	/**
	 * In this testcase we fetch a message, who are in the
	 * root and in the directory. We expect to fetch "directory", because
	 * the framework search from down to up.
	 * @throws MailFactoryException 
	 */
	@Test
	public void getMessage7() throws MailFactoryException{
		String value = propertiesLoader.getValue("mvv", "in.all.areas");
		assertNotNull(value);
		assertEquals(value, "client");
	}

	/**
	 * In this testcase we set the propFile to an non existing name.
	 * We expect an Exception.
	 */
	@Test
	public void getMessage8(){
		propertiesLoader.setPropFile("ma.properties");
		try {
			propertiesLoader.getValue("mvv", "in.all.areas");
			fail();
		} catch (MailFactoryException e) {
			log.debug(e);
		}		
	}



	/**
	 * In this testcase we set up a map with a variable.
	 * We expect that, the variable is replaced through "dir".
	 * @throws MailFactoryException 
	 */
	@Test
	public void replaceVariables1() throws MailFactoryException{
		Map<String, String> map = new HashMap<String, String>();
		map.put("variable", "${variable.in.directory}");
		TemplateConfig templateConfig = new TemplateConfig();
		templateConfig.setMap(map);
		propertiesLoader.replaceVariables(templateConfig);
		assertNotNull(templateConfig.getMap());
		assertEquals(templateConfig.getMap().get("variable"), "dir");
	}

	/**
	 * In this testcase we set up a map with a variable and the client "mvv".
	 * We expect that, the variable is replaced through "mvv".
	 * @throws MailFactoryException 
	 */
	@Test
	public void replaceVariables2() throws MailFactoryException{
		Map<String, String> map = new HashMap<String, String>();
		map.put("variable", "${variable.in.client}");
		TemplateConfig templateConfig = new TemplateConfig();
		templateConfig.setClient("mvv");
		templateConfig.setMap(map);
		propertiesLoader.replaceVariables(templateConfig);
		assertNotNull(templateConfig.getMap());
		assertEquals(templateConfig.getMap().get("variable"), "mvv");
	}

	/**
	 * In this testcase we set up a map with a variable and the client "mvv".
	 * We expect that, the variable is replaced through "dir", because
	 * in the mail.properties in the client dir. is no variable with the
	 * given name.
	 * @throws MailFactoryException 
	 */
	@Test
	public void replaceVariables3() throws MailFactoryException{
		Map<String, String> map = new HashMap<String, String>();
		map.put("variable", "${variable.in.directory}");
		TemplateConfig templateConfig = new TemplateConfig();
		templateConfig.setClient("mvv");
		templateConfig.setMap(map);
		propertiesLoader.replaceVariables(templateConfig);
		assertNotNull(templateConfig.getMap());
		assertEquals(templateConfig.getMap().get("variable"), "dir");
	}

	

	/**
	 * In this testcase we want to fetch properties from the client.
	 * We expect an Exception.
	 */
	@Test
	public void getProperites1(){
		try {
			propertiesLoader.getProperties("mvv", null);
			fail();
		} catch (MailFactoryException e) {
			log.debug(e);
		}
	}

	/**
	 * In this testcase we want to fetch properties from the client.
	 * We expect an Exception.
	 */
	@Test
	public void getProperites2(){
		try {
			propertiesLoader.getProperties("mvv", "");
		} catch (MailFactoryException e) {
			log.debug(e);
		}
	}

	/**
	 * In this testcase we want to fetch properties from the client.
	 * We expect not null.
	 */
	@Test
	public void getProperites3() throws MailFactoryException{
		Properties properties = propertiesLoader.getProperties("mvv", "mail.properties");
		assertNotNull(properties);
	}

	/**
	 * In this testcase we want to fetch properties from the directory.
	 * We expect not null.
	 */
	@Test
	public void getProperites4() throws MailFactoryException {
		Properties properties = propertiesLoader.getProperties(null, "mail.properties");
		assertNotNull(properties);
	}

	/**
	 * In this testcase we want to fetch properties from the root.
	 * We expect not null.
	 */
	@Test
	public void getProperites5() throws MailFactoryException {
		Properties properties = propertiesLoader.getProperties(null, "root.properties");
		assertNotNull(properties);
	}

	/**
	 * In this testcase we want to fetch properties from the client.
	 * We expect an Exception.
	 */
	@Test
	public void getProperitesFromClient1(){
		try {
			propertiesLoader.getPropertiesOnlyClient("mvv", null);
			fail();
		} catch (MailFactoryException e) {
			log.debug(e);
		}
	}

	/**
	 * In this testcase we want to fetch properties from the client.
	 * We expect an Exception.
	 */
	@Test
	public void getProperitesFromClient2(){
		try {
			propertiesLoader.getPropertiesOnlyClient("mvv", "");
			fail();
		} catch (MailFactoryException e) {
			log.debug(e);
		}
	}

	/**
	 * In this testcase we want to fetch properties from the directory.
	 * We expect an Exception.
	 */
	@Test
	public void getProperitesFromDirectory(){
		try {
			propertiesLoader.getPropertiesOnlyDirectory("");
			fail();
		} catch (MailFactoryException e) {
			log.debug(e);
		}
	}

	/**
	 * In this testcase we want to fetch properties from the directory.
	 * We expect an Exception.
	 */
	@Test
	public void getProperitesFromDirectory2(){
		try {
			propertiesLoader.getPropertiesOnlyDirectory(null);
			fail();
		} catch (MailFactoryException e) {
			log.debug(e);
		}
	}

	/**
	 * In this testcase we want to fetch properties from the root.
	 * We expect an Exception.
	 */
	@Test
	public void getProperitesFromRoot(){
		try {
			propertiesLoader.getPropertiesOnlyRoot("");
			fail();
		} catch (MailFactoryException e) {
			log.debug(e);
		}
	}

	/**
	 * In this testcase we want to fetch properties from the root.
	 * We expect an Exception.
	 */
	@Test
	public void getProperitesFromRoot2(){
		try {
			propertiesLoader.getPropertiesOnlyRoot(null);
			fail();
		} catch (MailFactoryException e) {
			log.debug(e);
		}
	}

}
