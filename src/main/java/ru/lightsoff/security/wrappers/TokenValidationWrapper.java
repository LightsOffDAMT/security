package ru.lightsoff.security.wrappers;

public class TokenValidationWrapper {
    private TokenValidationVerdict verdict;
    private String message;

    public TokenValidationWrapper(TokenValidationVerdict verdict, String message) {
        this.verdict = verdict;
        this.message = message;
    }

    public TokenValidationVerdict getVerdict() {
        return verdict;
    }

    public TokenValidationWrapper setVerdict(TokenValidationVerdict verdict) {
        this.verdict = verdict;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public TokenValidationWrapper setMessage(String message) {
        this.message = message;
        return this;
    }
}
