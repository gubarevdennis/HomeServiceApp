package den.gubarev.springproject.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "Employee")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Employee {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToMany
    @JoinTable(
            name = "task_employee",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private List<Task> tasks;

    @NotEmpty(message = "Name must be not empty")
    @Size(min = 2, max = 30, message = "Out of range")
    @Column(name = "name")
//    @Pattern(regexp = "[А-Я]\\w+ [А-Я]\\w+", message = "Incorrect name format (Example: Ivanov Ivan")
    private String name;

    @NotEmpty(message = "Name must be not empty")
    @Column(name = "password")
    private String password;

    @NotEmpty(message = "Role must be not empty")
    @Column(name = "role")
    private String role;

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

    public Employee() {}

    public Employee(String name, String position, String company, int apartmentNumber, String information, String dateAndTimeOfCreate) {
        this.name = name;
        this.position = position;
        this.company = company;
        this.grade = apartmentNumber;
        this.information = information;
        this.dateAndTimeOfCreate = dateAndTimeOfCreate;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public double getGrade() {
        return grade;
    }

    public String getInformation() {
        return information;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDateAndTimeOfCreate() {
        return dateAndTimeOfCreate;
    }

    public void setDateAndTimeOfCreate(String dateAndTimeOfCreate) {
        this.dateAndTimeOfCreate = dateAndTimeOfCreate;
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

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void addTask(Task task) {
        if (this.tasks == null)
            this.tasks = new ArrayList<>();

        this.tasks.add(task);
        task.getEmployees().add(this);
    }

    public String createDateAndTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss dd-MMMM-yyyy", Locale.ENGLISH);
        return dtf.format(LocalDateTime.now());
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", tasks=" + tasks +
                ", name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", company='" + company + '\'' +
                ", grade=" + grade +
                ", information='" + information + '\'' +
                ", dateAndTimeOfCreate='" + dateAndTimeOfCreate + '\'' +
                '}';
    }
}
