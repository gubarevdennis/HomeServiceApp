package den.gubarev.springproject.util;

//import den.gubarev.springproject.DAO.ResidentDAO;
import den.gubarev.springproject.models.Resident;
import den.gubarev.springproject.services.ResidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ResidentValidator implements Validator {
    // нужен для объектов класса Resident
    @Override
    public boolean supports(Class<?> clazz) {
        return Resident.class. equals(clazz);
    }

    private final ResidentService residentService;

    @Autowired
    public ResidentValidator(ResidentService residentService) {
        this.residentService = residentService;
    }
    @Override
    public void validate(Object o, Errors errors) {
        // есть ли человек с таким же емаилом
        Resident resident = (Resident) o;

        System.out.println("----->" + residentService.findOne(resident.getName()));

        if (residentService.findOne(resident.getName()) != null) { // проверка на null
            errors.rejectValue("name", "", "Человек с таким именем пользователя существует!");
        }
    }
}
