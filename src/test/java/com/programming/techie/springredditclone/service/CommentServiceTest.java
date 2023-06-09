package com.programming.techie.springredditclone.service;

import com.programming.techie.springredditclone.exceptions.SpringRedditException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class CommentServiceTest {


    @Test
    @DisplayName("Test should Pass When Comment do not Contains Swear Words")
    void shouldNotContainSwarWordsInsideComment() {
        CommentService commentService = new CommentService(null, null, null, null, null, null, null);
//        Assertions.assertFalse(commentService.containsSwearWords("This is a clean comment"));
        assertThat(commentService.containsSwearWords("This is a comment")).isFalse();
    }

    @Test
    @DisplayName("Should Throw Exception when Exception Contains Swear Words")
    public void shouldFailWhenCommentContainsSwearWords() {
        CommentService commentService = new CommentService(null, null, null, null, null, null, null);
        assertThatThrownBy(() -> {
            commentService.containsSwearWords("This is a shitty comment");
        }).isInstanceOf(SpringRedditException.class).hasMessage("Comments contains unacceptable language");
    }
}