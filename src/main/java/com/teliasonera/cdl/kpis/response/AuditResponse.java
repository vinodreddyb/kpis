package com.teliasonera.cdl.kpis.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuditResponse {

	private Date timestamp;
	private String service;
	private String username;
	private String impersonator;
	private String ipAddress;
	private String command;
	private String resource;
	private String allowed;
	private ServiceValues serviceValues;
	 
	
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getImpersonator() {
		return impersonator;
	}
	public void setImpersonator(String impersonator) {
		this.impersonator = impersonator;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	public String getAllowed() {
		return allowed;
	}
	public void setAllowed(String allowed) {
		this.allowed = allowed;
	}
	public ServiceValues getServiceValues() {
		return serviceValues;
	}
	public void setServiceValues(ServiceValues serviceValues) {
		this.serviceValues = serviceValues;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((allowed == null) ? 0 : allowed.hashCode());
		result = prime * result + ((command == null) ? 0 : command.hashCode());
		result = prime * result + ((impersonator == null) ? 0 : impersonator.hashCode());
		result = prime * result + ((ipAddress == null) ? 0 : ipAddress.hashCode());
		result = prime * result + ((resource == null) ? 0 : resource.hashCode());
		result = prime * result + ((service == null) ? 0 : service.hashCode());
		result = prime * result + ((serviceValues == null) ? 0 : serviceValues.hashCode());
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
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
		AuditResponse other = (AuditResponse) obj;
		if (allowed == null) {
			if (other.allowed != null)
				return false;
		} else if (!allowed.equals(other.allowed))
			return false;
		if (command == null) {
			if (other.command != null)
				return false;
		} else if (!command.equals(other.command))
			return false;
		if (impersonator == null) {
			if (other.impersonator != null)
				return false;
		} else if (!impersonator.equals(other.impersonator))
			return false;
		if (ipAddress == null) {
			if (other.ipAddress != null)
				return false;
		} else if (!ipAddress.equals(other.ipAddress))
			return false;
		if (resource == null) {
			if (other.resource != null)
				return false;
		} else if (!resource.equals(other.resource))
			return false;
		if (service == null) {
			if (other.service != null)
				return false;
		} else if (!service.equals(other.service))
			return false;
		if (serviceValues == null) {
			if (other.serviceValues != null)
				return false;
		} else if (!serviceValues.equals(other.serviceValues))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
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
		return "AuditResponse [timestamp=" + timestamp + ", service=" + service + ", username=" + username
				+ ", impersonator=" + impersonator + ", ipAddress=" + ipAddress + ", command=" + command + ", resource="
				+ resource + ", allowed=" + allowed + ", serviceValues=" + serviceValues + "]";
	}
	
	

}
