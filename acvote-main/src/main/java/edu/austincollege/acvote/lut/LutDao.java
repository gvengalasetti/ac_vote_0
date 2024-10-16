package edu.austincollege.acvote.lut;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import edu.austincollege.acvote.db.JdbcTemplateAbstractDao;

/**
 * An instance of this class provides Data Access for several types of 
 * codes in our backend database.  We call these Look Up Table items.  For
 * example the list of valid divisions [ (HU:Humanities),(SC:Sciences), 
 * (SS:Social Sciences), (CW:Campus Wide) ]
 * 
 * @author mahiggs
 *
 */
@Component("LutDao")
public class LutDao extends JdbcTemplateAbstractDao {

	
	public LutDao() {
		super();
	}

	
	/*
	 * No matter what kind of LutItem (Division, Rank, Tenure) we
	 * encapsulate into LutItem instances.
	 */
	private class LutMapper implements RowMapper<LutItem> {
		@Override
		public LutItem mapRow(ResultSet rs, int rowNum) throws SQLException {

			String code = rs.getString("code");
			String lbl = rs.getString("label");
			
			LutItem item = new LutItem(code,lbl);
			
			return item;
		}
	}
	
	/**
	 * Returns the list of divisions from our database;  Can be injected
	 * into our html freemarker templates for building up combo-boxes or 
	 * other widgets.  Might be useful for other reasons.
	 * 
	 * @return List<LutItem> list of valid division codes with labels
	 */
	public List<LutItem> divisions() {
		String sql = "SELECT * FROM divisions order by label";
		List<LutItem> results = getJdbcTemplate().query(sql, new LutMapper());
		return results;
	}
	
	/**
	 * Returns the list of ranks from our database;  Can be injected
	 * into our html freemarker templates for building up combo-boxes or 
	 * other widgets.  Might be useful for other reasons.
	 * 
	 * @return List<LutItem> list of valid division codes with labels
	 */
	public List<LutItem> ranks() {
		String sql = "SELECT * FROM ranks order by label";
		List<LutItem> results = getJdbcTemplate().query(sql, new LutMapper());
		return results;
	}
	
	/*
	 * Additional method for Tenure Line:  NA,T,TT
	 */
	public List<LutItem> tenureStatus() {
		String sql = "SELECT * FROM tenure order by label";
		List<LutItem> results = getJdbcTemplate().query(sql, new LutMapper());
		return results;
	}

	
	/*
	 * Additional method for voting status: NV,VT
	 */
	public List<LutItem> votingStatus() {
		String sql = "SELECT * FROM votestat order by label";
		List<LutItem> results = getJdbcTemplate().query(sql, new LutMapper());
		return results;
	}
	
}
