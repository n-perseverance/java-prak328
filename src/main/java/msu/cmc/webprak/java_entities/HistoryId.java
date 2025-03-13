package msu.cmc.webprak.java_entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class HistoryId implements Serializable {

    @Column(name = "isbn", nullable = false)
    private String isbn;

    @Column(name = "copy_id", nullable = false)
    private Integer copyId;

    @Column(name = "reader_id", nullable = false)
    private Integer readerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryId historyId = (HistoryId) o;
        return Objects.equals(isbn, historyId.isbn)
                && Objects.equals(copyId, historyId.copyId)
                && Objects.equals(readerId, historyId.readerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn, copyId, readerId);
    }
}