package com.teliasonera.cdl.kpis.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.teliasonera.cdl.kpis.utils.DateUtils;

public class Test {

	public static void mainttt(String[] args) {
		String test ="t_cem_lte_throughput_raw";
		
		String tests = "/data/prod/swe/base/cem/mud_mms_subs/ing_year=2017/ing_month=1/ing_day=2/ing_hour=12";
		String test2 = "/data/prod/swe/base/ime/cdfn/gprs/schdate_year=2016";
		String test3 ="/data/prod/grp/base/fenix/geo_location/gsm/2016/09/30";
		
		String s = new String("2016");
		/*Matcher matcher = Pattern.compile("\\d+").matcher(tests);
		if(matcher.find()) {
			System.out.println("fffff");
		}*/
		
		/*String midle = tests.substring(0,tests.indexOf(matcher.group() + "/"));
		System.out.println(midle.substring(0,midle.lastIndexOf("/")));
		*/
		
		/*
		 *  Extract stream path. 
		 *  Find the partition digits like year 2016. example /data/prod/swe/abts/ime/cdfn/gprs/schdate_year=2016/schdate_month=10
		 *  Get the index of first occurrence of the digits + / that means 2016/
		 *  Substring /data/prod/swe/abts/ime/cdfn/gprs/schdate_year=
		 *  Get last index of char '/' in /data/prod/swe/abts/ime/cdfn/gprs/schdate_year=
		 *  Substring /data/prod/swe/abts/ime/cdfn/gprs/  this is stream
		 
		Matcher matcher = Pattern.compile("\\d+").matcher(resourcePath);
		if(matcher.find()) {
			String subPath = resourcePath.substring(0,resourcePath.indexOf(matcher.group()));
			streamPaths.add(subPath.substring(0,subPath.lastIndexOf("/")));
		}*/
		
		Matcher matcher = Pattern.compile("\\d+").matcher(tests);
		if(matcher.find()) {
			String subPath = tests.substring(0,tests.indexOf(matcher.group()));
			System.out.println(subPath.substring(0,subPath.lastIndexOf("/")));
		}
	
		/*while (matcher.find()) {
			  System.out.println(matcher.group());
	     }*/
		
	}

}
