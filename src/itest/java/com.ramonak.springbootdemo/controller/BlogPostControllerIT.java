package com.ramonak.springbootdemo.controller;

import com.google.gson.Gson;
import com.ramonak.springbootdemo.model.BlogPost;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BlogPostControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate rt;

      // 1. Is for <Map> type response
//    @Test // GET /api/v1/post
//    public void getAllPosts_whenInvoked_returnsAllPosts() throws JSONException {
//
//
//        // given
//
//        // when
//        //Map<Integer, List<String>> resp = rt.getForObject(createURLWithPort("/api/v1/post"), Map.class);
//        ResponseEntity<Map> response = rt.getForEntity("/api/v1/post", Map.class);
//
//        // then
//        // assertions on response status
//        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
//
//        // ... on response content type
//        assertThat(response.getHeaders().getContentType(), equalTo(MediaType.APPLICATION_JSON_UTF8));
//
//        // ... on response body value
//        String expected = "{1=[Mindaugas, Hello world blog post!], " +
//                "2=[Romas, Karbauskio dienoraštis], 3=[Bromas, Karbauskio dienoraštis]}";
//        assertThat(response.getBody().toString(), equalTo(expected));
//
//
//    }

    // 2. Is for <List> type response
    @Test // GET /api/v1/post
    public void getAllPosts_whenInvoked_returnsAllPosts() throws JSONException {
        // given

        // when
        ResponseEntity<List<BlogPost>> response = rt.exchange("/api/v1/post", HttpMethod.GET,
                null, new ParameterizedTypeReference<List<BlogPost>>() {});

        // then
        // assertions on response status
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        // ... on response content type
        assertThat(response.getHeaders().getContentType(), equalTo(MediaType.APPLICATION_JSON_UTF8));

        // ... on response body value
        String expected = "[" +
                "{\"id\":1,\"author\":\"Mindaugas\",\"post\":\"Post 1\"}," +
                "{\"id\":2,\"author\":\"Kazys\",\"post\":\"Post 2\"}," +
                "{\"id\":3,\"author\":\"Pranas\",\"post\":\"Post3\"}" +
                "]";
        assertThat(new Gson().toJson(response.getBody()), equalTo(expected));
    }

    @Test // POST /api/v1/post
    public void addPost_whenPostSubmittedWithValidPost_postCountIncreases() throws JSONException {
        // given
        HttpHeaders headers = new HttpHeaders(){{
            set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        }};

        MultiValueMap<String, String> parts = new LinkedMultiValueMap<String, String>() {{
            add("id", "22");
            add("author", "Audrobanis");
            add("post", "Kaip man patinka pyzza!");
        }};

        //when
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity(parts, headers);
        ResponseEntity<Map> response = rt.postForEntity("/api/v1/post", requestEntity, Map.class);

        // then
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        String expected = "{22=[Audrobanis, Kaip man patinka pyzza!], " +
                "1=[Mindaugas, Hello world blog post!], " +
                "2=[Romas, Karbauskio dienoraštis], " +
                "3=[Bromas, Karbauskio dienoraštis]}";
        assertThat(response.getBody().toString(), equalTo(expected));
    }

    @Test //DELETE
    public void deletePost_onePostIsDeleted_postCountDecreases() throws JSONException {
        // given
        Integer id = 3;

        // when
        ResponseEntity<Map> response = rt.exchange("/api/v1/post/" + id, HttpMethod.DELETE, null, Map.class);

        //then
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        String expected = "{1=[Mindaugas, Hello world blog post!], " +
                "2=[Romas, Karbauskio dienoraštis]}";
        assertThat(response.getBody().toString(), equalTo(expected));
    }
}