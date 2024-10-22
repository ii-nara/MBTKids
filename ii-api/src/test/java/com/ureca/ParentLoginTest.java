package com.ureca;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class ParentLoginTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void 로그인페이지접근() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/mbtkids/login"))
        .andExpect(status().isOk())
        .andExpect(view().name("parent/login"));
  }

  @Test
  void 로그인실패() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/mbtkids/login")
            .param("loginIdOrEmail", "testId")
            .param("password", "1234"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/mbtkids/login"));

  }

  @WithMockUser(username = "testUser")
  @Test
  void 로그인_성공_home접근() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/mbtkids/success"))
        .andExpect(status().isOk())
        .andExpect(view().name("parent/success"));
  }
}
