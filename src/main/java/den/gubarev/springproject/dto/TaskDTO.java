package den.gubarev.springproject.dto;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;

public class TaskDTO {

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
