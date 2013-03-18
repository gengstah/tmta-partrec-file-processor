package services.tman.dao;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * The default implementation of the Territory Manager
 * Data Access Object
 * 
 * @author Gerard Delas Armas
 * @version 1.0.0
 * @since March 2013
 */
public class JdbcTerritoryManagerDao implements TerritoryManagerDao {
	private DataSource dataSource;
	private String schemaName;
	
	public JdbcTerritoryManagerDao() { }
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	
	@Override
	public void addReport(Map<String, Object> parameters, String tableName) {
		SimpleJdbcInsert insertReport = new SimpleJdbcInsert(dataSource);
		insertReport.setSchemaName(schemaName);
		insertReport.withTableName(tableName).execute(parameters);
	}
}