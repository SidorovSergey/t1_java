package ru.t1.java.demo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

abstract class BaseControllerTest {

    private MockMvc mockMvc;

    protected abstract Object getStandalone();

    @BeforeEach
    protected void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(getStandalone())
                .alwaysDo(MockMvcResultHandlers.print())
                .setControllerAdvice(new ExceptionController())
                .build();
    }

    protected void send(RequestBuilder request, String expectedResponse) throws Exception {
        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedResponse, true));
    }

    protected void send(RequestBuilder request) throws Exception {
        mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

}
