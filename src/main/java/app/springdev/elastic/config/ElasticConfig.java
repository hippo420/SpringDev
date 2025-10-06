package app.springdev.elastic.config;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableElasticsearchRepositories(basePackages = "app.springdev.elastic")
@Configuration
public class ElasticConfig {

    @Value("${spring.elasticsearch.uris}")
    private String uris;

    @Value("${spring.elasticsearch.username}")
    private String username;

    @Value("${spring.elasticsearch.password}")
    private String password;


    @Bean
    public ElasticsearchClient elasticsearchClient() {
        HashMap<String,String> map = parseProperties(this.uris);

        RestClient restClient = RestClient.builder(
                        new HttpHost(map.get("HOST"), Integer.parseInt(map.get("PORT")) , map.get("SCHEMA")))
                .build();

        return new ElasticsearchClient(new RestClientTransport(
                restClient, new JacksonJsonpMapper()));
    }

    private HashMap<String,String> parseProperties(String url){
        HashMap<String,String> map = new HashMap<>();
        if(url.contains("https://"))
            map.put("SCHEMA", "https");
        else
            map.put("SCHEMA", "http");
        url = url.replaceAll("http://","").replaceAll("https://","");
        String[] data = url.split(":");
        map.put("HOST",data[0]);
        map.put("PORT",data[1]);
        return map;
    }

}
