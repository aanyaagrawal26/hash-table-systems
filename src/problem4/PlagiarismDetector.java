package problem4;

import java.util.*;

public class PlagiarismDetector {


    // n-gram → documents that contain it
    private Map<String, Set<String>> index = new HashMap<>();

    // document → set of n-grams
    private Map<String, Set<String>> documentNgrams = new HashMap<>();

    private int N = 5; // 5-gram

    public PlagiarismDetector(int n) {
        this.N = n;
    }

    // Break text into n-grams
    private Set<String> generateNgrams(String text) {
        String[] words = text.toLowerCase().split("\\s+");
        Set<String> ngrams = new HashSet<>();

        for (int i = 0; i <= words.length - N; i++) {
            StringBuilder sb = new StringBuilder();

            for (int j = 0; j < N; j++) {
                sb.append(words[i + j]).append(" ");
            }

            ngrams.add(sb.toString().trim());
        }

        return ngrams;
    }

    // Add document to database
    public void addDocument(String docId, String text) {

        Set<String> ngrams = generateNgrams(text);
        documentNgrams.put(docId, ngrams);

        for (String gram : ngrams) {
            index.putIfAbsent(gram, new HashSet<>());
            index.get(gram).add(docId);
        }

        System.out.println(docId + " indexed with " + ngrams.size() + " n-grams");
    }

    // Analyze plagiarism
    public void analyzeDocument(String docId, String text) {

        Set<String> ngrams = generateNgrams(text);
        Map<String, Integer> matchCount = new HashMap<>();

        for (String gram : ngrams) {

            if (index.containsKey(gram)) {

                for (String doc : index.get(gram)) {

                    if (!doc.equals(docId)) {
                        matchCount.put(doc, matchCount.getOrDefault(doc, 0) + 1);
                    }
                }
            }
        }

        System.out.println("Extracted " + ngrams.size() + " n-grams\n");

        for (String doc : matchCount.keySet()) {

            int matches = matchCount.get(doc);
            double similarity = (matches * 100.0) / ngrams.size();

            System.out.println("Found " + matches + " matching n-grams with \"" + doc + "\"");
            System.out.printf("Similarity: %.2f%%", similarity);

            if (similarity > 60) {
                System.out.println(" (PLAGIARISM DETECTED)");
            } else if (similarity > 10) {
                System.out.println(" (suspicious)");
            } else {
                System.out.println();
            }

            System.out.println();
        }
    }

    public static void main(String[] args) {

        PlagiarismDetector detector = new PlagiarismDetector(5);

        String essay1 = "Artificial intelligence is transforming modern technology and enabling new innovations across industries";
        String essay2 = "Artificial intelligence is transforming modern technology and enabling powerful innovations in many industries";
        String essay3 = "Sports and physical activities improve health and help people maintain fitness";

        detector.addDocument("essay_089.txt", essay1);
        detector.addDocument("essay_092.txt", essay2);
        detector.addDocument("essay_200.txt", essay3);

        System.out.println("\nChecking new submission:\n");

        detector.analyzeDocument(
                "essay_123.txt",
                "Artificial intelligence is transforming modern technology and enabling new innovations across industries"
        );
    }


}
