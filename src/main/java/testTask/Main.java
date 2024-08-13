package testTask;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        DocumentManager manager = new DocumentManager();

        DocumentManager.Author authorFirst = DocumentManager.Author.builder().id("1").name("Author One").build();
        DocumentManager.Author authorSecond = DocumentManager.Author.builder().id("2").name("Author Two").build();

        DocumentManager.Document docFirst = DocumentManager.Document.builder()
                .title("Document One")
                .content("This is the content of document one.")
                .author(authorFirst)
                .created(Instant.now())
                .build();

        DocumentManager.Document docSecond = DocumentManager.Document.builder()
                .title("Document Two")
                .content("This is the content of document two.")
                .author(authorSecond)
                .created(Instant.now())
                .build();

        manager.save(docFirst);
        manager.save(docSecond);

        DocumentManager.SearchRequest request = DocumentManager.SearchRequest.builder()
                .titlePrefixes(Arrays.asList("Document"))
                .authorIds(Arrays.asList("1"))
                .build();

        List<DocumentManager.Document> searchResults = manager.search(request);
        System.out.println("Search results: " + searchResults);

        Optional<DocumentManager.Document> foundDoc = manager.findById(docFirst.getId());
        System.out.println("Found document: " + foundDoc);
    }
}

