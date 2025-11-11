package com.devel.demo;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
		// timestamp 날짜 변환
		Instant instant = Instant.ofEpochMilli(System.currentTimeMillis());
		LocalDateTime previousTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		// 일짜 비교 
		System.out.println(previousTime.toLocalDate().equals(localDateTime.toLocalDate()));
		// 날짜 사이 시간 비교
		System.out.println(ChronoUnit.HOURS.between(localDateTime, previousTime));
		// 날짜 사이 일 비교
		System.out.println(ChronoUnit.DAYS.between(localDateTime.toLocalDate(), custom.toLocalDate()));
		// 현재 시각 timestamp
		System.out.println(System.currentTimeMillis());
		// 시간 표기 형태 변환 
		System.out.println(localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		// 시간 표기 형태 사용자 커스텀  
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
