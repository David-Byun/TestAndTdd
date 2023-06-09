package com.programming.techie.springredditclone.repository;

import com.programming.techie.springredditclone.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTestEmbedded {

    @Autowired
    private UserRepository userRepository;

    /*
        Recursive Comparison(재귀적 비교) : 객체 내부에 또 다른 객체들이 포함되어 있는 경우, 객체들을 깊이 내려가면서 각각의 필드를 비교하는 방식
        usingRecursiveComparison()은 객체를 재귀적으로 비교하기 위한 설정을 활성화 한다
        ignoringFields("userId")는 'userId' 필드를 비교에서 제외하도록 설정한다. 'userId'의 값은 비교하지 않는다.
        userId : auto-increment 이기 때문에,
     */
    @Test
    void shouldSaveUser() {
        User user = new User(null, "test user", "secret password", "user@email.com", Instant.now(), true);
        User savedUser = userRepository.save(user);
        assertThat(savedUser).usingRecursiveComparison().ignoringFields("userId").isEqualTo(user);
    }

    @Test
    @Sql("classpath:test-data.sql")
    void shouldSaveUsersThroughSqlFile() {
        Optional<User> test = userRepository.findByUsername("testuser_sql");
        assertThat(test).isNotEmpty();
    }
}