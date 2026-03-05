package problem7;

import java.util.*;

class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    boolean isEndOfWord = false;
}

public class AutocompleteSystem {


    private TrieNode root = new TrieNode();

    // query -> frequency
    private Map<String, Integer> frequencyMap = new HashMap<>();

    // Insert query into Trie
    private void insert(String query) {

        TrieNode node = root;

        for (char c : query.toCharArray()) {

            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
        }

        node.isEndOfWord = true;
    }

    // Update frequency when a search occurs
    public void updateFrequency(String query) {

        frequencyMap.put(query, frequencyMap.getOrDefault(query, 0) + 1);

        if (!frequencyMap.containsKey(query) || frequencyMap.get(query) == 1) {
            insert(query);
        }
    }

    // Find node for prefix
    private TrieNode getPrefixNode(String prefix) {

        TrieNode node = root;

        for (char c : prefix.toCharArray()) {

            if (!node.children.containsKey(c)) {
                return null;
            }

            node = node.children.get(c);
        }

        return node;
    }

    // DFS to collect queries
    private void collectQueries(TrieNode node, String prefix, List<String> results) {

        if (node.isEndOfWord) {
            results.add(prefix);
        }

        for (char c : node.children.keySet()) {
            collectQueries(node.children.get(c), prefix + c, results);
        }
    }

    // Return top 10 suggestions
    public List<String> search(String prefix) {

        TrieNode node = getPrefixNode(prefix);

        if (node == null) {
            return new ArrayList<>();
        }

        List<String> results = new ArrayList<>();
        collectQueries(node, prefix, results);

        // Min heap for top 10
        PriorityQueue<String> heap =
                new PriorityQueue<>((a, b) ->
                        frequencyMap.get(a) - frequencyMap.get(b));

        for (String q : results) {

            heap.offer(q);

            if (heap.size() > 10) {
                heap.poll();
            }
        }

        List<String> suggestions = new ArrayList<>();

        while (!heap.isEmpty()) {
            suggestions.add(heap.poll());
        }

        Collections.reverse(suggestions);

        return suggestions;
    }

    public static void main(String[] args) {

        AutocompleteSystem system = new AutocompleteSystem();

        system.updateFrequency("java tutorial");
        system.updateFrequency("javascript");
        system.updateFrequency("java download");
        system.updateFrequency("java tutorial");
        system.updateFrequency("java tutorial");
        system.updateFrequency("java features");
        system.updateFrequency("java tutorial");
        system.updateFrequency("java vs python");

        List<String> results = system.search("jav");

        System.out.println("Suggestions for 'jav':");

        for (String r : results) {
            System.out.println(
                    r + " (" + system.frequencyMap.get(r) + " searches)"
            );
        }
    }


}
