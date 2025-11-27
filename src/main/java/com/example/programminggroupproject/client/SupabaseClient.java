package com.example.programminggroupproject.client;

import okhttp3.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class SupabaseClient {
    private static final String SUPABASE_URL = "https://your-project.supabase.co";
    private static final String SUPABASE_KEY = "your-anon-key";

    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    public SupabaseClient() {
        this.client = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    // Simple query returning raw JSON string
    public String query(String table, String query) throws IOException {
        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/rest/v1/" + table + query)
                .addHeader("apikey", SUPABASE_KEY)
                .addHeader("Authorization", "Bearer " + SUPABASE_KEY)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() == null) {
                throw new IOException("Empty response body");
            }
            return response.body().string();
        }
    }

    // Generic query that deserializes to a specific type
    public <T> T query(String table, String query, Class<T> responseType) throws IOException {
        String jsonResponse = query(table, query);
        return objectMapper.readValue(jsonResponse, responseType);
    }
}