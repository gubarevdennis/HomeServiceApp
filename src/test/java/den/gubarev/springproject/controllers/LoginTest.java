package den.gubarev.springproject.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

////////////////////////////////////////////////////
// Тестами покрываем все методы всех контроллеров //
///////////////////////////////////////////////////

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mvc;

    @Autowired
    private AuthController authController;

    @Test
    public void test() throws Exception{
        this.mvc.perform(get("/auth/login")) // get запрос
                .andDo(print()) // выводит в консоль
                .andExpect(status().isOk()) // обертка над assert
                .andExpect(content().string(containsString("Введите имя пользователя:")));
    }

    @Test
    public void accessDeniedTest() throws Exception{
        this.mvc.perform(get("/resident"))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void correctLoginTest() throws Exception{
        this.mvc.perform(formLogin("/process_login").user("resident").password("password"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/resident"));
    }

    @Test
    public void badCredentials() throws Exception{
        this.mvc.perform(post("/process_login").param("user","NoName"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

}
