package com.teliasonera.cdl.kpis.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class HiveDaoServiceImpl {
	final static Logger logger = LoggerFactory.getLogger(HiveDaoServiceImpl.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void saveDataUsageKpiToHiveTable(final List<AuditDO> listData) {
		
		String insertSQL = "insert into table t_kpi_data_usage values(?,?,?,?,?,?,?)";
		try {
			jdbcTemplate.batchUpdate(insertSQL, new BatchPreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps, int index) throws SQLException {
					AuditDO data = listData.get(index);
					ps.setString(1, data.getDate());
					ps.setString(2, data.getUsername());
					ps.setString(3, data.getSourcename());
					ps.setString(4, data.getStream());
					ps.setString(5, data.getTanant());
					ps.setString(6, data.getDatabasename());
					ps.setString(7, data.getTabelname());
				}
				
				@Override
				public int getBatchSize() {
					return listData.size();
				}
			});
		} catch (Exception e) {
			logger.error("Error occured while inserting events to hive table t_kpi_data_usage: " + e);
			e.printStackTrace();
		} finally {
			
		}
	}
}
