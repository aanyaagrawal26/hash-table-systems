package problem6;

import java.util.HashMap;
import java.util.Map;

class TokenBucket {


    int tokens;
    int maxTokens;
    int refillRate; // tokens added per second
    long lastRefillTime;

    public TokenBucket(int maxTokens, int refillRate) {
        this.maxTokens = maxTokens;
        this.refillRate = refillRate;
        this.tokens = maxTokens;
        this.lastRefillTime = System.currentTimeMillis();
    }

    public synchronized boolean allowRequest() {

        refill();

        if (tokens > 0) {
            tokens--;
            return true;
        }

        return false;
    }

    private void refill() {

        long now = System.currentTimeMillis();
        long elapsed = (now - lastRefillTime) / 1000;

        if (elapsed > 0) {
            int refillTokens = (int) (elapsed * refillRate);
            tokens = Math.min(maxTokens, tokens + refillTokens);
            lastRefillTime = now;
        }
    }

    public int getRemainingTokens() {
        refill();
        return tokens;
    }


}

public class RateLimiter {


    // clientId -> token bucket
    private Map<String, TokenBucket> clients = new HashMap<>();

    private int limit = 1000;
    private int refillRate = 1000 / 3600; // tokens per second

    public boolean checkRateLimit(String clientId) {

        clients.putIfAbsent(clientId,
                new TokenBucket(limit, refillRate));

        TokenBucket bucket = clients.get(clientId);

        boolean allowed = bucket.allowRequest();

        if (allowed) {
            System.out.println(
                    "Allowed (" + bucket.getRemainingTokens() +
                            " requests remaining)"
            );
        } else {
            System.out.println(
                    "Denied (0 requests remaining, retry later)"
            );
        }

        return allowed;
    }

    public void getRateLimitStatus(String clientId) {

        TokenBucket bucket = clients.get(clientId);

        if (bucket == null) {
            System.out.println("No requests from client yet.");
            return;
        }

        int remaining = bucket.getRemainingTokens();
        int used = limit - remaining;

        System.out.println(
                "{used: " + used +
                        ", limit: " + limit +
                        ", remaining: " + remaining + "}"
        );
    }

    public static void main(String[] args) {

        RateLimiter limiter = new RateLimiter();

        String client = "abc123";

        for (int i = 0; i < 5; i++) {
            limiter.checkRateLimit(client);
        }

        limiter.getRateLimitStatus(client);
    }


}
