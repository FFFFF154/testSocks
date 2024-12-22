package ru.alekseev.testsocks.filter;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.*;
import org.springframework.data.jpa.domain.Specification;
import ru.alekseev.testsocks.entity.Socks;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocksSearchFilter {
    private String color;
    private Double cottonPercent;
    private String operation;
    private String sort;
    private Double minPercent;
    private Double maxPercent;

    public Specification<Socks> buildSpecification() {
        Specification<Socks> spec = Specification.where(null);
        return null;
    }

}
