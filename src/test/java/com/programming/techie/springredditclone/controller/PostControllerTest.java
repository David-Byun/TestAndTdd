package com.programming.techie.springredditclone.controller;

import com.programming.techie.springredditclone.dto.PostResponse;
import com.programming.techie.springredditclone.security.JwtProvider;
import com.programming.techie.springredditclone.service.PostService;
import com.programming.techie.springredditclone.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.http.ResponseEntity.status;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// WebMvcTest : Spring MVC components에서 작동
@WebMvcTest(controllers = PostController.class)
class PostControllerTest {

    @MockBean
    private PostService postService;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @MockBean
    private JwtProvider jwtProvider;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    @DisplayName("Should List All Posts When making GET request to endpoint")
    void shouldCreatePost() throws Exception {
        PostResponse postRequest1 = new PostResponse(1L, "Post Name", "http://url.site", "Description", "User 1",
                "Subreddit Name", 0, 0, "1 day ago", false, false);
        PostResponse postRequest2 = new PostResponse(2L, "Post Name 2", "http://url2.site2", "Description2", "User 2",
                "Subreddit Name 2", 0, 0, "2 days ago", false, false);
        List<PostResponse> postResponseList = new ArrayList<>();
        postResponseList.add(postRequest1);
        postResponseList.add(postRequest2);

        Mockito.when(postService.getAllPosts()).thenReturn(postResponseList);

        mockMvc.perform(get("/api/posts"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].postName").value("Post Name"))
                .andExpect(jsonPath("$[0].url").value("http://url.site"))
                .andExpect(jsonPath("$[1].url").value("http://url2.site2"))
                .andExpect(jsonPath("$[1].postName").value("Post Name 2"))
                .andExpect(jsonPath("$[1].id").value(2));

    }

}