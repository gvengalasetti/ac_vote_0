package edu.austincollege.acvote.properties.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import edu.austincollege.acvote.db.JdbcTemplateAbstractDao;
import edu.austincollege.acvote.properties.AcProperties;

/**
 * An instance of this class performs Object-Relational-Mapping (ORM) by transforming
 * entities in our relational database to/from AcProperties objects.
 * 
 * @author guna
 * @author mahiggs
 *
 */
@Component("JdbcPropertiesDao")
public class JdbcPropertiesDao extends  JdbcTemplateAbstractDao implements PropertiesDao{
	private Logger log = LoggerFactory.getLogger(JdbcPropertiesDao.class);

	private class PropertiesMapper implements RowMapper<AcProperties> {

		@Override
		public AcProperties mapRow(ResultSet rs, int rowNum) throws SQLException {
			AcProperties p = new AcProperties(null, null);

			p.setPropval(rs.getString("propval"));
			p.setPropkey(rs.getString("propkey"));

			return p;
		}

	}
	
	/**
	 * Returns a list of all AcProperties derived from our database table.  Might
	 * be empty.   
	 */
	@Override
	public List<AcProperties> listProperties() throws Exception {
		log.debug("listing all properties of import...");

		// querying
		String sql = "SELECT * FROM properties";
		List<AcProperties> results = getJdbcTemplate().query(sql, new PropertiesMapper());

		log.debug("listed all properties");
		return results;	
	}

	/**
	 * Uses the key to fetch an AcProperties object in the database with same key.
	 * 
	 * @return AcProperties instance with key and value from database.
	 * @throws Exception instance if key not found in database
	 */
	@Override
	public AcProperties returnProperty(String key) throws Exception  {
		log.debug("finding property {}", key);
		
		assertNonEmptyKey(key);
		
		//querying
		String sql = String.format("SELECT * FROM properties WHERE propkey= '%s'", key);
		List<AcProperties> results = getJdbcTemplate().query(sql, new PropertiesMapper());
		
		if(results.size()<1) {
			String msg = String.format("no key %s exists", key);
			log.warn(msg);
			throw new Exception(msg);
		}
		
		log.debug("found key {}", key);
		AcProperties r = results.get(0);
		return r;
	
	}

	/**
	 * We use this helper method to make sure the key is 
	 * @param key
	 * @throws Exception if key is empty, null or all whitespace
	 */
	private void assertNonEmptyKey(String key) throws Exception {
		
		if (Strings.isBlank(key)) {
			log.warn("unable to get property [{}]", key);
			throw new Exception("unable to get property: null key");
		}
	}
	

	/**
	 * Creates or updates existing property defined by the specified key,value
	 * parameters.
	 * 
	 * @throws Exception when someting goes wrong.
	 */
	@Override
	public void setProperty(String key, String value) throws Exception {
		
		assertNonEmptyKey(key);  
		key = key.trim();
		
		AcProperties existing = null;
		
		try {
			existing = this.returnProperty(key);  // see if already exists,  if not skip to catch clause
			
			/*
			 * property already exists, so we update it.
			 */
			String sql = (String.format("UPDATE properties SET propval='%s' WHERE propkey='%s'",value, key));

			int num = 0;
			try {
				num=getJdbcTemplate().update(connection->{
					PreparedStatement ps= connection.prepareStatement(sql);
					return ps;
				});
			}
			catch (Exception e) {
				e.printStackTrace();
				// problem with update.
				String msg = String.format("unable to update property %s", key);
				log.warn(msg);
				throw new Exception(msg);
			}
			
		} catch (Exception e) {
			// property did not exist...so we create it in the db.
			createProperty(key, value);
		}
		
	}

	/**
	 * When inserting a new property into database, this method issues the insert sql query.
	 * 
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	private void createProperty(String key, String value) throws Exception {
		
		assertNonEmptyKey(key);
		
		String sql = (String.format("INSERT INTO properties (propkey, propval) VALUES ('%s', '%s')",key, value));
		int num = 0;
		try {
			num=getJdbcTemplate().update(connection->{
				PreparedStatement ps= connection.prepareStatement(sql);
				return ps;
			});
		}
		catch (Exception e) {
			e.printStackTrace();
			String msg = String.format("unable to create property %s", key);
			log.warn(msg);
			throw new Exception(msg);
		}

		if (num != 1) {
			String msg = String.format("unable to create property %s", key);
			log.warn(msg);
			throw new Exception(msg);
		}
		log.debug("created property key {} and value {}", key, value);
	}

	
	@Override
	public void delete(String propertyKey) throws Exception {
		
		assertNonEmptyKey(propertyKey);
		
		log.debug("deleting file import{}", propertyKey);
		int rc = getJdbcTemplate().update(String.format("DELETE FROM properties WHERE propkey = '%s'", propertyKey));
		if(rc !=1) {
			String msg=String.format("unable to delete propery '%s'", propertyKey);
			log.warn(msg);
			throw new Exception(msg);
		}
		else {
			log.debug("deleted property{}", propertyKey);
		}

	}
	

}
