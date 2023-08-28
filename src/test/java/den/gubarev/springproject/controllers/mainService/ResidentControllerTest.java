package den.gubarev.springproject.controllers.mainService;


import den.gubarev.springproject.controllers.mainService.ResidentController;
import den.gubarev.springproject.services.ResidentAndEmployeeDetailsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails(value = "resident", userDetailsServiceBeanName = "residentAndEmployeeDetailsService")
public class ResidentControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ResidentController residentController;

    @Test
    @Transactional
    public void residentPageTest() throws Exception{
        this.mvc.perform(get("/resident")) // get запрос
                .andDo(print()) // выводит в консоль
                .andExpect(authenticated())
                .andExpect(content().string(containsString("Выйти")));
    }

    @Test
    @Transactional
    public void employeePageTest() throws Exception{
        this.mvc.perform(get("/employee")) // get запрос
                .andDo(print()) // выводит в консоль
                .andExpect(authenticated())
                .andExpect(content().string(containsString("Выйти")));
    }

    @Test
    @Transactional
    public void taskPageTest() throws Exception{
        this.mvc.perform(get("/employee")) // get запрос
                .andDo(print()) // выводит в консоль
                .andExpect(authenticated())
                .andExpect(content().string(containsString("Выйти")));
    }
}
