package den.gubarev.springproject.dto;

import den.gubarev.springproject.models.Task;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

public class ResidentDTO {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Name must be not empty")
    @Size(min = 2, max = 30, message = "Out of range")
//    @Pattern(regexp = "[A-Z]\\w+ [A-Z]\\w+", message = "Incorrect name format (Example: Ivanov Ivan")
    @Column(name = "name")
    private String name;

    @NotEmpty(message = "Name must be not empty")
    @Column(name = "password")
    private String password;

    @Min(value = 1, message = "Out of range")
    @Column(name = "apartment_number")
    private int apartmentNumber;

    @Size(min = 0, max = 30, message = "Out of range")
    @Column(name = "information")
    private String information;

    @Pattern(regexp = "\\d{2}:\\d{2}:\\d{2} \\d{2}-[A-Z]\\w+-\\d{4}", message = "Incorrect time format (Example: 23:59:59 23-February-2019)")
    @Column(name = "date_and_time_of_create")
    private String dateAndTimeOfCreate;

    @OneToMany(mappedBy = "resident")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private List<TaskDTO> tasks;

    public List<TaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDTO> tasks) {
        this.tasks = tasks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(int apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getDateAndTimeOfCreate() {
        return dateAndTimeOfCreate;
    }

    public void setDateAndTimeOfCreate(String dateAndTimeOfCreate) {
        this.dateAndTimeOfCreate = dateAndTimeOfCreate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
