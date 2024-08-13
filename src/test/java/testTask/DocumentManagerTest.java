package testTask;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class DocumentManagerTest {

    private DocumentManager documentManager;

    @BeforeEach
    void setUp() {
        documentManager = new DocumentManager();
    }

    @Test
    void shouldSaveDocument() {
        DocumentManager.Author author = DocumentManager.Author.builder().id("1").name("Author One").build();
        DocumentManager.Document document = DocumentManager.Document.builder()
                .title("Document One")
                .content("Content of document one")
                .author(author)
                .created(Instant.now())
                .build();

        DocumentManager.Document savedDocument = documentManager.save(document);

        assertThat(savedDocument).isNotNull();
        assertThat(savedDocument.getId()).isNotNull();
        assertThat(documentManager.findById(savedDocument.getId())).isPresent().contains(savedDocument);
    }

    @Test
    void shouldSearchDocuments() {
        DocumentManager.Author author1 = DocumentManager.Author.builder().id("1").name("Author One").build();
        DocumentManager.Author author2 = DocumentManager.Author.builder().id("2").name("Author Two").build();

        DocumentManager.Document doc1 = DocumentManager.Document.builder()
                .title("Document One")
                .content("Content of document one")
                .author(author1)
                .created(Instant.now())
                .build();

        DocumentManager.Document doc2 = DocumentManager.Document.builder()
                .title("Document Two")
                .content("Content of document two")
                .author(author2)
                .created(Instant.now())
                .build();

        documentManager.save(doc1);
        documentManager.save(doc2);

        DocumentManager.SearchRequest request = DocumentManager.SearchRequest.builder()
                .titlePrefixes(Arrays.asList("Document"))
                .authorIds(Arrays.asList("1"))
                .build();

        List<DocumentManager.Document> searchResults = documentManager.search(request);

        assertThat(searchResults).hasSize(1);
        assertThat(searchResults).contains(doc1);
    }

    @Test
    void shouldReturnEmptyListForNonMatchingSearch() {
        DocumentManager.Document doc = DocumentManager.Document.builder()
                .title("Document One")
                .content("Content of document one")
                .author(DocumentManager.Author.builder().id("1").name("Author One").build())
                .created(Instant.now())
                .build();

        documentManager.save(doc);

        DocumentManager.SearchRequest request = DocumentManager.SearchRequest.builder()
                .titlePrefixes(Arrays.asList("Nonexistent"))
                .build();

        List<DocumentManager.Document> searchResults = documentManager.search(request);

        assertThat(searchResults).isEmpty();
    }

    @Test
    void shouldFindDocumentById() {
        DocumentManager.Author author = DocumentManager.Author.builder().id("1").name("Author One").build();
        DocumentManager.Document document = DocumentManager.Document.builder()
                .title("Document One")
                .content("Content of document one")
                .author(author)
                .created(Instant.now())
                .build();

        DocumentManager.Document savedDocument = documentManager.save(document);

        Optional<DocumentManager.Document> foundDocument = documentManager.findById(savedDocument.getId());

        assertThat(foundDocument).isPresent().contains(savedDocument);
    }

    @Test
    void shouldReturnEmptyOptionalForNonExistentId() {
        Optional<DocumentManager.Document> foundDocument = documentManager.findById(UUID.randomUUID().toString());

        assertThat(foundDocument).isNotPresent();
    }
}
