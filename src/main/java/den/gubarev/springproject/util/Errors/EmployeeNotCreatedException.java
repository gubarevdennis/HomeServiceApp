package den.gubarev.springproject.util.Errors;

public class EmployeeNotCreatedException extends RuntimeException{
    public EmployeeNotCreatedException(String message) {
        super(message);
    }
}
