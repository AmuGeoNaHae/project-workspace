package com.team.webproject.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.webproject.common.DateChange;
import com.team.webproject.dto.GenerationDiscountDTO;
import com.team.webproject.dto.PerformanceDTO;
import com.team.webproject.mapper.GenerationDiscountMapper;
import com.team.webproject.mapper.PerformanceMapper;

@Service
public class DetailServiceimpl implements DetailService {

	@Autowired
	PerformanceMapper performanceMapper;

	@Autowired
	GenerationDiscountMapper generationDiscountMapper;

	// DB에서 할인률 조회
	public List<GenerationDiscountDTO> getDisCount() {
		return generationDiscountMapper.getDisCount();
	}

	// DB에서 공연 1개 조회
	public PerformanceDTO getPerformance(String performance_code) {
		return performanceMapper.getPerformance(performance_code);
	}

	// 전시 외 - 타임테이블 map으로 리턴
	@Override
	public Map<String, List<String>> getTimeTable(Object schedule) {
		// JSON으로 정리할 데이터를 담을 Map
		Map<String, List<String>> scheduleMap = new HashMap<>();

		String[] schedules = schedule.toString().split("\\),");

		for (String s : schedules) {
			String[] dayTime = s.split("\\(|\\)");
			if (dayTime.length < 2) {
				continue;
			} else {
				String[] days = expandDayRange(dayTime[0].trim());
				String time = dayTime[1];

				for (String day : days) {

					if (day != null) {
						scheduleMap.computeIfAbsent(day, k -> new ArrayList<>()).add(time);	
					}                	
				}
			}            
		}

		return scheduleMap;
	}

	// 날짜를 숫자로 변환
	@Override
	public String[] expandDayRange(String dayRange) {
		String[] weekdays = {"1", "2", "3", "4", "5", "6", "0"};

		// 축약형과 전체 요일명을 매핑하는 맵 생성
		Map<String, String> dayMap = new HashMap<>();
		dayMap.put("월", "1");
		dayMap.put("화", "2");
		dayMap.put("수", "3");
		dayMap.put("목", "4");
		dayMap.put("금", "5");
		dayMap.put("토", "6");
		dayMap.put("일", "0");

		// 요일 범위를 나타내는 정규식
		Pattern pattern = Pattern.compile("(월|화|수|목|금|토|일)요일 ~ (월|화|수|목|금|토|일)요일");
		Matcher matcher = pattern.matcher(dayRange);

		if (matcher.find()) {
			String startDayAbbr = matcher.group(1); // 축약형 요일
			String endDayAbbr = matcher.group(2); // 축약형 요일

			// 축약형을 전체 요일명으로 변환
			String startDay = dayMap.get(startDayAbbr);
			String endDay = dayMap.get(endDayAbbr);

			int startIndex = Arrays.asList(weekdays).indexOf(startDay);
			int endIndex = Arrays.asList(weekdays).indexOf(endDay);
			int rangeLength = (endIndex - startIndex) + 1;

			// 기존 days 배열을 확장하기 위한 새로운 배열 생성
			String[] expandedDays = new String[rangeLength];
			for (int i = 0; i < rangeLength; i++) {
				expandedDays[i] = weekdays[startIndex + i];
			}

			return expandedDays;
		} else {
			// "~" 기호가 없을 경우 단일 요일로 간주
			String dayAbbr = dayRange.substring(0, 1);
			String day = dayMap.get(dayAbbr);
			return new String[]{day};
		}
	}

