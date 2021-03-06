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
package org.ploin.pmf;

import org.ploin.pmf.entity.MailConfig;
import org.ploin.pmf.entity.TemplateConfig;

import java.util.Properties;

/**
 * Description: This class is responsible for reading properties files
 * <p/>
 * $LastChangedBy: r.reiz $<br>
 * $Revision: 77 $<br>
 * $Date: 2010-03-15 13:13:45 +0100 (Mon, 15 Mar 2010) $<br>
 */
public interface IPropertiesLoader {

	/**
	 * Returns a message with the key "key" from the property file "propFile".
	 * It looks for a properties file in the following directories in this order:
	 * <ol>
	 * <li>mail directory</li>
	 * <li>class-root</li>
	 * </ol>
	 * If the key is null, the method throws a MailFactoryException. <br/>
	 * <br/>
	 * @param key :the key to search
	 * @return String :the value to the key.
	 */
	String getValue(String key) throws MailFactoryException;

	/**
	 * Returns a message with the key "key" from the property file.
	 * It looks for a properties file in the following directories in this order:
	 * <ol>
	 * <li>client directory</li>
	 * <li>mail directory</li>
	 * <li>class-root</li>
	 * </ol>
	 * 
	 * @param client :the client for the mail
	 * @param key :the key to search
	 * @return String :the value to the key.
	 */
	String getValue(String client, String key) throws MailFactoryException;

	/**
	 * This method replaces all variables with keys like "${variable}" in the map of the templateConfig with values
	 * found in the mail.properties having the key "variable".
	 *
	 * @param templateConfig containing client, locale, template-name and map.
	 */
	void replaceVariables(TemplateConfig templateConfig) throws MailFactoryException;

	/**
	 * This method replaces all variables with keys like "${variable}" in the email-addresses of the receivers (to, cc, bcc) with values
	 * found in the mail.properties having the key "variable".
	 *
	 * @param mailConfig containing receiver-Emailadresses (to, cc, bcc)
	 */
	void replaceVariables(String client, MailConfig mailConfig) throws MailFactoryException;	
	
	/**
	 * This method looks for a properties file with the given name "propName".<br>
	 * It looks for a properties file in the following directories in this order:
	 * <ol>
	 * <li>client directory</li>
	 * <li>mail directory</li>
	 * <li>class-root</li>
	 * </ol>
	 * 
	 * @param client
	 *            - the client of the mail
	 * @param propName
	 *            - the name of the properties file
	 * @return A Properties Object
	 */
	Properties getProperties(String client, String propName) throws MailFactoryException;

	/**
	 * This method looks for a properties file with the given name "propertyFilename".<br>
	 * It looks for the file only in the client-directory. If there is no file
	 * with the given name it throws an MailFactoryException.
	 * 
	 * @param client
	 *            - the client of the mail
	 * @param propertyFilename
	 *            - the file name of the properties file.
	 * @return A Properties Object
	 */
	Properties getPropertiesOnlyClient(String client, String propertyFilename) throws MailFactoryException;

	/**
	 * This method looks for a properties file with the given name "propertyFilename".<br>
	 * It looks for the file only in the mail-directory. If there is no file
	 * with the given name it throws an MailFactoryException.
	 * 
	 * @param propertyFilename
	 *            - the client of the mail
	 * @return A Properties Object
	 */
	Properties getPropertiesOnlyDirectory(String propertyFilename) throws MailFactoryException;

	/**
	 * This method looks for a properties file with the given name "propertyFilename".<br>
	 * It looks for the file only in the root-directory (class-root). If there
	 * is no file with the given name it throws an MailFactoryException.
	 * 
	 * @param propertyFilename
	 *            - the client of the mail
	 * @return A Properties Object
	 */
	Properties getPropertiesOnlyRoot(String propertyFilename) throws MailFactoryException;

	/**
	 * Gets the path to the properties file.
	 * 
	 * @return the path to the directory there the mail-templates are placed
	 */
	String getDirectory();

	/**
	 * Sets the path to the properties file.
	 * 
	 * @param directory
	 *            - path to the directory there the mail-templates are placed.
	 */
	void setDirectory(String directory);

	/**
	 * Gets the name of the properties file.
	 * 
	 * @return the name of the properties files. The standard is
	 *         "mail.properties".
	 */
	String getPropFile();

	/**
	 * Sets the name of the properties file.
	 * 
	 * @param propFile
	 *            - the name of the properties files.
	 */
	void setPropFile(String propFile);
}
