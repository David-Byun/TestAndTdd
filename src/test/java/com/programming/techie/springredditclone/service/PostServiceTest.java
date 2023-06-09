package com.programming.techie.springredditclone.service;

import com.programming.techie.springredditclone.dto.PostRequest;
import com.programming.techie.springredditclone.dto.PostResponse;
import com.programming.techie.springredditclone.mapper.PostMapper;
import com.programming.techie.springredditclone.model.Post;
import com.programming.techie.springredditclone.model.Subreddit;
import com.programming.techie.springredditclone.model.User;
import com.programming.techie.springredditclone.repository.PostRepository;
import com.programming.techie.springredditclone.repository.SubredditRepository;
import com.programming.techie.springredditclone.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

//1. null로 테스트 하지만, inject로 주입된 것들 연동해서 테스트가 필요하다 >> mock 을 사용한다.
//2. mock을 사용할때는 처음에 객체로 환경을 만들고, 두번째로 when thenReturn을 통해서 시나리오를 입력한다.
//3. assertThat을 통해서 시나리오 대로 데이터 값이 나오는지 테스트 한다.
@ExtendWith(MockitoExtension.class)
@Slf4j
class PostServiceTest {

    @Value("${spring.datasource.password}")
    String password;

    @Mock
    private PostRepository postRepository;
    @Mock
    private SubredditRepository subredditRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthService authService;
    @Mock
    private PostMapper postMapper;

    private PostService postService;

    @Captor
    private ArgumentCaptor<Post> postArgumentCaptor;

    @BeforeEach
    public void setup() {
        postService = new PostService(postRepository, subredditRepository, userRepository, authService, postMapper);
    }

    @Test
    @DisplayName("Should Retrieve Post by Id")
    public void shouldFindPostsById() {

        //given

        Post post = new Post(123L, "First Post", "http://url.site", "Test", 0, null, Instant.now(), null);

        PostResponse expectedPostResponse = new PostResponse(123L, "First Post", "http://url.site", "Test",
                "Test User", "Test Subredit", 0, 0, "1 Hour Ago", false, false);

        //when : repository에서 조회한 아이디 값의 객체는 실제로 객체로 나온다.
        when(postRepository.findById(123L)).thenReturn(Optional.of(post));
        //Mockito.any()를 사용하여 모든 인수를 일치시키도록 정의된 동작 실행
        when(postMapper.mapToDto(Mockito.any(Post.class))).thenReturn(expectedPostResponse);
        //Mockito.any(Post.class) 부분에 실제 데이터가 들어가도 테스트가 진행되는지 ? OK

        //then
        PostResponse actualPostResponse = postService.getPost(123L);
        assertThat(actualPostResponse.getId()).isEqualTo(expectedPostResponse.getId());
        assertThat(actualPostResponse.getPostName()).isEqualTo(expectedPostResponse.getPostName());

    }

    //save void method이기 때문에 postRepository.save() method가 테스트로 인해 호출되었는지 안되었는지 확인
    @Test
    @DisplayName("Should Save Posts")
    void shouldSavePosts() {
        //given

        User currentUser = new User(123L, "test user", "secret password", "user@email.com", Instant.now(), true);
        Subreddit subreddit = new Subreddit(123L, "First Subreddit", "Subreddit Description", Collections.emptyList(), Instant.now(), currentUser);
        Post post = new Post(123L, "First Post", "http://url.site", "Test",
                0, null, Instant.now(), null);
        PostRequest postRequest = new PostRequest(null, "First Subreddit", "First Post", "http://url.site", "Test");

        Mockito.when(subredditRepository.findByName("First Subreddit"))
                .thenReturn(Optional.of(subreddit));
        Mockito.when(authService.getCurrentUser()).thenReturn(currentUser);
        Mockito.when(postMapper.map(postRequest, subreddit, currentUser))
                .thenReturn(post);


        //when
        postService.save(postRequest);

        //ArgumentMatchers.any()를 사용하면 메서드 호출시에 특정한 'Post' 객체에 의존하지 않고 어떤 'Post' 객체가 인자로 전달되어도 매칭되게 할 수 있음
        //ArgumentCaptor : postRepository.save() method의 arguments들을 capture해서 우리가 의도한 거랑 같은지 확인
        //then
        Mockito.verify(postRepository, Mockito.times(1)).save(postArgumentCaptor.capture());

        assertThat(postArgumentCaptor.getValue().getPostId()).isEqualTo(123L);
        assertThat(postArgumentCaptor.getValue().getPostName()).isEqualTo("First Post");

    }
}