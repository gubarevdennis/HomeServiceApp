package den.gubarev.springproject.repositories;


import den.gubarev.springproject.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Передаем сущность и тип первичного ключа
@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Integer> {
    Optional<Employee> findByName(String name);

}
