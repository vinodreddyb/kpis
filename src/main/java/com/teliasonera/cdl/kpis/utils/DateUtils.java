package com.teliasonera.cdl.kpis.utils;

import java.util.Calendar;

public enum DateUtils {

	START_DATE, END_DATE;

	public void extractParttion() {
		
	}
	public long getDate() {
		switch (this) {
		case START_DATE:
			Calendar startDate = Calendar.getInstance();
			startDate.set(Calendar.HOUR_OF_DAY, startDate.getMinimum(Calendar.HOUR_OF_DAY));
			startDate.set(Calendar.MINUTE, startDate.getMinimum(Calendar.MINUTE));
			startDate.set(Calendar.SECOND, startDate.getMinimum(Calendar.SECOND));
			System.out.println(startDate.getTime());
			return startDate.getTimeInMillis();
		case END_DATE:
			Calendar endDate = Calendar.getInstance();
			endDate.set(Calendar.HOUR_OF_DAY, endDate.getMaximum(Calendar.HOUR_OF_DAY));
			endDate.set(Calendar.MINUTE, endDate.getMaximum(Calendar.MINUTE));
			endDate.set(Calendar.SECOND, endDate.getMaximum(Calendar.SECOND));
			System.out.println(endDate.getTime());
			return endDate.getTimeInMillis();
		default:
			throw new AssertionError("Unknown operations " + this);
		}
	}
}
