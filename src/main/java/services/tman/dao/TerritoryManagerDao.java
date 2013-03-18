package services.tman.dao;

import java.util.Map;

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
}