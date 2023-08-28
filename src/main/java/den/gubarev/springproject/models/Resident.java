package den.gubarev.springproject.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@Entity
@Table(name = "Resident")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Resident {

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

    @NotEmpty(message = "Role must be not empty")
    @Column(name = "role")
    private String role;

    @Min(value = 1, message = "Out of range")
    @Column(name = "apartment_number")
    private int apartmentNumber;

    @Size(min = 0, max = 30, message = "Out of range")
    @Column(name = "information")
    private String information;

    @Pattern(regexp = "\\d{2}:\\d{2}:\\d{2} \\d{2}-[A-Z]\\w+-\\d{4}", message = "Incorrect time format (Example: 23:59:59 23-February-2019)")
    @Column(name = "date_and_time_of_create")
    private String dateAndTimeOfCreate;

    @OneToMany(mappedBy = "resident", fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private List<Task> tasks;

    public Resident() {}

    public Resident(String name, int apartmentNumber, String information, String dateAndTimeOfCreate) {
        this.name = name;
        this.apartmentNumber = apartmentNumber;
        this.information = information;
        this.dateAndTimeOfCreate = dateAndTimeOfCreate;
    }

    public void setApartmentNumber(int apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public int getApartmentNumber() {
        return apartmentNumber;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDateAndTimeOfCreate() {
        return dateAndTimeOfCreate;
    }

    public void setDateAndTimeOfCreate(String dateAndTimeOfCreate) {
        this.dateAndTimeOfCreate = dateAndTimeOfCreate;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String createDateAndTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss dd-MMMM-yyyy", Locale.ENGLISH);
        return dtf.format(LocalDateTime.now());
    }

    public void addTask(Task task) {
        if (this.tasks == null)
            this.tasks = new ArrayList<>();

        this.tasks.add(task);
        task.setResident(this);
    }

    @Override
    public String toString() {
        return "Resident{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", apartmentNumber=" + apartmentNumber +
                ", information='" + information + '\'' +
                ", dateAndTimeOfCreate='" + dateAndTimeOfCreate + '\'' +
                ", tasks=" + tasks +
                '}';
    }
}
