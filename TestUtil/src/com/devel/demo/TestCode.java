package com.devel.demo;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestCode {
	
	public static void main(String[] args) throws Exception {
		timeMethod();
		mapMethod();
	}
	
	private static void timeMethod() throws Exception {
		LocalDateTime localDateTime = LocalDateTime.now();
		
		Instant instant = Instant.ofEpochMilli(System.currentTimeMillis());
		
		System.out.println(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
		
		System.out.println(System.currentTimeMillis());
		
		System.out.println(localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		
		System.out.println(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:dd")));
	}
	
	private static void mapMethod() throws Exception {
		Map<String,Object> map = Map.of("inst", 123,
										"key2", "value2",
										"list", List.of("123",123,"abc"));
		
		map = new HashMap<String, Object>(map);
		
		System.out.println(map.toString());
	}
}
