package com.jojoldu.book.springboot.web;

import com.jojoldu.book.springboot.config.auth.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@RunWith(SpringRunner.class) // JUnit4 버전
@ExtendWith(SpringExtension.class) //JUnit5 버전
@WebMvcTest(controllers = HelloController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = SecurityConfig.class)})
public class HelloControllerTest {

    @Autowired //스프링 빈(Bean)을 주입
    private MockMvc mvc; //개발한 웹 프로그램을 실제 서버에 배포하지 않고도 테스트를 위한 요청을 제공 즉, 웹 API 테스트 할 때 사용, 이 클래스를 통해 HTTP GET, POST등에 대한 API테스트

    @WithMockUser(roles = "USER")
    @Test //Test할 메소드
    public void hello가_리턴된다() throws Exception {
        String hello = "hello";

        mvc.perform(get("/hello")) //MockMvc를 통해 /hello 주소로 HTTP GET 요청, 체이닝이 지원되어 아래와 같이 여러 검증 기능을 이어서 선언
                .andExpect(status().isOk()) //mvc.perform의 결과를 검증, HTTP Header의 Status를 검증, 우리가 흔히 알고 있는 200, 404, 500등의 상태를 검증
                .andExpect(content().string(hello)); //mvc.perform의 결과를 검증, 응답 본문의 내용을 검증, Controller에서 "hello"를 리턴하기 때문에 이 값이 맞는지 검증
    }


    @WithMockUser(roles = "USER")
    @Test
    public void helloDto가_리턴된다() throws Exception {
        String name = "hello";
        int amount = 1000;

        mvc.perform(
                get("/hello/dto")
                    .param("name", name)
                    .param("amount", String.valueOf(amount))) //API 테스트할 때 사용될 요청 파라미터로 String값만 허용 됨
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.name", is(name)))
                        .andExpect(jsonPath("$.amount", is(amount)));
    }
}
