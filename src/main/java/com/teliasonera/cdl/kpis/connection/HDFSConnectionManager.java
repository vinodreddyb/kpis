package com.teliasonera.cdl.kpis.connection;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.security.UserGroupInformation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HDFSConnectionManager {
	final Configuration conf = new Configuration();

	@Value("${hdfs.username}")
	private String username;

	@PostConstruct
	public void init() {
		conf.set("fs.hdfs.impl", DistributedFileSystem.class.getName());
		conf.set("fs.file.impl", LocalFileSystem.class.getName());

	}

	public void saveKpiToHDFS(final String hdfsPath, final List<String> data) {

		/*
		 * final Configuration conf = new Configuration();
		 * conf.set("fs.hdfs.impl", DistributedFileSystem.class.getName());
		 * conf.set("fs.file.impl", LocalFileSystem.class.getName());
		 */
		try {
			UserGroupInformation.setConfiguration(conf);
			UserGroupInformation proxyUser = UserGroupInformation.getCurrentUser();
			UserGroupInformation ugi = UserGroupInformation.createProxyUser(username, proxyUser);

			ugi.doAs(new PrivilegedExceptionAction<Void>() {
				public Void run() throws Exception {
					BufferedWriter writer = null;
					try {
						FileSystem fs = FileSystem.get(conf);
						System.out.println("Path: " + hdfsPath);
						Path path = new Path(hdfsPath);
						boolean newFile = false;
						if (!fs.exists(path)) {
							newFile = true;
							fs.createNewFile(path);
						}
						FSDataOutputStream fsout = fs.append(path);
						writer = new BufferedWriter(new OutputStreamWriter(fsout));
						/*if(!newFile) {
							writer.newLine();
						}*/
						for (String line : data) {
							writer.append(line);
							writer.newLine();
						}
						
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						writer.close();
					}
					return null;
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void listPaths(final String hdfsPath) {

		/*
		 * final Configuration conf = new Configuration();
		 * conf.set("fs.hdfs.impl", DistributedFileSystem.class.getName());
		 * conf.set("fs.file.impl", LocalFileSystem.class.getName());
		 */
		try {
			UserGroupInformation.setConfiguration(conf);
			UserGroupInformation proxyUser = UserGroupInformation.getCurrentUser();
			UserGroupInformation ugi = UserGroupInformation.createProxyUser(username, proxyUser);

			ugi.doAs(new PrivilegedExceptionAction<Void>() {
				public Void run() throws Exception {

					FileSystem fs = FileSystem.get(conf);
					FileStatus[] status = fs.listStatus(new Path(hdfsPath));
					for (int i = 0; i < status.length; i++) {
						FileStatus fileStatus = status[i];
						System.out.println("Source: " + fileStatus.getPath().getName());
						if (fileStatus.isDirectory()) {
							FileStatus[] subStatus = fs.listStatus(fileStatus.getPath());
							for (FileStatus st : subStatus) {
								System.out.println(st.getPath().getName());
							}

						} else {
							System.out.println(fileStatus.getPath());
						}
					}
					return null;
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public Map<String, Double> getSpaceBySourceAndStream() {
		final Map<String, Double> result = new TreeMap<>();
		try {
			UserGroupInformation.setConfiguration(conf);
			UserGroupInformation proxyUser = UserGroupInformation.getCurrentUser();
			UserGroupInformation ugi = UserGroupInformation.createProxyUser(username, proxyUser);

			ugi.doAs(new PrivilegedExceptionAction<Void>() {
				public Void run() throws Exception {

					FileSystem fs = FileSystem.get(conf);
					
					getAllFilePath(new Path("/data/prod"),fs, result);
					return null;
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	private void getAllFilePath(Path filePath, FileSystem fs, Map<String, Double> spaceMap)
			throws FileNotFoundException, IOException {
		//Map<String, Double> spaceMap = new HashMap<>();
		FileStatus[] fileStatus = fs.listStatus(filePath);
        for (FileStatus fileStat : fileStatus) {
			Path path = fileStat.getPath();
			String name = path.getName();
			
			
			if("raw".equals(name) || "archive".equals(name) || "sandboxes".equals(name)) {
					continue;
			}
			if (fileStat.isDirectory() && !name.startsWith(".") && !name.startsWith("_") && !name.contains("temp")) {
				Matcher matcher = Pattern.compile("\\d+").matcher(name);
				if (matcher.find()) {
					Path streamPath = path.getParent();
					String stream = streamPath.toString();
					String[] tokens = stream.substring(stream.indexOf("data")).split("/");
					String tanant = tokens[2] + ":" + tokens[3];
					
					StringBuilder build = new StringBuilder();
					for (int i = 4; i < tokens.length; i++) {
						build.append(tokens[i]);
						if (i != tokens.length - 1)
							build.append(":");
					}
					String key = (tanant + ":" + build.toString()).trim();
					if (!spaceMap.containsKey(key)) {
						double size = (((fs.getContentSummary(streamPath).getLength() / 1024.0) / 1024.0) / 1024.0);
						spaceMap.put(key, size);
					}

				} else {
					getAllFilePath(fileStat.getPath(), fs, spaceMap);
				}

			}
		}
	}
		

	public Map<String, Double> getSpaceOccupation(final Set<String> hdfsPaths) {
		/*
		 * final Configuration conf = new Configuration();
		 * conf.set("fs.hdfs.impl", DistributedFileSystem.class.getName());
		 * conf.set("fs.file.impl", LocalFileSystem.class.getName());
		 */
		final Map<String, Double> sizes = new HashMap<>();
		
		try {
			UserGroupInformation.setConfiguration(conf);
			UserGroupInformation proxyUser = UserGroupInformation.getCurrentUser();
			UserGroupInformation ugi = UserGroupInformation.createProxyUser(username, proxyUser);

			ugi.doAs(new PrivilegedExceptionAction<Void>() {
				public Void run() throws Exception {

					FileSystem fs = FileSystem.get(conf);

					for (String hdfsPath : hdfsPaths) {
						sizes.put(hdfsPath,(((fs.getContentSummary(new Path(hdfsPath)).getLength()/1024.0)/1024.0)/1024.0) );
					}
					return null;
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

		return sizes;

	}
}
