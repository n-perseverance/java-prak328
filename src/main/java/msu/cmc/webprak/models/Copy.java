package msu.cmc.webprak.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "copies")
@Getter
@Setter
public class Copy implements CommonEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "copy_id")
    private Integer copyId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "isbn", nullable = false)
    private Book book;

    @Column(nullable = false, name = "availability")
    private String availability;

    @Override
    public Integer getId() {
        return copyId;
    }

    @Override
    public void setId(Integer copyId) {
        this.copyId = copyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Copy copy = (Copy) o;
        return Objects.equals(copyId, copy.copyId)
                && Objects.equals(book, copy.book)
                && Objects.equals(availability, copy.availability);
    }

    @Override
    public int hashCode() {
        return Objects.hash(copyId, book, availability);
    }
}