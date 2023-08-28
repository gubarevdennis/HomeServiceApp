package den.gubarev.springproject.services;

import den.gubarev.springproject.models.Employee;
import den.gubarev.springproject.models.Resident;
import den.gubarev.springproject.repositories.EmployeeRepository;
import den.gubarev.springproject.repositories.ResidentRepository;
import den.gubarev.springproject.security.EmployeeDetails;
import den.gubarev.springproject.security.ResidentDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ResidentAndEmployeeDetailsService implements UserDetailsService {
    private final EmployeeRepository employeeRepository;
    private final ResidentRepository residentRepository;

    @Autowired
    public ResidentAndEmployeeDetailsService(EmployeeRepository employeeRepository, ResidentRepository residentRepository) {

        this.employeeRepository = employeeRepository;
        this.residentRepository = residentRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Employee> foundEmployee = employeeRepository.findByName(username);
        Optional<Resident> foundResident = residentRepository.findByName(username);

        if (foundEmployee.isPresent()) {
            return new EmployeeDetails(foundEmployee.get());
        } else if (foundResident.isPresent()) {
            return new ResidentDetails(foundResident.get());
        }
            return null;
    }
}
