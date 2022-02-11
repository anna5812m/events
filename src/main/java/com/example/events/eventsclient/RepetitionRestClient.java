package com.example.events.eventsclient;

import com.example.events.model.Repetition;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Service
public class RepetitionRestClient {
    private RestTemplate restTemplate;
    private RepetitionRestClientProperties properties;

    public RepetitionRestClient(
            RepetitionRestClientProperties properties) {
        this.restTemplate = new RestTemplate();
        this.restTemplate.setErrorHandler(
                new RepetitionErrorHandler());
        this.properties = properties;
    }

    public Iterable<Repetition> findAll() throws URISyntaxException {
        RequestEntity<Iterable<Repetition>> requestEntity = new RequestEntity
                <Iterable<Repetition>>(HttpMethod.GET, new URI(properties.getUrl() +
                properties.getBasePath()));
        ResponseEntity<Iterable<Repetition>> response =
                restTemplate.exchange(requestEntity, new
                        ParameterizedTypeReference<Iterable<Repetition>>() {
                        });
        if(response.getStatusCode() == HttpStatus.OK){
            return response.getBody();
        }
        return null;
    }

    public Repetition findById(String id){
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return restTemplate.getForObject(properties.getUrl() +
                properties.getBasePath() + "/{id}",Repetition.class,params);
    }

    public Repetition upsert(Repetition repetition) throws URISyntaxException {
        RequestEntity<?> requestEntity = new RequestEntity<>(repetition, HttpMethod.
                POST, new URI(properties.getUrl() + properties.getBasePath()));
        ResponseEntity<?> response = restTemplate.exchange(requestEntity, new
                ParameterizedTypeReference<Repetition>() {});
        if(response.getStatusCode() == HttpStatus.CREATED){
            return restTemplate.getForObject(response.getHeaders().
                    getLocation(),Repetition.class);
        }
        return null;
    }

    public Repetition setCompleted(String id) throws URISyntaxException{
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        restTemplate.postForObject(properties.getUrl() + properties.getBasePath() + "/{id}?_method=patch",null,ResponseEntity.class, params);
        return findById(id);
    }
    public void delete(String id){
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        restTemplate.delete(properties.getUrl() + properties.getBasePath() +
                "/{id}",params);
    }
}

