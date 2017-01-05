package com.teliasonera.cdl.kpis.service;

public interface DataUsageKPIService {
	public void extractAndStoreDataUsageEvents(long startTime, long endTime);
	public void extractVolumeSnapShot();
}
