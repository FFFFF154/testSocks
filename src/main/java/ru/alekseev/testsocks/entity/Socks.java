package ru.alekseev.testsocks.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Table(schema = "test_socks", name = "socks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Socks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_socks")
    private Long idSocks;

    private String color;

    @Column(name = "cotton_percent")
    private Double cottonPercent;

    private Integer quantity;

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "idSocks = " + idSocks + ", " +
                "color = " + color + ", " +
                "cottonPercent = " + cottonPercent + ", " +
                "quantity = " + quantity + ")";
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Socks socks = (Socks) o;
        return getIdSocks() != null && Objects.equals(getIdSocks(), socks.getIdSocks());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
