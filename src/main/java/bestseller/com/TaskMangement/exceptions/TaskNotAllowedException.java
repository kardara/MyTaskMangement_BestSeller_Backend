package bestseller.com.TaskMangement.exceptions;

public class TaskNotAllowedException extends RuntimeException {
    public TaskNotAllowedException(String message) {
        super(message);
    }
}
