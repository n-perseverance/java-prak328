package msu.cmc.webprak.java_entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "history")
@Getter
@Setter
public class History implements CommonEntity<HistoryId> {

    @EmbeddedId
    private HistoryId id = new HistoryId();

    @ManyToOne
    @MapsId("isbn")
    @JoinColumn(name = "isbn", nullable = false)
    @Cascade(org.hibernate.annotations.CascadeType.MERGE)
    private Book book;

    @ManyToOne
    @MapsId("copyId")
    @JoinColumn(name = "copy_id", nullable = false)
    @Cascade(org.hibernate.annotations.CascadeType.MERGE)
    private Copy copy;

    @ManyToOne
    @MapsId("readerId")
    @JoinColumn(name = "reader_id", nullable = false)
    @Cascade(org.hibernate.annotations.CascadeType.MERGE)
    private Reader reader;

    @Column(nullable = false, name = "issue_date")
    private Date issueDate;

    @Column(nullable = false, name = "return_date")
    private Date returnDate;

    public String getFormattedIssueDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(issueDate);
    }

    public String getFormattedReturnDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(returnDate);
    }

    @Override
    public HistoryId getId() {
        return id;
    }

    @Override
    public void setId(HistoryId id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        History history = (History) o;
        return Objects.equals(id, history.id)
                && Objects.equals(copy, history.copy)
                && Objects.equals(reader, history.reader)
                && Objects.equals(issueDate, history.issueDate)
                && Objects.equals(returnDate, history.returnDate);
    }
}