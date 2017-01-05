package com.teliasonera.cdl.kpis.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceValues {
	private String dest;

	@JsonProperty(value = "delegation_token_id")
	private String delegationTokenId;
	private String permissions;
	private String src;
	
	@JsonProperty(value = "table_name")
	private String tableName;
	
	@JsonProperty(value = "resource_path")
	private String resourcePath;
	
	@JsonProperty(value ="database_name")
	private String dataBaseName;
	
	public String getResourcePath() {
		return resourcePath;
	}
	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}
	public String getDataBaseName() {
		return dataBaseName;
	}
	public void setDataBaseName(String dataBaseName) {
		this.dataBaseName = dataBaseName;
	}
	public String getDest() {
		return dest;
	}
	public void setDest(String dest) {
		this.dest = dest;
	}
	public String getDelegationTokenId() {
		return delegationTokenId;
	}
	public void setDelegationTokenId(String delegationTokenId) {
		this.delegationTokenId = delegationTokenId;
	}
	public String getPermissions() {
		return permissions;
	}
	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataBaseName == null) ? 0 : dataBaseName.hashCode());
		result = prime * result + ((delegationTokenId == null) ? 0 : delegationTokenId.hashCode());
		result = prime * result + ((dest == null) ? 0 : dest.hashCode());
		result = prime * result + ((permissions == null) ? 0 : permissions.hashCode());
		result = prime * result + ((resourcePath == null) ? 0 : resourcePath.hashCode());
		result = prime * result + ((src == null) ? 0 : src.hashCode());
		result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
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
		ServiceValues other = (ServiceValues) obj;
		if (dataBaseName == null) {
			if (other.dataBaseName != null)
				return false;
		} else if (!dataBaseName.equals(other.dataBaseName))
			return false;
		if (delegationTokenId == null) {
			if (other.delegationTokenId != null)
				return false;
		} else if (!delegationTokenId.equals(other.delegationTokenId))
			return false;
		if (dest == null) {
			if (other.dest != null)
				return false;
		} else if (!dest.equals(other.dest))
			return false;
		if (permissions == null) {
			if (other.permissions != null)
				return false;
		} else if (!permissions.equals(other.permissions))
			return false;
		if (resourcePath == null) {
			if (other.resourcePath != null)
				return false;
		} else if (!resourcePath.equals(other.resourcePath))
			return false;
		if (src == null) {
			if (other.src != null)
				return false;
		} else if (!src.equals(other.src))
			return false;
		if (tableName == null) {
			if (other.tableName != null)
				return false;
		} else if (!tableName.equals(other.tableName))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ServiceValues [dest=" + dest + ", delegationTokenId=" + delegationTokenId + ", permissions="
				+ permissions + ", src=" + src + ", tableName=" + tableName + ", resourcePath=" + resourcePath
				+ ", dataBaseName=" + dataBaseName + "]";
	}
	
	

	

}
