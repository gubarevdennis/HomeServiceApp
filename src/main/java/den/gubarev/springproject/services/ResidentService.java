package den.gubarev.springproject.services;

import den.gubarev.springproject.models.Resident;
import den.gubarev.springproject.repositories.ResidentRepository;
import den.gubarev.springproject.util.Errors.ResidentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ResidentService {

    private final ResidentRepository residentRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ResidentService(ResidentRepository residentRepository, PasswordEncoder passwordEncoder) {
        this.residentRepository = residentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Resident> findAll() {
        return residentRepository.findAll();
    }

    public List<Resident> findAll(String sortByField) {
        return residentRepository.findAll(Sort.by(sortByField));
    }

    public List<Resident> findAll(int page, int orderPerPage, String sortByField) {
        return residentRepository.findAll(PageRequest.of(page,orderPerPage, Sort.by(sortByField))).getContent();
    }

    public Resident findOne(String name) {
        Optional<Resident> foundResident = residentRepository.findByName(name);
        return foundResident.orElse(null);
    }

    public Resident findOne(int id) {
        Optional<Resident> foundResident = residentRepository.findById(id);
        return foundResident.orElseThrow(ResidentNotFoundException::new);
    }

    @Transactional
    public void save(Resident resident) {

        // Проверка на существубщего пользователя
        Optional<Resident> foundResident = residentRepository.findByName(resident.getName());

        if(foundResident.isEmpty()) {

            // Шифруем пароль
            String encodedPassword = passwordEncoder.encode(resident.getPassword());
            resident.setPassword(encodedPassword);
            // Фиксируем время обращения к серверу
            resident.setDateAndTimeOfCreate(resident.createDateAndTime());
            // Назначаем роль
            resident.setRole("ROLE_RESIDENT");
            residentRepository.save(resident);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void update(int id, Resident updatedResident) {
        updatedResident.setId(id);
        residentRepository.save(updatedResident);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void delete(int id) {
        residentRepository.deleteById(id);
    }


}
