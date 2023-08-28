package den.gubarev.springproject.services;

import den.gubarev.springproject.models.Resident;
import den.gubarev.springproject.repositories.ResidentRepository;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.constraints.AssertTrue;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

////////////////////////////////////////////////
// Тестами покрываем все методы всех сервисов //
////////////////////////////////////////////////

@RunWith(SpringRunner.class)
@SpringBootTest
class ResidentServiceTest {

    @Autowired
    private ResidentService residentService;

    @MockBean
    private ResidentRepository residentRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void save() {
        Resident resident = new Resident();

        residentService.save(resident);

        Assert.assertNotNull(resident.getDateAndTimeOfCreate());
        Assert.assertTrue(CoreMatchers.is(resident.getRole()).matches("ROLE_RESIDENT"));

        // Работа с репозиторием
        Mockito.verify(residentRepository,Mockito.times(1)).save(resident);
    }

    @Test
    void saveFailTest() {
        Resident resident = new Resident();

        resident.setName("TestName");

        // Ищем пользователя, если находим то save не должен происходить
        Mockito.doReturn(Optional.of(resident))
                .when(residentRepository)
                .findByName("TestName");

        residentService.save(resident);

        // Прверяем что ничего не сработало при обнаружении резидента имеющимся именем
        Assert.assertNull(resident.getDateAndTimeOfCreate());
        Assert.assertFalse(CoreMatchers.is(resident.getRole()).matches("ROLE_RESIDENT"));

        // Работа с репозиторием
        Mockito.verify(residentRepository,Mockito.times(0)).save(ArgumentMatchers.any(Resident.class));
    }

    @Test
    void findAll() {

    }

    @Test
    void testFindAll() {
    }

    @Test
    void testFindAll1() {
    }

    @Test
    void findOne() {
    }

    @Test
    void testFindOne() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}