package den.gubarev.springproject.repositories;

import den.gubarev.springproject.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Передаем сущность и тип первичного ключа
@Repository
public interface OrderRepository extends JpaRepository<Task,Integer> {

}
