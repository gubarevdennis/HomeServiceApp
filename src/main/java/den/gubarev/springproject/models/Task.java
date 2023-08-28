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
@Table(name = "Task")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Task {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Id
    private int id;

    @NotEmpty(message = "Name of order must be not empty")
    @Size(min = 1, max = 30, message = "Out of range (Range: 1-30 digits)")
    @Column(name = "name")
    private String name;

//    @Pattern(regexp = "[A-Z]\\w+", message = "Incorrect status format (Example: Done")
    @Column(name = "status")
    private String status;

//    @NotEmpty(message = "Message must be not empty")
    @Size(min = 0, max = 1000, message = "Out of range (Range: 1-30 digits)")
    @Column(name = "message")
    private String message;

    @Min(value = 0, message = "Out of range")
    @Max(value = 5, message = "Out of range")
    @Column(name = "grade")
    private double grade;

    @Pattern(regexp = "\\d{2}:\\d{2}:\\d{2} \\d{2}-[A-Z]\\w+-\\d{4}", message = "Incorrect time format (Example: 23:59:59 23-February-2019)")
    @Column(name = "date_and_time_of_send")
    private String dateAndTimeOfSend;

    @Column(name = "date_and_time_of_get")
    @Pattern(regexp = "\\d{2}:\\d{2}:\\d{2} \\d{2}-[A-Z]\\w+-\\d{4}", message = "Incorrect time format (Example: 23:59:59 23-February-2019)")
    private String dateAndTimeOfGet;

    @Column(name = "date_and_time_of_done")
    @Pattern(regexp = "\\d{2}:\\d{2}:\\d{2} \\d{2}-[A-Z]\\w+-\\d{4}", message = "Incorrect time format (Example: 23:59:59 23-February-2019)")
    private String dateAndTimeOfDone;

    @ManyToMany(mappedBy = "tasks")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private List<Employee> employees;

    @ManyToOne
    @JoinColumn(name = "resident_id", referencedColumnName = "id")
    private Resident resident;

    @OneToOne(mappedBy = "task")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Chat chat;

    public Task() {}

    public Task(String status, String name, String message, int grade,
                String dateAndTimeOfSend, String dateAndTimeOfGet, String dateAndTimeOfDone) {
        this.status = status;
        this.name = name;
        this.message = message;
        this.grade = grade;
        this.dateAndTimeOfSend = dateAndTimeOfSend;
        this.dateAndTimeOfGet = dateAndTimeOfGet;
        this.dateAndTimeOfDone = dateAndTimeOfDone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public String getDateAndTimeOfSend() {
        return dateAndTimeOfSend;
    }

    public void setDateAndTimeOfSend(String dateAndTimeOfSend) {
        this.dateAndTimeOfSend = dateAndTimeOfSend;
    }

    public String getDateAndTimeOfGet() {
        return dateAndTimeOfGet;
    }

    public void setDateAndTimeOfGet(String dateAndTimeOfGet) {
        this.dateAndTimeOfGet = dateAndTimeOfGet;
    }

    public String getDateAndTimeOfDone() {
        return dateAndTimeOfDone;
    }

    public void setDateAndTimeOfDone(String dateAndTimeOfDone) {
        this.dateAndTimeOfDone = dateAndTimeOfDone;
    }

    public Resident getResident() {
        return resident;
    }

    public void setResident(Resident resident) {
        this.resident = resident;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public void addEmployee(Employee employee) {
        if (this.employees == null)
            this.employees = new ArrayList<>();

        this.employees.add(employee);
        employee.getTasks().add(this);
    }

    public String createDateAndTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss dd-MMMM-yyyy", Locale.ENGLISH);
        return dtf.format(LocalDateTime.now());
    }

}
