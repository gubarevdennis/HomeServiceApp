package den.gubarev.springproject.dto;

import den.gubarev.springproject.models.Task;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

public class EmployeeDTO {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Name must be not empty")
    @Size(min = 2, max = 30, message = "Out of range")
    @Column(name = "name")
    private String name;

    @Column(name = "position")
    @Size(min = 0, max = 30, message = "Out of range")
    private String position;

    @Column(name = "company")
    @Size(min = 0, max = 30, message = "Out of range")
    private String company;

    @Min(value = 0, message = "Out of range")
    @Max(value = 5, message = "Out of range")
    @Column(name = "grade")
    private double grade;

    @Size(min = 0, max = 30, message = "Out of range")
    @Column(name = "information")
    private String information;

    @Pattern(regexp = "\\d{2}:\\d{2}:\\d{2} \\d{2}-[A-Z]\\w+-\\d{4}", message = "Incorrect time format (Example: 23:59:59 23-February-2019)")
    @Column(name = "date_and_time_of_create")
    private String dateAndTimeOfCreate;

    @ManyToMany
    @JoinTable(
            name = "task_employee",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
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
}
