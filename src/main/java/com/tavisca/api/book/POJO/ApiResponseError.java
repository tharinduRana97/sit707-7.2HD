package com.tavisca.api.book.POJO;

public class ApiResponseError extends ApiResponse {
    private String message;

    // No-argument constructor
    public ApiResponseError() {
    }

    // Parameterized constructor
    public ApiResponseError(String message) {
        this.message = message;
    }

    // Getter
    public String getMessage() {
        return message;
    }

    // Setter
    public void setMessage(String message) {
        this.message = message;
    }

    // equals() and hashCode() for correctness and comparison

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ApiResponseError)) return false;
        ApiResponseError other = (ApiResponseError) obj;
        return message != null ? message.equals(other.message) : other.message == null;
    }

    @Override
    public int hashCode() {
        return message != null ? message.hashCode() : 0;
    }
}
