package com.ramonak.springbootdemo.repository;

import com.ramonak.springbootdemo.model.BlogPost;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BlogPostInMemRepository {

    List<BlogPost> posts = new ArrayList<BlogPost>(){{
        add(new BlogPost(1, "Mindaugas", "Post 1"));
        add(new BlogPost(2, "Kazys", "Post 2"));
        BlogPost bp3 = new BlogPost();
        bp3.setId(3);
        bp3.setAuthor("Pranas");
        bp3.setPost("Post3");
        add(bp3);
    }};

    public List<BlogPost> getAllPosts(){
        return posts;
    }
}
