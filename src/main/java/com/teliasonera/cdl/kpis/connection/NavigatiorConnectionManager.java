package com.teliasonera.cdl.kpis.connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teliasonera.cdl.kpis.response.AuditResponse;

@Component
public class NavigatiorConnectionManager {

	final static Logger logger = LoggerFactory.getLogger(NavigatiorConnectionManager.class);
	@Autowired
	private RestTemplate restTemplate;

	public List<AuditResponse> executeAudit(String url) {
		List<AuditResponse> response = new ArrayList<>();
		ResponseEntity<String> responseEntitty = null;
		try {
			responseEntitty = restTemplate.exchange(url, HttpMethod.GET, null,
					String.class);
			System.out.println(url);
			if(HttpStatus.OK == responseEntitty.getStatusCode()) {
				String res = responseEntitty.getBody();
				ObjectMapper mapper = new ObjectMapper();
				response = mapper.readValue(res, new TypeReference<List<AuditResponse>>(){});
			}
		} catch (RestClientException e) {
			logger.error("Exception occured while executing query at navigator:" + e);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response;

	}

}
