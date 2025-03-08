package msu.cmc.webprak.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "history")
@Getter
@Setter
public class History implements CommonEntity<HistoryId> {

    @EmbeddedId
    private HistoryId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("isbn")
    @JoinColumns({
            @JoinColumn(name = "isbn", referencedColumnName = "isbn", nullable = false),
            @JoinColumn(name = "copy_id", referencedColumnName = "copy_id", nullable = false)
    })
    private Copy copy;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("readerId")
    @JoinColumn(name = "reader_id", nullable = false)
    private Reader reader;

    @Column(nullable = false, name = "issue_date")
    private Date issueDate;

    @Column(nullable = false, name = "return_date")
    private Date returnDate;

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

    @Override
    public int hashCode() {
        return Objects.hash(id, copy, reader, issueDate, returnDate);
    }
}