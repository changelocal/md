package com.md.union.front.api.config;


import com.arc.common.filter.ResponseWrapFilter;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;


@Import(com.arc.common.mvc.config.WebMvcConfiguration.class)
@Configuration
public class MdConfiguration implements WebMvcConfigurer, EnvironmentAware {
	private Environment environment;

	@org.springframework.context.annotation.Bean
	public RestTemplate restTemplate() {
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager
				.setMaxTotal(this.environment.getProperty("http.connection-manager.maxTotal", Integer.TYPE, 100));
		connectionManager.setDefaultMaxPerRoute(
				this.environment.getProperty("http.connection-manager.defaultMaxPerRoute", Integer.TYPE, 100));
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectionRequestTimeout(this.environment.getProperty("http.requestTimeout", Integer.TYPE, 20000))
				.setSocketTimeout(this.environment.getProperty("http.socketTimeout", Integer.TYPE, 60000))
				.setConnectTimeout(this.environment.getProperty("http.connectTimeout", Integer.TYPE, 50000)).build();
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager)
				.setDefaultRequestConfig(requestConfig).build();
		return new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
	}

	@org.springframework.context.annotation.Bean
	public FilterRegistrationBean<Filter> responseWrapFilter() {
		FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<Filter>();
		registration.setName("responseWrapFilter");
		registration.setFilter(new ResponseWrapFilter());
		registration.setOrder(2);
		registration.addUrlPatterns("/front/*");
		registration.addUrlPatterns("/web/*");
		return registration;
	}

	public Environment getEnvironment() {
		return environment;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	/*@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/swagger/**").addResourceLocations("classpath:/swagger/dist/");
	}*/

}
