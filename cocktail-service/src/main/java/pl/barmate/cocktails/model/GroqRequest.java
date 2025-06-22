package pl.barmate.cocktails.model;

import lombok.Data;
import java.util.List;

@Data
public class GroqRequest {
    private String model;
    private List<Message> messages;
    private double temperature = 0.5;

    public GroqRequest(String model, String systemPrompt, String userPrompt) {
        this.model = model;
        this.messages = List.of(
                new Message("system", systemPrompt),
                new Message("user", userPrompt)
        );
    }

    @Data
    public static class Message {
        private String role;
        private String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}
