package problem10;

import java.util.*;

class VideoData {
    String videoId;
    String content;


    public VideoData(String videoId, String content) {
        this.videoId = videoId;
        this.content = content;
    }


}

public class MultiLevelCache {


    // L1 Cache (LRU using LinkedHashMap)
    private LinkedHashMap<String, VideoData> L1;

    // L2 Cache (SSD simulation)
    private Map<String, VideoData> L2 = new HashMap<>();

    // L3 Database simulation
    private Map<String, VideoData> database = new HashMap<>();

    // access frequency
    private Map<String, Integer> accessCount = new HashMap<>();

    // statistics
    private int L1Hits = 0;
    private int L2Hits = 0;
    private int L3Hits = 0;

    public MultiLevelCache(int L1Capacity) {

        L1 = new LinkedHashMap<String, VideoData>(L1Capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                return size() > L1Capacity;
            }
        };

        // preload database
        database.put("video_123", new VideoData("video_123", "Movie A"));
        database.put("video_456", new VideoData("video_456", "Movie B"));
        database.put("video_999", new VideoData("video_999", "Movie C"));
    }

    public VideoData getVideo(String videoId) {

        long start = System.nanoTime();

        // ----- L1 CACHE -----
        if (L1.containsKey(videoId)) {
            L1Hits++;
            System.out.println("L1 Cache HIT (0.5ms)");
            return L1.get(videoId);
        }

        System.out.println("L1 Cache MISS");

        // ----- L2 CACHE -----
        if (L2.containsKey(videoId)) {
            L2Hits++;
            System.out.println("L2 Cache HIT (5ms)");

            VideoData data = L2.get(videoId);

            promoteToL1(videoId, data);

            return data;
        }

        System.out.println("L2 Cache MISS");

        // ----- L3 DATABASE -----
        if (database.containsKey(videoId)) {
            L3Hits++;
            System.out.println("L3 Database HIT (150ms)");

            VideoData data = database.get(videoId);

            L2.put(videoId, data);
            accessCount.put(videoId, 1);

            return data;
        }

        System.out.println("Video not found.");
        return null;
    }

    private void promoteToL1(String videoId, VideoData data) {

        int count = accessCount.getOrDefault(videoId, 0) + 1;
        accessCount.put(videoId, count);

        if (count >= 2) {
            L1.put(videoId, data);
            System.out.println("Promoted to L1 Cache");
        }
    }

    public void invalidate(String videoId) {

        L1.remove(videoId);
        L2.remove(videoId);
        database.remove(videoId);

        System.out.println("Cache invalidated for " + videoId);
    }

    public void getStatistics() {

        int total = L1Hits + L2Hits + L3Hits;

        System.out.println("\nCache Statistics:");

        System.out.printf("L1 Hit Rate: %.2f%%\n",
                total == 0 ? 0 : (L1Hits * 100.0 / total));

        System.out.printf("L2 Hit Rate: %.2f%%\n",
                total == 0 ? 0 : (L2Hits * 100.0 / total));

        System.out.printf("L3 Hit Rate: %.2f%%\n",
                total == 0 ? 0 : (L3Hits * 100.0 / total));
    }

    public static void main(String[] args) {

        MultiLevelCache cache = new MultiLevelCache(3);

        System.out.println("Request 1:");
        cache.getVideo("video_123");

        System.out.println("\nRequest 2:");
        cache.getVideo("video_123");

        System.out.println("\nRequest 3:");
        cache.getVideo("video_999");

        cache.getStatistics();
    }

}
