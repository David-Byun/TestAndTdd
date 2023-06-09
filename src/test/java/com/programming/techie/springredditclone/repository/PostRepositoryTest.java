package com.programming.techie.springredditclone.repository;

import com.programming.techie.springredditclone.model.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostRepositoryTest extends BaseTest {

    @Autowired
    private PostRepository postRepository;

    /*
        Post 객체를 만들고 데이터베이스에 올바르게 저장되었는지 아닌지 확인해준다.
     */
    @Test
    void shouldSavePost() {
        Post expectedPostObject = new Post(null, "First Post", "http://url.site", "Test",
                0, null, Instant.now(), null);
        Post actualPostObject = postRepository.save(expectedPostObject);
        org.assertj.core.api.Assertions.assertThat(actualPostObject).usingRecursiveComparison().ignoringFields("postId").isEqualTo(expectedPostObject);
    }

}