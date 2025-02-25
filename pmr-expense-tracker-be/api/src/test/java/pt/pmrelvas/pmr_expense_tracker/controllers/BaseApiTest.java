package pt.pmrelvas.pmr_expense_tracker.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;

public abstract class BaseApiTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    protected <T> T request(RequestDto<T> requestDto) throws Exception {
        MvcResult result = mockMvc
                .perform(requestDto.requestBuilder())
                .andExpect(requestDto.resultMatcher())
                .andReturn();
        return objectMapper.readValue(result.getResponse().getContentAsString(), requestDto.clazz());
    }

    protected <T> List<T> requestList(RequestDto<T> requestDto) throws Exception {
        MvcResult result = mockMvc
                .perform(requestDto.requestBuilder())
                .andExpect(requestDto.resultMatcher())
                .andReturn();
        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                objectMapper.getTypeFactory()
                        .constructCollectionType(List.class, requestDto.clazz()));
    }

    @Builder(toBuilder = true)
    protected record RequestDto<T>(
            RequestBuilder requestBuilder,
            ResultMatcher resultMatcher,
            Class<T> clazz
    ) {

    }

}
