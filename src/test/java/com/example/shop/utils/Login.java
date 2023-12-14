package com.example.shop.utils;

import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Component
public final class Login {
    @Autowired
    MockMvc mockMvc;

    public String getJWTToken(String username, String password) throws Exception {
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/v1/users/login")
                        .with(httpBasic(username,password)));
        //convertAction to mvcResult
        MvcResult mvcResult = resultActions.andReturn();
        //get response from mvcResult and convert to string
        String responseString = mvcResult.getResponse().getContentAsString();
        //convert string to json object
        JSONObject jsonResponse = new JSONObject(responseString);
        //finally save token in token field
        return "Bearer " + jsonResponse.getString("token");
    }

}
