package back.backend;

import java.util.Map;
import java.util.stream.Collectors;

public class EmailForm {

    private String ownerEmail;
    private String userEmail;
    private String formTitle;
    private Map<String, String> formData;

    // getters and setters

    public String getFormDataAsString() {
        return formData.entrySet().stream()
            .map(entry -> "<p><b>" + entry.getKey() + ":</b> " + entry.getValue() + "</p>")
            .collect(Collectors.joining(""));
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public String getFormTitle() {
        return formTitle;
    }

}
