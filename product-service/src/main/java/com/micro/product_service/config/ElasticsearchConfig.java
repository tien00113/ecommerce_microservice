// package com.micro.product_service.config;

// import org.elasticsearch.client.RestHighLevelClient;
// import org.apache.http.HttpHost;
// import org.elasticsearch.client.RestClient;
// import org.elasticsearch.client.RestClientBuilder;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.data.elasticsearch.client.erhlc.RestClients;
// import org.springframework.data.elasticsearch.client.ClientConfiguration;
// import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
// import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

// import co.elastic.clients.elasticsearch.ElasticsearchClient;
// import co.elastic.clients.json.jackson.JacksonJsonpMapper;
// import co.elastic.clients.transport.ElasticsearchTransport;
// import co.elastic.clients.transport.rest_client.RestClientTransport;

// @Configuration
// @EnableElasticsearchRepositories(basePackages = "com.micro.product_service.repository")
// public class ElasticsearchConfig {

//     @Bean
//     public RestClient restClient = RestClient
//             .builder(HttpHost.create("http://localhost:9200"))
//             .build();
//     ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
//     ElasticsearchClient client = new ElasticsearchClient(transport);
// }
