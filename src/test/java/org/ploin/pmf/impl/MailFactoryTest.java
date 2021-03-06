/**
 * Copyright [2009] [PLOIN GmbH -> http://www.ploin-gmbh.de]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.ploin.pmf.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.ploin.pmf.*;
import org.ploin.pmf.entity.MailConfig;
import org.ploin.pmf.entity.SendingResult;
import org.ploin.pmf.entity.TemplateConfig;

import java.util.HashMap;
import java.util.Map;


/**
 * Here are the testcases for the standardimplementation of the IMailFactory interface.
 * <p/>
 * $LastChangedBy: r.reiz $<br>
 * $Revision: 80 $<br>
 * $Date: 2010-03-18 11:39:35 +0100 (Thu, 18 Mar 2010) $<br>
 */
public class MailFactoryTest {

	private static Log log = LogFactory.getLog(MailFactoryTest.class);
	IMailFactory mailFactory;
	MailConfig mailConfig;
	Map<String, String> map = new HashMap<String, String>();


	/**
	 * Before we start the tests, we set up the map.
	 * @throws MailFactoryException 
	 */
	@Before
	public void init() throws MailFactoryException{
		mailFactory = new MailFactory();
		mailFactory.init();
		map.put(":designation:", "Dear Sir RobBob");
		map.put(":reglink:", "I am the link. click me!");
		mailConfig = new MailConfig();
		mailConfig.addToRecipient("TIME Team", "time@ploin.de");
		mailConfig.setSubject("TestMail");
	}


	/**
	 * Here we test to send a mail. In the properties file we
	 * have set "toEmailOverride" and "toNameOverride".
	 * @throws MailFactoryException 
	 */
	@Test
	public void sendMail() throws MailFactoryException{
		boolean result = true;

		mailFactory.getPropertiesLoader().setPropFile("mailTest.properties");

		TemplateConfig templateConfig = new TemplateConfig();
		templateConfig.setMap(map);
		templateConfig.setName("registrationMail");

		SendingResult res = mailFactory.sendMail(mailConfig, templateConfig);
		log.debug("email send: " + res);
		assertTrue(res != null);

		assertTrue(result);
	}


	/**
	 * Here we test to send a mail.
	 */
	@Test
	public void sendMail1(){
		mailFactory.getPropertiesLoader().setPropFile("mail.properties");
		SendingResult result = null;
		try {
			TemplateConfig templateConfig = new TemplateConfig();
			templateConfig.setName("registrationMail");
//			templateConfig.setName("testHtm");
			templateConfig.setMap(map);
			result = mailFactory.sendMail(mailConfig, templateConfig);
			log.debug("email send: " + result.getResult());
		} catch (Exception e) {
			log.error("ERROR ", e);
			result = null;
		}
		assertNotNull(result);
	}
	

	/**
	 * Here we test to send a mail, with two threads.
	 */
	@Test
	public void sendMail2(){
		SendingResult result = null;
		try {
			mailFactory.setSingleThread(false);
			TemplateConfig templateConfig = new TemplateConfig();
			templateConfig.setName("registrationMail");
			templateConfig.setMap(map);
			result = mailFactory.sendMail(mailConfig, templateConfig);
			log.debug("email send: " + result.getResult());
		} catch (Exception e) {
			log.error("ERROR ", e);
			result = null;
		}
		assertNotNull(result);
	}

	/**
	 * Here we test to send a mail, with two threads and through a fallback server.
	 */
	@Test
	public void sendMail3(){
		SendingResult result = null;
		try {
			mailFactory.setSingleThread(false);
			mailFactory.getPropertiesLoader().setPropFile("mail3.properties");
			TemplateConfig templateConfig = new TemplateConfig();
			templateConfig.setName("registrationMail");
			templateConfig.setMap(map);
			result = mailFactory.sendMail(mailConfig, templateConfig);
			log.debug("email send: " + result.getResult());
		} catch (Exception e) {
			log.error("ERROR  ", e);
			result = null;
		}
		assertNotNull(result);
	}


	/**
	 * In this testcase we test the getter and setter for the directroy
	 */
	@Test
	public void getterAndSetterForDirectory(){
		mailFactory.setDirectory("dirYY");
		assertNotNull(mailFactory.getDirectory());
		assertEquals(mailFactory.getDirectory(), "dirYY");
	}

	/**
	 * In this testcase we test the getter and setter for the html extension
	 */
	@Test
	public void getterAndSetterForHtmlExtension(){
		mailFactory.setHtmlExtension(".htm");
		assertNotNull(mailFactory.getHtmlExtension());
		assertEquals(mailFactory.getHtmlExtension(), ".htm");
		assertFalse(mailFactory.getHtmlExtension().equals(".html"));
	}

	/**
	 * In this testcase we test the getter and setter for the plain extension
	 */
	@Test
	public void getterAndSetterForPlainExtension(){
		mailFactory.setPlainExtension(".tx");
		assertNotNull(mailFactory.getPlainExtension());
		assertEquals(mailFactory.getPlainExtension(), ".tx");
		assertFalse(mailFactory.getPlainExtension().equals(".txt"));
	}

	/**
	 * In this testcase we test the getter and setter for the IMailReader
	 */
	@Test
	public void getterAndSetterForMailReader(){
		IMailReader mailreader = new MailReader();
		assertNotNull(mailFactory.getMailReader());
		mailFactory.setMailReader(null);
		assertNull(mailFactory.getMailReader());
		mailFactory.setMailReader(mailreader);
		assertNotNull(mailFactory.getMailReader());
	}

