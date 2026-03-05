package problem5;

import java.util.*;

class PageViewEvent {
    String url;
    String userId;
    String source;


    public PageViewEvent(String url, String userId, String source) {
        this.url = url;
        this.userId = userId;
        this.source = source;
    }


}

public class RealTimeAnalytics {

    // pageUrl -> visit count
    private Map<String, Integer> pageViews = new HashMap<>();

    // pageUrl -> unique users
    private Map<String, Set<String>> uniqueVisitors = new HashMap<>();

    // traffic source -> count
    private Map<String, Integer> trafficSources = new HashMap<>();

    // Process incoming page view event
    public void processEvent(PageViewEvent event) {

        // Update page views
        pageViews.put(event.url, pageViews.getOrDefault(event.url, 0) + 1);

        // Track unique visitors
        uniqueVisitors.putIfAbsent(event.url, new HashSet<>());
        uniqueVisitors.get(event.url).add(event.userId);

        // Track traffic source
        trafficSources.put(event.source,
                trafficSources.getOrDefault(event.source, 0) + 1);
    }

    // Get Top 10 pages
    private List<Map.Entry<String, Integer>> getTopPages() {

        List<Map.Entry<String, Integer>> list =
                new ArrayList<>(pageViews.entrySet());

        list.sort((a, b) -> b.getValue() - a.getValue());

        return list.subList(0, Math.min(10, list.size()));
    }

    // Display dashboard
    public void getDashboard() {

        System.out.println("\n===== REAL-TIME DASHBOARD =====\n");

        System.out.println("Top Pages:");

        int rank = 1;
        for (Map.Entry<String, Integer> entry : getTopPages()) {

            String url = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(url).size();

            System.out.println(rank + ". " + url + " - "
                    + views + " views (" + unique + " unique)");

            rank++;
        }

        System.out.println("\nTraffic Sources:");

        int total = trafficSources.values().stream().mapToInt(i -> i).sum();

        for (Map.Entry<String, Integer> entry : trafficSources.entrySet()) {

            double percent = (entry.getValue() * 100.0) / total;

            System.out.printf("%s: %.1f%%\n", entry.getKey(), percent);
        }
    }

    public static void main(String[] args) {

        RealTimeAnalytics analytics = new RealTimeAnalytics();

        analytics.processEvent(new PageViewEvent(
                "/article/breaking-news", "user_123", "google"));

        analytics.processEvent(new PageViewEvent(
                "/article/breaking-news", "user_456", "facebook"));

        analytics.processEvent(new PageViewEvent(
                "/sports/championship", "user_789", "google"));

        analytics.processEvent(new PageViewEvent(
                "/sports/championship", "user_123", "direct"));

        analytics.processEvent(new PageViewEvent(
                "/article/breaking-news", "user_789", "google"));

        analytics.processEvent(new PageViewEvent(
                "/tech/ai-future", "user_222", "direct"));

        analytics.getDashboard();
    }

}
