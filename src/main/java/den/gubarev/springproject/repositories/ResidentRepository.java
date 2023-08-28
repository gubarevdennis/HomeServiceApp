package den.gubarev.springproject.repositories;


import den.gubarev.springproject.models.Employee;
import den.gubarev.springproject.models.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


// Передаем сущность и тип первичного ключа
@Repository
public interface ResidentRepository extends JpaRepository<Resident,Integer> {
    Optional<Resident> findByName(String name);
}
