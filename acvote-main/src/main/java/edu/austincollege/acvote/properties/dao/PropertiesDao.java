package edu.austincollege.acvote.properties.dao;

import java.util.List;

import edu.austincollege.acvote.properties.AcProperties;

/**
 * An interface that creates a Dao object that can be used to check if the import for faculty members 
 * has been made. 
 *
 */
public interface PropertiesDao {
	
	/**
	 * Will get last known updates and faculty file uploaded.
	 * @return list of property key and value, import file name, and string. 
	 * @throws Exception
	 */
	public List<AcProperties> listProperties() throws Exception;
	/**
	 * 
	 * @param Property Key.
	 * @return Property key.
	 * @throws Exception
	 */
	public AcProperties returnProperty(String key)  throws Exception;
	
	/**
	 * 
	 * @param Property Key, and Property Value
	 * @return Key and Value.
	 * @throws Exception
	 */
	public void setProperty(String key,String value)  throws Exception;
	
	
	/**
	 * Deletes file of matching key.
	 * 
	 * @param key
	 * @throws Exception
	 */
	public void delete(String propertyKey) throws Exception;
}

