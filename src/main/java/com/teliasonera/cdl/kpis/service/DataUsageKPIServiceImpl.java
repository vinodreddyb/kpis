package com.teliasonera.cdl.kpis.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.teliasonera.cdl.kpis.connection.HDFSConnectionManager;
import com.teliasonera.cdl.kpis.connection.NavigatiorConnectionManager;
import com.teliasonera.cdl.kpis.dao.AuditDO;
import com.teliasonera.cdl.kpis.response.AuditResponse;
import com.teliasonera.cdl.kpis.response.ServiceValues;
import com.teliasonera.cdl.kpis.utils.DateUtils;

@Component
public class DataUsageKPIServiceImpl implements DataUsageKPIService {

	private static final String PARTTION_FORMAT = "%d-%d-%d %d:%d:%d";
	final static Logger logger = LoggerFactory.getLogger(DataUsageKPIServiceImpl.class);
	@Value("${navigator.baseurl}")
	private String baseUrl;

	@Autowired
	private NavigatiorConnectionManager navigatiorConnectionManager;

	@Value("${kpi.datausage.data.path}")
	private String dataUsageKpiPath;

	@Value("${kpi.datausage.data.filename}")
	private String dataUsageKpiFile;

	@Value("${kpi.datausage.newsources.path}")
	private String newSourcesKpiPath;

	@Value("${kpi.datausage.newsources.filename}")
	private String newSourcesKpiFile;
	
	@Value("${kpi.datausage.volums.path}")
	private String dataVolumsSavingPath;

	@Value("${kpi.datausage.volumns.filename}")
	private String dataVolumsSavingFile;
	

	@Autowired
	private HDFSConnectionManager hDFSConnectionManager;

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * This method get all queried events from navigator on hive tables. Useful
	 * in finding most accessed sources
	 */
	public void extractAndStoreDataUsageEvents(long startTime, long endTime) {

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + "/audits")
				// Add query parameter
				.queryParam("query", "service==hive;command==QUERY").queryParam("startTime", startTime)
				.queryParam("endTime", endTime).queryParam("limit", 10000);

		List<AuditResponse> response = navigatiorConnectionManager.executeAudit(builder.toUriString());
		Pattern sourcePattern = Pattern.compile("_(.*?)_");

		List<String> outputData = new ArrayList<>();
		List<AuditDO> auditData = new ArrayList<>();
		Set<String> resoucesPaths = new HashSet<>();

		Pattern partitionPattern = Pattern.compile("\\d+");

		for (AuditResponse audit : response) {
			ServiceValues sv = audit.getServiceValues();
			String tableName = sv.getTableName();
			String dataBase = sv.getDataBaseName();
			String sourceName = null;
			String tanant = null;
			String stream = null;
			if (StringUtils.isNotBlank(tableName) && StringUtils.isNotBlank(dataBase) && !dataBase.contains("raw")
					&& !tableName.contains("raw") && !tableName.contains("temp") && !tableName.contains(":")) {

				String resourcePath = sv.getResourcePath();
				if (StringUtils.isNotBlank(resourcePath)) {
					resoucesPaths.add(resourcePath);
					Matcher matcher = partitionPattern.matcher(resourcePath);
					String resourcePathWithoutPartition = null;
					if (matcher.find()) {
						String subPath = resourcePath.substring(0, resourcePath.indexOf(matcher.group()));
						resourcePathWithoutPartition = subPath.substring(0, subPath.lastIndexOf("/"));
					} else {
						resourcePathWithoutPartition = resourcePath;
					}
					String[] tokens = resourcePathWithoutPartition.split("/");

					if (tokens.length > 3)
						tanant = tokens[3];

					if (tokens.length > 5)
						sourceName = tokens[5];

					if (tokens.length > 6) {
						StringBuilder build = new StringBuilder();
						for (int i = 6; i < tokens.length; i++) {
							build.append(tokens[i]);
							if (i != tokens.length - 1)
								build.append(":");
						}

						stream = build.toString();
					}

				} else {
					continue;
				}

				AuditDO auditDO = new AuditDO();
				auditDO.setDate(dateFormat.format(audit.getTimestamp()));
				auditDO.setDatabasename(dataBase);
				auditDO.setTabelname(tableName);
				auditDO.setTanant(tanant);
				auditDO.setStream(stream);
				auditDO.setUsername(audit.getUsername());
				auditDO.setSourcename(sourceName);
				auditDO.setResourcePath(resourcePath);
				auditDO.setPartition(extractPartitionDate(resourcePath));
				auditData.add(auditDO);
				System.out.println(auditDO);
			}

		}

		// System.out.println(resoucesPaths);

		Map<String, Double> spaceMap = hDFSConnectionManager.getSpaceOccupation(resoucesPaths);

		// System.out.println(spaceMap);

	///	Map<String, Map<String, Double>> totalUsedSpaceSoureAndStreamWise = new HashMap<>();

