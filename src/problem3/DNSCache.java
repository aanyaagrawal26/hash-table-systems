package problem3;

import java.util.HashMap;
import java.util.Map;

public class DNSCache {

    private Map<String, String> cache = new HashMap<>();

    // Add a domain and its IP address
    public void addEntry(String domain, String ip) {
        cache.put(domain, ip);
    }

    // Get IP address from domain
    public String getIP(String domain) {
        return cache.getOrDefault(domain, "Domain not found in cache");
    }

    // Display all cached domains
    public void displayCache() {
        System.out.println("DNS Cache:");
        for (Map.Entry<String, String> entry : cache.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    public static void main(String[] args) {

        DNSCache dns = new DNSCache();

        dns.addEntry("google.com", "142.250.190.14");
        dns.addEntry("github.com", "140.82.121.3");
        dns.addEntry("openai.com", "104.18.12.123");

        dns.displayCache();

        System.out.println();
        System.out.println("IP for github.com: " + dns.getIP("github.com"));
        System.out.println("IP for facebook.com: " + dns.getIP("facebook.com"));
    }
}