	/**
	 * In this testcase we test the getter and setter for the IMailReplacer
	 */
	@Test
	public void getterAndSetterForMailReplacer(){
		IMailReplacer mailReplacer = new MailReplacer();
		assertNotNull(mailFactory.getMailReplacer());
		mailFactory.setMailReplacer(null);
		assertNull(mailFactory.getMailReplacer());
		mailFactory.setMailReplacer(mailReplacer);
		assertNotNull(mailFactory.getMailReplacer());
	}

	/**
	 * In this testcase we test the getter and setter for the IMailSender
	 */
	@Test
	public void getterAndSetterForMailSender(){
		IMailSender mailSender = new MailSender();
		assertNotNull(mailFactory.getMailSender());
		mailFactory.setMailSender(null);
		assertNull(mailFactory.getMailSender());
		mailFactory.setMailSender(mailSender);
		assertNotNull(mailFactory.getMailSender());
	}

	/**
	 * In this testcase we test the getter and setter for the IPropertiesLoader
	 */
	@Test
	public void getterAndSetterForPropertiesLoader(){
		IPropertiesLoader pLoader = new PropertiesLoader();
		assertNotNull(mailFactory.getPropertiesLoader());
		mailFactory.setPropertiesLoader(null);
		assertNull(mailFactory.getPropertiesLoader());
		mailFactory.setPropertiesLoader(pLoader);
		assertNotNull(mailFactory.getPropertiesLoader());
	}

	/**
	 * In this testcase we test the getter and setter for the varialbe "singleThread"
	 */
	@Test
	public void getterAndSetterForSingleThread(){
		mailFactory.setSingleThread(true);
		assertTrue(mailFactory.isSingleThread());
		mailFactory.setSingleThread(false);
		assertFalse(mailFactory.isSingleThread());
	}
	


	/**
	 * In this testcase we fetch the plainTemplate filled with the
	 * variables from the map.
	 * We expect not null and "regTemplate1.txt". 
	 * @throws MailFactoryException 
	 */
	@Test
	public void getPlainMessage() throws MailFactoryException{
		TemplateConfig templateConfig = new TemplateConfig();
		templateConfig.setClient("mvv");
		templateConfig.setName("regTemplate");
		templateConfig.setMap(map);
		String plain = mailFactory.getPlainMessage(templateConfig);
		assertEquals("regTemplate1.txt", plain.trim());
	}

	/**
	 * In this testcase we call the method with a wrong parameter.	 
	 * We expect an Exception
	 */
	@Test
	public void getPlainMessage1(){
		TemplateConfig templateConfig = new TemplateConfig();
		templateConfig.setClient(null);
		templateConfig.setName("regTemplate");
		templateConfig.setMap(null);
		try {
			mailFactory.getPlainMessage(templateConfig);
			fail();
		} catch (MailFactoryException e) {
			log.debug(e);
		}
	}

	/**
	 * In this testcase we call the method with a wrong parameter.
	 * We expect an Exception
	 */
	@Test
	public void getPlainMessage2(){
		TemplateConfig templateConfig = new TemplateConfig();
		templateConfig.setClient(null);
		templateConfig.setName(null);
		templateConfig.setMap(map);
		try {
			mailFactory.getPlainMessage(templateConfig);
			fail();
		} catch (MailFactoryException e) {
			log.debug(e);
		}
	}

	/**
	 * In this testcase we call the method with a wrong parameter.
	 * We expect an Exception
	 */
	@Test
	public void getPlainMessage3(){
		TemplateConfig templateConfig = new TemplateConfig();
		templateConfig.setClient(null);
		templateConfig.setName("");
		templateConfig.setMap(map);
		try {
			mailFactory.getPlainMessage(templateConfig);
			fail();
		} catch (MailFactoryException e) {
			log.debug(e);
		}
	}

	/**
	 * In this testcase we fetch the htmlTemplate filled with the
	 * variables from the map.
	 * We expect not null and "regTemplate1".
	 */
	@Test
	public void getHtmlMessage() throws MailFactoryException {
		TemplateConfig templateConfig = new TemplateConfig();
		templateConfig.setClient("mvv");
		templateConfig.setName("regTemplate");
		templateConfig.setMap(map);
		String html = mailFactory.getHtmlMessage(templateConfig);
		assertEquals("regTemplate1", html.trim());
	}

	/**
	 * In this testcase we call the method with a wrong parameter.
	 * We expect an Exception
	 */
	@Test
	public void getHtmlMessage2(){
		TemplateConfig templateConfig = new TemplateConfig();
		templateConfig.setClient("mvv");
		templateConfig.setName(null);
		
		try {
			mailFactory.getHtmlMessage(templateConfig);
			fail();
		} catch (MailFactoryException e) {
			log.debug(e);
		}
	}

	/**
	 * In this testcase we call the method with a wrong parameter.
	 * We expect an Exception
	 */
	@Test
	public void getHtmlMessage3(){

		TemplateConfig templateConfig = new TemplateConfig();
		templateConfig.setClient("mvv");
		templateConfig.setName("");
		
		try {
			mailFactory.getHtmlMessage(templateConfig);
			fail();
		} catch (MailFactoryException e) {
			log.debug(e);
		}
	}

	/**
	 * In this testcase we call the method with a wrong parameter.
	 * We expect an Exception
	 */
	@Test
	public void getHtmlMessage4(){

		TemplateConfig templateConfig = new TemplateConfig();
		templateConfig.setClient("mvv");
		templateConfig.setName("regTemplate");

		try {
			mailFactory.getHtmlMessage(templateConfig);
			fail();
		} catch (MailFactoryException e) {
			log.debug(e);
		}
	}

}
