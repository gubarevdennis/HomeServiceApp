package den.gubarev.springproject.services;

import den.gubarev.springproject.models.Employee;
import den.gubarev.springproject.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import den.gubarev.springproject.security.EmployeeDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EmployeeDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeDetailsService(EmployeeRepository employeeRepository) {

        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Employee> foundEmployee = employeeRepository.findByName(username);

       if (foundEmployee.isPresent()) {
       return new EmployeeDetails(foundEmployee.get());
       } else
           return null;
    }
}
