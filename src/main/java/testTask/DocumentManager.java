package testTask;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


public class DocumentManager {

    private final Map<String, Document> documentStorage = new HashMap<>();

    public Document save(Document document) {
      if (document.getId() == null || document.getId().isEmpty()) {
          document.setId(UUID.randomUUID().toString());

      }
      documentStorage.put(document.getId(), document);
        return document;
    }

    public List<Document> search(SearchRequest request) {
        return documentStorage.values().stream()
                .filter(document -> request.getTitlePrefixes() == null || request.getTitlePrefixes().stream().anyMatch(prefix -> document.getTitle().startsWith(prefix)))
                .filter(document -> request.getContainsContents() == null || request.getContainsContents().stream().anyMatch(content -> document.getContent().contains(content)))
                .filter(document -> request.getAuthorIds() == null || request.getAuthorIds().contains(document.getAuthor().getId()))
                .filter(document -> request.getCreatedFrom() == null || !document.getCreated().isBefore(request.getCreatedFrom()))
                .filter(document -> request.getCreatedTo() == null || !document.getCreated().isAfter(request.getCreatedTo()))
                .collect(Collectors.toList());

    }

    public Optional<Document> findById(String id) {
        return Optional.ofNullable(documentStorage.get(id));
    }

    @Data
    @Builder
    public static class SearchRequest {
        private List<String> titlePrefixes;
        private List<String> containsContents;
        private List<String> authorIds;
        private Instant createdFrom;
        private Instant createdTo;
    }

    @Data
    @Builder
    public static class Document {
        private String id;
        private String title;
        private String content;
        private Author author;
        private Instant created;
    }

    @Data
    @Builder
    public static class Author {
        private String id;
        private String name;
    }
}
