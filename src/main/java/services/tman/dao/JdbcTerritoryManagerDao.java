package services.tman.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
	private List<String> ignoreColumns;
	
	public JdbcTerritoryManagerDao() { }
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	
	@Override
	public void addReport(Map<String, Object> parameters, String tableName) {
		removeIgnoreColumns(parameters);
		SimpleJdbcInsert insertReport = new SimpleJdbcInsert(dataSource);
		insertReport.setSchemaName(schemaName);
		insertReport.withTableName(tableName).execute(parameters);
	}

	@Override
	public void addReport(Map<String, Object> parameters, String tableName,
			String[] columnNames) {
		removeIgnoreColumns(parameters);
		columnNames = removeIgnoreColumns(columnNames);
		SimpleJdbcInsert insertReport = new SimpleJdbcInsert(dataSource);
		insertReport.setSchemaName(schemaName);
		
		String escapedColumnNames[] = new String[columnNames.length];
		int arrayIndex = 0;
		for(String columnName : columnNames) {
			escapedColumnNames[arrayIndex++] = "\"" + columnName + "\"";
		}
		
		insertReport.withTableName(tableName)
			.usingColumns(escapedColumnNames)
			.execute(parameters);
	}
	
	private void removeIgnoreColumns(Map<String, Object> parameters) {
		for(String columnName : ignoreColumns) {
			parameters.remove(columnName);
		}
	}
	
	private String[] removeIgnoreColumns(String[] columnNames) {
		List<String> columnNamesList = new ArrayList<String>();
		Collections.addAll(columnNamesList, columnNames);
		
		for(String columnName : ignoreColumns) {
			columnNamesList.remove(columnName);
		}
		
		return columnNamesList.toArray(new String[columnNamesList.size()]);
	}

	public List<String> getIgnoreColumns() {
		return ignoreColumns;
	}

	public void setIgnoreColumns(List<String> ignoreColumns) {
		this.ignoreColumns = ignoreColumns;
	}
}