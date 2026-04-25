package com.example.newdesign.model;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

public class OllamaService {

    private boolean available;
    private HttpClient client;

    public OllamaService() {
        try {
            client = HttpClient.newHttpClient();

            HttpRequest testRequest = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:11434/api/tags"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(testRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                available = true;
                System.out.println("✅ Ollama connected successfully!");
            } else {
                available = false;
                System.out.println("⚠️ Ollama returned status: " + response.statusCode());
            }
        } catch (Exception e) {
            available = false;
            System.out.println("⚠️ Ollama not available - using fallback mode");
            System.out.println("   To enable AI, run in terminal: ollama serve");
        }
    }

    public boolean isAvailable() {
        return available;
    }

    public String getMatchRecommendations(User currentUser, List<User> allUsers) {
        if (!available) return null;

        String teach = formatSkills(currentUser.getTeachSkills());
        String learn = formatSkills(currentUser.getLearnSkills());

        StringBuilder usersStr = new StringBuilder();
        for (User u : allUsers) {
            usersStr.append("- ").append(u.getFullName())
                    .append(" (Teaches: ").append(formatSkills(u.getTeachSkills()))
                    .append(", Wants: ").append(formatSkills(u.getLearnSkills())).append(")\n");
        }

        String prompt = "You are a helpful skill exchange assistant. " +
                "The user teaches these skills: " + teach + ". " +
                "The user wants to learn these skills: " + learn + ". " +
                "Here are other users on the platform:\n" + usersStr.toString() + "\n" +
                "Recommend the top 3 best matches. For each match, explain why they are a good fit. " +
                "Be friendly, use emojis, and keep it concise.";

        return callOllama(prompt);
    }

    public String getComparison(User user1, User user2, User currentUser) {
        if (!available) return null;

        String prompt = "Compare these two users as skill exchange partners:\n\n" +
                "CURRENT USER teaches: " + formatSkills(currentUser.getTeachSkills()) + "\n" +
                "CURRENT USER wants to learn: " + formatSkills(currentUser.getLearnSkills()) + "\n\n" +
                "USER A (" + user1.getFullName() + "):\n" +
                "  Teaches: " + formatSkills(user1.getTeachSkills()) + "\n" +
                "  Wants to learn: " + formatSkills(user1.getLearnSkills()) + "\n\n" +
                "USER B (" + user2.getFullName() + "):\n" +
                "  Teaches: " + formatSkills(user2.getTeachSkills()) + "\n" +
                "  Wants to learn: " + formatSkills(user2.getLearnSkills()) + "\n\n" +
                "Which user is a better match for the current user? Explain why. Keep it brief.";

        return callOllama(prompt);
    }

    public String getScheduleTips(User match, User currentUser) {
        if (!available) return null;

        String prompt = "Give quick practical tips for scheduling a skill exchange session between:\n" +
                "- " + match.getFullName() + " teaches: " + formatSkills(match.getTeachSkills()) + "\n" +
                "- " + currentUser.getFullName() + " wants to learn: " + formatSkills(currentUser.getLearnSkills()) + "\n\n" +
                "Suggest: 1) Best time slots, 2) Where to meet (online or in-person), 3) How to prepare for first session. " +
                "Keep it brief and helpful, about 3-4 sentences.";

        return callOllama(prompt);
    }

    private String callOllama(String prompt) {
        try {
            String escapedPrompt = prompt
                    .replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");

            String json = String.format("""
                {
                    "model": "llama3.2:1b",
                    "prompt": "%s",
                    "stream": false,
                    "options": {
                        "temperature": 0.7,
                        "num_predict": 500
                    }
                }
                """, escapedPrompt);

            System.out.println("🤖 Calling Ollama API...");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:11434/api/generate"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String body = response.body();
                int start = body.indexOf("\"response\":\"") + 12;
                if (start > 12) {
                    int end = body.indexOf("\"", start);
                    if (end > start) {
                        String result = body.substring(start, end);
                        result = result.replace("\\n", "\n").replace("\\\"", "\"");
                        System.out.println("✅ Ollama response received!");
                        return result;
                    }
                }
                return "Could not parse Ollama response";
            } else {
                System.err.println("Ollama HTTP error: " + response.statusCode());
                return null;
            }

        } catch (Exception e) {
            System.err.println("Ollama error: " + e.getMessage());
            return null;
        }
    }

    private String formatSkills(List<Skill> skills) {
        if (skills == null || skills.isEmpty()) return "none";
        return skills.stream()
                .map(Skill::getSkillName)
                .collect(Collectors.joining(", "));
    }
}