package testTask;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        DocumentManager manager = new DocumentManager();

        // Создание авторов
        DocumentManager.Author authorFirst = DocumentManager.Author.builder().id("1").name("Author One").build();
        DocumentManager.Author authorSecond = DocumentManager.Author.builder().id("2").name("Author Two").build();

        // Создание документов
        DocumentManager.Document doc1 = DocumentManager.Document.builder()
                .title("Document One")
                .content("This is the content of document one.")
                .author(authorFirst)
                .created(Instant.now())
                .build();

        DocumentManager.Document doc2 = DocumentManager.Document.builder()
                .title("Document Two")
                .content("This is the content of document two.")
                .author(authorSecond)
                .created(Instant.now())
                .build();

        // Сохранение документов
        manager.save(doc1);
        manager.save(doc2);

        // Поиск документов
        DocumentManager.SearchRequest request = DocumentManager.SearchRequest.builder()
                .titlePrefixes(Arrays.asList("Document"))
                .authorIds(Arrays.asList("1"))
                .build();

        List<DocumentManager.Document> searchResults = manager.search(request);
        System.out.println("Search results: " + searchResults);

        // Поиск документа по ID
        Optional<DocumentManager.Document> foundDoc = manager.findById(doc1.getId());
        System.out.println("Found document: " + foundDoc);
    }
}

