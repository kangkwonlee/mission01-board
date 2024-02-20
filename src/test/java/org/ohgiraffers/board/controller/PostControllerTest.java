package org.ohgiraffers.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ohgiraffers.board.domain.dto.CreatePostRequest;
import org.ohgiraffers.board.domain.dto.CreatePostResponse;
import org.ohgiraffers.board.domain.dto.ReadPostResponse;
import org.ohgiraffers.board.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/** 통합테스트 & 단위테스트
 * 통합테스트 :
 * 모듈을 통합하는 과정에서 모듈 간의 호환성을 확인하기 위해 수행되는 테스트
 * 통합테스트는 API의 모든 과정이 올바르게 동작하는지 확인하는 것
 *
 * 단위(유닛)테스트 :
 * 하나의 모듈을 기준으로 독립적으로 진행되는 가장 작은 단위의 테스트 (작은 단위 = 함수, 메소드)
 * 단위테스트는 어플리케이션을 구성하는 하나의 기능이 올바르게 동작하는지 독립적으로 테스트하는 것
 */

/** @WebMvcTest
 *  MVC를 위한 테스트, 컨트롤러가 예상대로 동작하는지 테스트하는데 사용된다.
 *  웹 어플리케이션을 어플리케이션 서버에 배포하지 않고 테스트용 MVC 환경을 만들어 요청 및 전송 응답기능을 제공해준다.
 */
@WebMvcTest(PostController.class)
public class PostControllerTest {

    /** @Autowired
     *  의존성 주입(DI)을 할 때, 사용하는 필요한 의존 객체의 타입이 해당하는 빈을 찾아 주입한다.
     *  의존성 : 하나의 코드가 다른 코드에 의존하는 상태를 뜻한다.
     */
    @Autowired
    MockMvc mockMvc;

    /** @MockBean
     * 스프링의 의존성 주입기능을 사용해 테스트 실행 시 실제 빈 대신에 Mockito로 생성된
     * 모의객체를 스프링 어플리케이션 컨텍스트에 추가한다.
     */
    @MockBean
    PostService postService;

    /** ObjectMapper
     *  특정 객체를 json 형태로 바꾸기위해 사용한다.
     */
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("게시글 작성 기능 테스트")
    void create_post_test() throws Exception {
        //given
        CreatePostRequest request = new CreatePostRequest("테스트 제목", "테스트 내용");
        CreatePostResponse response = new CreatePostResponse(1L, "테스트 제목", "테스트 내용");

        given(postService.createPost(any())).willReturn(response);

        //when & then
        mockMvc.perform(post("/api/v1/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(1L))
                .andExpect(jsonPath("$.title").value("테스트 제목"))
                .andExpect(jsonPath("$.content").value("테스트 내용"))
                .andDo(print());

    }

    @Test
    @DisplayName("게시글을 단일 조회하는 테스트")
    void read_post_test() throws Exception {

        //given
        Long postId = 1L;
        ReadPostResponse response = new ReadPostResponse(1L, "테스트 제목", "테스트 내용");

        given(postService.readPostById(any())).willReturn(response);

        //when & then
        mockMvc.perform(get("/api/v1/posts/{postId}", postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(1L))
                .andExpect(jsonPath("$.title").value("테스트 제목"))
                .andExpect(jsonPath("$.content").value("테스트 내용"))
                .andDo(print());
    }
}