	// KOPIS 요청
	@Override
	public JSONObject getKopisInfo(String performance_code) {
		HashMap<String, Object> result = new HashMap<String, Object>();

		String jsonInString = "";
		JSONObject jsonob = null;

		try {

			HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
			factory.setConnectTimeout(5000); //타임아웃 설정 5초
			factory.setReadTimeout(5000);//타임아웃 설정 5초
			RestTemplate restTemplate = new RestTemplate(factory);

			HttpHeaders header = new HttpHeaders();
			HttpEntity<?> entity = new HttpEntity<>(header);

			String url = "http://www.kopis.or.kr/openApi/restful/pblprfr/"+performance_code;

			UriComponents uri = UriComponentsBuilder.fromHttpUrl(url+"?"+"service=c7e9ff4fa5dd433aac399c3804a60abb").build();

			//이 한줄의 코드로 API를 호출해 MAP타입으로 전달 받는다.
			ResponseEntity<Map> resultMap = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, Map.class);
			result.put("statusCode", resultMap.getStatusCodeValue()); //http status code를 확인
			result.put("header", resultMap.getHeaders()); //헤더 정보 확인
			result.put("body", resultMap.getBody()); //실제 데이터 정보 확인

			//데이터를 제대로 전달 받았는지 확인 string형태로 파싱해줌
			ObjectMapper mapper = new ObjectMapper();
			System.out.println(mapper.writeValueAsString(resultMap.getBody().get("db")));
			jsonInString = mapper.writeValueAsString(resultMap.getBody().get("db"));
			JSONParser parser = new JSONParser();
			jsonob = (JSONObject) parser.parse(jsonInString);

		} catch (HttpClientErrorException | HttpServerErrorException e) {
			result.put("statusCode", e.getRawStatusCode());
			result.put("body"  , e.getStatusText());
			System.out.println(e.toString());

		} catch (Exception e) {
			result.put("statusCode", "999");
			result.put("body"  , "excpetion오류");
			System.out.println(e.toString()); 
		}		

		return jsonob;
	}

	// 공공데이터 요청
//	public void getPublicDataInfo(String performance_code) {
//		
//		long start = System.currentTimeMillis();
//		LocalDate date = LocalDate.now();
//		LocalDate plusdate = date.plusMonths(1);
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//
//		StringBuilder urlBuilder = new StringBuilder("http://www.culture.go.kr/openapi/rest/publicperformancedisplays/d/"); /*URL*/
//		
//		try {
//
//			urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=cXG%2BsVlagSV2%2FrTreOPObTV1p66Hho1fOgZi0uxSNS3GGBq7xLhMe9uPRSf3u4Ya%2BoyDgW4evwP42PU18PTy0g%3D%3D"); /*Service Key*/
//            urlBuilder.append("&" + URLEncoder.encode("ComMsgHeader","UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /**/
//            urlBuilder.append("&" + URLEncoder.encode("RequestTime","UTF-8") + "=" + URLEncoder.encode("20100810:23003422", "UTF-8")); /*Optional 필드*/
//            urlBuilder.append("&" + URLEncoder.encode("CallBackURI","UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /*Optional 필드*/
//            urlBuilder.append("&" + URLEncoder.encode("MsgBody","UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /**/
//            urlBuilder.append("&" + URLEncoder.encode("seq","UTF-8") + "=" + URLEncoder.encode(performance_code, "UTF-8")); /* 시퀀스로 공연 조회 */
//            
//			URL url = new URL(urlBuilder.toString());
//			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//			conn.setRequestMethod("GET");
//			conn.setRequestProperty("Content-type", "application/json");
//			System.out.println("Response code: " + conn.getResponseCode());
//			
//			BufferedReader rd;
//			if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
//				rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//			} else {
//				rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//			}
//			StringBuilder sb = new StringBuilder();
//			String line;
//			while ((line = rd.readLine()) != null) {
//				sb.append(line);
//			}
//			rd.close();
//			conn.disconnect();
//			System.out.println(sb.toString());
//			
//			XmlMapper xml = new XmlMapper();
//	        Object dataInstance = xml.readValue(sb.toString().getBytes(), Object.class);
//
//	        
//			// model.addAttribute("age", jsonob.get("prfage")); - 연령
//
//            
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		} 
//
//	}

}
