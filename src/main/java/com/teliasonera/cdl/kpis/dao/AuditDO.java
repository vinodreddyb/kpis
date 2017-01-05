package com.teliasonera.cdl.kpis.dao;

public class AuditDO {
	private String date; 
	private String username; 
	private String sourcename;
	private String stream;
	private String tanant;
	private String databasename;
	private String tabelname;
	private double size;
	private String resourcePath;
	private String partition;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSourcename() {
		return sourcename;
	}
	public void setSourcename(String sourcename) {
		this.sourcename = sourcename;
	}
	public String getStream() {
		return stream;
	}
	public void setStream(String stream) {
		this.stream = stream;
	}
	public String getTanant() {
		return tanant;
	}
	public void setTanant(String tanant) {
		this.tanant = tanant;
	}
	public String getDatabasename() {
		return databasename;
	}
	public void setDatabasename(String databasename) {
		this.databasename = databasename;
	}
	public String getTabelname() {
		return tabelname;
	}
	public void setTabelname(String tabelname) {
		this.tabelname = tabelname;
	}

	public double getSize() {
		return size;
	}
	public void setSize(double size) {
		this.size = size;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((databasename == null) ? 0 : databasename.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((partition == null) ? 0 : partition.hashCode());
		result = prime * result + ((resourcePath == null) ? 0 : resourcePath.hashCode());
		long temp;
		temp = Double.doubleToLongBits(size);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((sourcename == null) ? 0 : sourcename.hashCode());
		result = prime * result + ((stream == null) ? 0 : stream.hashCode());
		result = prime * result + ((tabelname == null) ? 0 : tabelname.hashCode());
		result = prime * result + ((tanant == null) ? 0 : tanant.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuditDO other = (AuditDO) obj;
		if (databasename == null) {
			if (other.databasename != null)
				return false;
		} else if (!databasename.equals(other.databasename))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (partition == null) {
			if (other.partition != null)
				return false;
		} else if (!partition.equals(other.partition))
			return false;
		if (resourcePath == null) {
			if (other.resourcePath != null)
				return false;
		} else if (!resourcePath.equals(other.resourcePath))
			return false;
		if (Double.doubleToLongBits(size) != Double.doubleToLongBits(other.size))
			return false;
		if (sourcename == null) {
			if (other.sourcename != null)
				return false;
		} else if (!sourcename.equals(other.sourcename))
			return false;
		if (stream == null) {
			if (other.stream != null)
				return false;
		} else if (!stream.equals(other.stream))
			return false;
		if (tabelname == null) {
			if (other.tabelname != null)
				return false;
		} else if (!tabelname.equals(other.tabelname))
			return false;
		if (tanant == null) {
			if (other.tanant != null)
				return false;
		} else if (!tanant.equals(other.tanant))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return date + ", " + username + ", " + sourcename + ", " + stream + ", " + tanant + ", " + databasename + ", "
				+ tabelname + "," + partition + ", \"" + resourcePath + "\"," + size;
	}
	public String getResourcePath() {
		return resourcePath;
	}
	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}
	public String getPartition() {
		return partition;
	}
	public void setPartition(String partition) {
		this.partition = partition;
	}
	
	
	
}
