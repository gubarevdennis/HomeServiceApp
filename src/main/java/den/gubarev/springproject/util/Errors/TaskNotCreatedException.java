package den.gubarev.springproject.util.Errors;

public class TaskNotCreatedException extends RuntimeException{
    public TaskNotCreatedException(String message) {
        super(message);
    }
}
