/**
 * Copyright [2009] [PLOIN GmbH -> http://www.ploin.de]
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
import org.ploin.pmf.IMailReader;
import org.ploin.pmf.MailFactoryException;
import org.ploin.pmf.entity.TemplateConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.net.URL;
import java.util.Locale;

/**
 * Description: Implementation of the {@link IMailReader} interface.
 * This class offers methods for reading the template files.<br/>
 * <p/>
 * $LastChangedBy: r.reiz $<br>
 * $Revision: 77 $<br>
 * $Date: 2010-03-15 13:13:45 +0100 (Mon, 15 Mar 2010) $<br>
 */
public class MailReader implements Serializable, IMailReader{

	private static final long serialVersionUID = 5239257590499731559L;

	private Log log = LogFactory.getLog(MailReader.class);

	private String directory = "";
	private String htmlExtension = ".html";
	private String plainExtension = ".txt";
	

	// *** Public Interface Methods ***
	
	public String getHtmlMail(TemplateConfig templateConfig) throws MailFactoryException {
		return getMail(htmlExtension, templateConfig);
	}

	public String getPlainMail(TemplateConfig templateConfig) throws MailFactoryException {
		return getMail(plainExtension, templateConfig);
	}

	// *** Private Helper Methods ***

	/**
	 * 
	 * @param extension - the extension string. (.html or .txt)
	 * @param templateConfig the config object containing the client and the locale
	 * @return String/Null
	 * @throws Exception
	 */
	private String getMail(String extension, TemplateConfig templateConfig) throws MailFactoryException {

		try {
			String templateName = templateConfig.getName();
			Locale locale = templateConfig.getLocale();
			String languageString = "";
			if (locale != null)
				languageString = "_" + locale.getLanguage();

			String client = templateConfig.getClient();
			String result = readFileFromClient(templateName, extension, client, languageString);

			if (result == null || result.equals("")) 
				result = readFileFromDirectory(templateName, extension, languageString);

			if (result == null || result.equals("")) 
				result = readFileFromRoot(templateName, extension, languageString);

			return result;

		} catch (Exception exception){
			throw new MailFactoryException(exception);
		} 
	}

	/**
	 * 
	 * @param mailName - the mailName without locale and without extension
	 * @param extension - the extension string. (.html or .txt)
	 * @param client - the selected client
	 * @param locale - the selected locale.
	 * @return
	 */
	private String getMailFromClient(String mailName, String extension, String client, String locale){
		StringBuffer pathToFile = new StringBuffer();
		pathToFile.append(directory);
		pathToFile.append("/");
		pathToFile.append(client);
		pathToFile.append("/");
		pathToFile.append(mailName);
		pathToFile.append(locale);
		pathToFile.append(extension);
		return pathToFile.toString();
	}

	/**
	 * 
	 * @param mailName - the mailName without locale and without extension
	 * @param extension - the extension string. (.html or .txt)	 
	 * @param locale - the selected locale.
	 * @return
	 */
	private String getMailFromDirectory(String mailName, String extension, String locale){
		StringBuffer pathToFile = new StringBuffer();
		pathToFile.append(directory);
		pathToFile.append("/");
		pathToFile.append(mailName);
		pathToFile.append(locale);
		pathToFile.append(extension);
		return pathToFile.toString();
	}

	/**
	 * 
	 * @param mailName - the mailName without locale and without extension
	 * @param extension - the extension string. (.html or .txt)
	 * @param locale - the selected locale.
	 * @return
	 */
	private String getMailFromRoot(String mailName, String extension, String locale){
		StringBuffer pathToFile = new StringBuffer();
		pathToFile.append(mailName);
		pathToFile.append(locale);
		pathToFile.append(extension);
		return pathToFile.toString();
	}

	/**
	 * 
	 * @param mailName - the mailName without locale and without extension
	 * @param extension - the extension string. (.html or .txt)
	 * @param client - the selected client
	 * @param languageString - the selected locale.
	 * @return
	 */
	private String readFileFromClient(String mailName, String extension, String client, String languageString){
		String pathToF = getMailFromClient(mailName, extension, client, languageString);
		String result = readFile(pathToF);
		if ( (result == null || result.equals("")) && !languageString.equals("") ) {
			pathToF = getMailFromClient(mailName, extension, client, "");
			result = readFile(pathToF);
		}
		return result;
	}

	/**
	 * 
	 * @param mailName - the mailName without locale and without extension
	 * @param extension - the extension string. (.html or .txt)	 
	 * @param languageString - the selected locale.
	 * @return
	 */
	private String readFileFromDirectory(String mailName, String extension, String languageString){
		String pathToF = getMailFromDirectory(mailName, extension, languageString);
		String result = readFile(pathToF);
		if ( (result == null || result.equals("")) && !languageString.equals("") ) {
			pathToF = getMailFromDirectory(mailName, extension, "");
			result = readFile(pathToF);
		}
		return result;
	}

	/**
	 * 
	 * @param mailName - the mailName without locale and without extension
	 * @param extension - the extension string. (.html or .txt)	 
	 * @param languageString - the selected locale.
	 * @return
	 */
	private String readFileFromRoot(String mailName, String extension, String languageString){
		String pathToF = getMailFromRoot(mailName, extension, languageString);
		String result = readFile(pathToF);
		if ( (result == null || result.equals("")) && !languageString.equals("") ) {
			pathToF = getMailFromRoot(mailName, extension, "");
			result = readFile(pathToF);
		}
		return result;
	}

	/**
	 * 
	 * @param fileName - the name of the file.
	 * @return
	 */
	private String readFile(String fileName){
		fileName = getFilename(fileName);
		if (fileName != null && !fileName.trim().equals("")){
			StringBuffer contents = new StringBuffer();
			File file = new File(fileName);
			if (!file.exists()){
				log.error("file: " + fileName + " does not exist ");
				return null;
			} else {
				log.debug("file: " + fileName + " does exist ");
				try {
					BufferedReader input = new BufferedReader(new FileReader(fileName));
					String line = null;
					while ((line = input.readLine()) != null) {
						contents.append(line);
						contents.append(System.getProperty("line.separator"));
					}
				} catch (Exception e) {
					log.error("Error in readFile ", e);
					return null;
				}
			}
			return contents.toString();
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param fileName the name of the file
	 * @return
	 */
	private String getFilename(final String fileName){
		log.debug("getFilename: " + fileName);
		ClassLoader loader = Thread.currentThread ().getContextClassLoader ();
		if (loader == null) loader = ClassLoader.getSystemClassLoader();
		URL url = loader.getResource(fileName);
		if (url != null){
			return url.getPath().replaceAll("%20", " ");		// workaround for Windows homedir.
		} else {
			return null;
		}
	}

	// *** Getter and Setter Methods ***
	
	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getHtmlExtension() {
		return htmlExtension;
	}

	public void setHtmlExtension(String htmlExtension) {
		this.htmlExtension = htmlExtension;
	}

	public String getPlainExtension() {
		return plainExtension;
	}

	public void setPlainExtension(String plainExtendsion) {
		this.plainExtension = plainExtendsion;
	}

}