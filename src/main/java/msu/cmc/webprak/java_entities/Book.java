package msu.cmc.webprak.java_entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.List;

@Entity
@Table(name = "books")
@Getter
@Setter
public class Book implements CommonEntity<String> {

    @Id
    @Column(nullable = false, name = "isbn")
    private String isbn;

    @Column(nullable = false, name = "name")
    private String name;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[]", nullable = false, name = "authors")
    private List<String> authors;

    @Column(nullable = false, name = "publisher")
    private String publisher;

    @Column(nullable = false, name = "year")
    private Date year;

    @Override
    public String getId() {
        return isbn;
    }

    @Override
    public void setId(String isbn) {
        this.isbn = isbn;
    }

    public String getYearOnly() {
        if (year != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            return sdf.format(year);
        }
        return "";
    }

    public void setYearFromString(String yearStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            this.year = sdf.parse(yearStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Неверный формат года");
        }
    }

    public Book(
            String isbn,
            String name,
            List<String> authors,
            String publisher,
            Date year
    ) {
        this.isbn = isbn;
        this.name = name;
        this.authors = authors;
        this.publisher = publisher;
        this.year = year;
    }

    public Book() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book other = (Book) o;
        return Objects.equals(isbn, other.isbn)
                && name.equals(other.name)
                && Objects.deepEquals(authors, other.authors)
                && publisher.equals(other.publisher)
                && year.equals(other.year);
    }
}