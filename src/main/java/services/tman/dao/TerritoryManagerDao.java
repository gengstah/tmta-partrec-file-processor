package services.tman.dao;

import java.util.Map;

/**
 * The main data access object interface
 * This interface provides methods to
 * insert actual data to a data store
 * 
 * @author Gerard
 * @version 1.0.0
 * @see services.tman.dao.JdbcTerritoryManagerDao
 */
public interface TerritoryManagerDao {
	/**
	 * This method is used for inserting a row of actual data
	 * in the specified table. The map of parameters are used
	 * by this method to populate the row.
	 * 
	 * @param parameters The map of parameters that consist of the 
	 * column name as map key and value to be stored as map value 
	 * @param tableName The table name that will be used to load
	 * the actual data
	 */
	void addReport(Map<String, Object> parameters, String tableName);
	
	/**
	 * This method is used for inserting a row of actual data
	 * in the specified table. The map of parameters are used
	 * by this method to populate the row.
	 * 
	 * This is an overloaded method to let the client pass in
	 * the column names that will be used instead of the key of
	 * parameter map. This is useful when the client needs to
	 * escape the column names for their tables first.
	 * 
	 * @param parameters The map of parameters that consist of the 
	 * column name as map key and value to be stored as map value 
	 * @param tableName The table name that will be used to load
	 * the actual data
	 * @param columnNames The array of column names to be used
	 */
	void addReport(Map<String, Object> parameters, String tableName, String[] columnNames);
}