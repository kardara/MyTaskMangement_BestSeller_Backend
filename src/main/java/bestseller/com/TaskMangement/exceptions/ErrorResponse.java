package bestseller.com.TaskMangement.exceptions;

import java.util.Map;

public record ErrorResponse(
    String message, 
    Map<String, String> details,
    String timestamp,
    int code) {
}
