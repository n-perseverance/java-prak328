package msu.cmc.webprak.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "readers")
@Getter
@Setter
public class Reader implements CommonEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "reader_id")
    private Integer id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "phone_number")
    private long phoneNumber;

    @Column(nullable = false, name = "address")
    private String address;

    public Reader(
            Integer id,
            String name,
            long phoneNumber,
            String address
    ) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public Reader() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reader other = (Reader) o;
        return Objects.equals(id, other.id)
                && name.equals(other.name)
                && phoneNumber == other.phoneNumber
                && address.equals(other.address);
    }
}