		for (AuditDO auditDO : auditData) {
			String resourcePath = auditDO.getResourcePath();
			auditDO.setSize((spaceMap.containsKey(resourcePath)) ? spaceMap.get(resourcePath) : 0.0);

			/*if (totalUsedSpaceSoureAndStreamWise.containsKey(auditDO.getSourcename())) {
				Map<String, Double> spaceForStream = totalUsedSpaceSoureAndStreamWise.get(auditDO.getSourcename());
				Double space = spaceForStream.get(auditDO.getStream());
				if (space != null) {
					spaceForStream.put(auditDO.getStream(), space + auditDO.getSize());
				} else {
					spaceForStream.put(auditDO.getStream(), auditDO.getSize());
				}
			} else {
				Map<String, Double> streamSize = new HashMap<>();
				streamSize.put(auditDO.getStream(), auditDO.getSize());
				totalUsedSpaceSoureAndStreamWise.put(auditDO.getSourcename(), streamSize);
			}*/

			outputData.add(auditDO.toString());
			System.out.println(auditDO);
		}
		// hiveDaoServiceImpl.saveDataUsageKpiToHiveTable(outputData);
		logger.info("inserting data usage kpi. No of records " + outputData.size() + " records");

		hDFSConnectionManager.saveKpiToHDFS(dataUsageKpiPath + "/" + dataUsageKpiFile, outputData);

		logger.info("insert data operation completed. Added " + outputData.size() + " records");
		System.out.println("insert data operation completed. Added " + outputData.size() + " records");

	}

	/**
	 * 
	 */
	public void extractVolumeSnapShot() {

		List<String> outputData = new ArrayList<>();
		Map<String, Double> volumnsMap = hDFSConnectionManager.getSpaceBySourceAndStream();
		String date = dateFormat.format(new Date());
		for (Map.Entry<String, Double> entry : volumnsMap.entrySet()) {
			String key = entry.getKey();
			String[] keyTokens = key.split(":");
			// Consider only stream volume. We are getting entire
			/**
			 * For example Cem volume is swe:base:cem--->66921.78818451427 But
			 * we also get streams volumns under cem . So we have consider only
			 * streams > swe:base:cem:aif_call_raw--->40.571665758267045
			 * swe:base:cem:aif_ho_raw--->135.82613619137555
			 */
			if (keyTokens.length > 3) {
				
				StringBuilder stream = new StringBuilder();
				for(int i = 3 ; i< keyTokens.length ; i++) {
					if(i != 3) stream.append(':');
					stream.append(keyTokens[i]);
				}
				
				outputData.add(date + "," + keyTokens[0] + "," + keyTokens[1] +"," +  keyTokens[2] + "," + stream.toString() + "," + entry.getValue());
			}
		}

		logger.info("inserting data volume snapshot. No of records " + outputData.size() + " records");

		hDFSConnectionManager.saveKpiToHDFS(dataVolumsSavingPath + "/" + dataVolumsSavingFile, outputData);

		logger.info("inserting data volume snapshot. Added " + outputData.size() + " records");
		System.out.println("inserting data volume snapshot. Added " + outputData.size() + " records");
	}

	/**
	 * This method get folder creation events from navigator. Useful to identify
	 * if any new source added.
	 */
	public void extractHDFSFolderCreationEvents() {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + "/audits")
				// Add query parameter
				.queryParam("query", "service==hdfs;command==mkdirs")
				.queryParam("startTime", DateUtils.START_DATE.getDate())
				.queryParam("endTime", DateUtils.END_DATE.getDate());

		List<AuditResponse> response = navigatiorConnectionManager.executeAudit(builder.toUriString());
		List<String> outputData = new ArrayList<>();
		for (AuditResponse audit : response) {
			String date = dateFormat.format(audit.getTimestamp());
			String resource = audit.getResource();
			String impersonator = audit.getImpersonator();
			String tanant = null;
			String source = null;
			String stream = null;
			if (impersonator.startsWith("httpfs") && !resource.contains("/.")) {
				String[] tokens = resource.split("/");

				switch (tokens.length) {
				case 3:// tanant added
					tanant = tokens[2];
					outputData.add(date + "," + tanant + "," + source + "," + stream + "," + "tanant");
					break;
				case 5:// source added
					tanant = tokens[2];
					source = tokens[4];
					outputData.add(date + "," + tanant + "," + source + "," + stream + "," + "source");
					break;
				case 6:// stream added
					tanant = tokens[2];
					source = tokens[4];
					stream = tokens[5];
					outputData.add(date + "," + tanant + "," + source + "," + stream + "," + "stream");
					break;
				}
			}

		}
		logger.info(
				"inserting new sources addition event information. No of records " + outputData.size() + " records");
		hDFSConnectionManager.saveKpiToHDFS(newSourcesKpiPath + "/" + newSourcesKpiFile, outputData);
		logger.info(
				"Completed inserting new sources addition event information . Added " + outputData.size() + " records");
		System.out.println("inserting new sources addition event information " + outputData.size() + " records");
	}

	private String extractPartitionDate(String path) {
		Matcher matcher = Pattern.compile("-?\\d+").matcher(path);
		List<Integer> dates = new ArrayList<>();

		while (matcher.find()) {
			try {
				dates.add(Integer.parseInt(matcher.group()));
			} catch (NumberFormatException ex) {
				logger.error("invalid number for partion date" + path);
			}

		}
		String partitionedDate = null;

		switch (dates.size()) {
		case 3:
			partitionedDate = dates.get(0) + "-" + dates.get(1) + "-" + dates.get(2);
			break;
		case 4:
			partitionedDate = dates.get(0) + "-" + dates.get(1) + "-" + dates.get(2) + " " + dates.get(3) + ":" + 0
					+ ":" + 0;
		}
		return partitionedDate;
	}
}
