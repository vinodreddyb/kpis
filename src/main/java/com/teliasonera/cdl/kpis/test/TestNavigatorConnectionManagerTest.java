package com.teliasonera.cdl.kpis.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class TestNavigatorConnectionManagerTest {

	public void connect() throws ClientProtocolException, IOException  {

		System.setProperty("socksProxyHost", "127.0.0.1");
		System.setProperty("socksProxyPort", "60000");
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope("131.116.126.254", 7187),
				new UsernamePasswordCredentials("scw409", "Vedam2016"));

		CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
		try {
			InetSocketAddress socksaddr = new InetSocketAddress("121.0.0.1", 60000);
			HttpClientContext context = HttpClientContext.create();
			context.setAttribute("socks.address", socksaddr);
			HttpHost target = new HttpHost("131.116.126.254", 7187, "http");
			HttpGet request = new HttpGet(
					"/api/v3/audits?query=service==hdfs;src==*fin*&startTime=1480800024000&endTime=1481800000000&offset=0&limit=50");

			System.out.println("Executing request " + request + " to " + target + " via SOCKS proxy " + socksaddr);
			CloseableHttpResponse response = httpclient.execute(target, request, context);
			try {
				System.out.println("----------------------------------------");
				System.out.println(response.getStatusLine());
				BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

				String output;
				System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					System.out.println(output);
				}

			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
	
	}
}