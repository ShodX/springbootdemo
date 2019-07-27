package com.ramonak.springbootdemo.controller;

import com.ramonak.springbootdemo.model.BlogPost;
import com.ramonak.springbootdemo.repository.BlogPostInMemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.*;

@RestController // = @Controller + @ResponseBody
@RequestMapping("/api/v1")
public class BlogPostController {

    Map<String, List<String>> posts = new HashMap<String, List<String>>(){{
        put("1", Arrays.asList("Mindaugas", "Hello world blog post!"));
        put("2", Arrays.asList("Romas", "Karbauskio dienoraštis"));
        put("3", Arrays.asList("Bromas", "Karbauskio dienoraštis"));
//        put("4", Arrays.asList("Mindaugas", "Something blog post!"));
//        put("5", Arrays.asList("Mindaugas", "Something else blog post!"));
    }};


    @Autowired
    BlogPostInMemRepository bpr;

    // get all posts
    @RequestMapping(method = RequestMethod.GET, path="/post")
    public @ResponseBody List<BlogPost> getAllPosts() {
        return bpr.getAllPosts();
    }

//    // get all posts
//    @RequestMapping(method = RequestMethod.GET, path="/post") // ~= @GetMapping, but it only works w/ methods
//    public Map getAllPosts() {
//        return posts;
//    }

    //get a post by ID
    @RequestMapping(method = RequestMethod.GET, path = "/post/{id}") // {id} -> parameter
    public List<String> getById(@PathVariable String id) {
        return posts.get(id);
    }

    // get single post
    @RequestMapping(method = RequestMethod.GET, path="/single_post")
    public Map getPost(@RequestParam String id) {
        System.err.println("getPost /single_post hit");
        return new HashMap<String, List<String>>(){{
            put(id, posts.get(id)); }};
    }

    // delete a post
    @RequestMapping(method = RequestMethod.DELETE, path="/post/{id}")
    public Map deletePost(@PathVariable Integer id) {
        posts.remove(id.toString());
        return posts;
    }

    // add post
    @RequestMapping(method = RequestMethod.POST, path="/post",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Map addPost(@RequestParam("id") Integer id,
                       @RequestParam("author") String author,
                       @RequestParam("post") String post) {
        posts.put(id.toString(), Arrays.asList(author, post));
        return posts;
    }

    // update post
    // Note the usage of @PathVariable instead of @RequestParam.
    // In reality you would use either one or the other, this is just for illustration that they can both be used
    @RequestMapping(method = RequestMethod.PUT, path="/post/{id}",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Map updatePost(@PathVariable Integer id,
                          @RequestParam("author") String author,
                          @RequestParam("post") String post) {
        posts.put(id.toString(), Arrays.asList(author, post));
        return posts;
    }

//    @RequestMapping(method = RequestMethod.GET, path = "/post/by/author/{authorName}")
//    public Map getByAuthor(@PathVariable String authorName) {
//        Map<List<String>> resultSearch = null;
//        for (List<String> entry : posts.values()) {
//            if (entry.contains(authorName)) {
//                resultSearch.add(entry);
//            }
//        }
//        return resultSearch;
//    }

}
