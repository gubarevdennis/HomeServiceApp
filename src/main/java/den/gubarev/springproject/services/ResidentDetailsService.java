package den.gubarev.springproject.services;

import den.gubarev.springproject.models.Resident;
import den.gubarev.springproject.repositories.ResidentRepository;
import den.gubarev.springproject.security.EmployeeDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import den.gubarev.springproject.security.ResidentDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ResidentDetailsService implements UserDetailsService {


    private final ResidentRepository residentRepository;

    @Autowired
    public ResidentDetailsService(ResidentRepository residentRepository) {
        this.residentRepository = residentRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Optional<Resident> foundResident = residentRepository.findByName(username);

        if (foundResident.isPresent()) {
            return new ResidentDetails(foundResident.get());
        } else
            return null;
    }
}
