package org.larin.budget.data.spec;

import org.larin.budget.data.entity.tx.Transaction;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;
import java.util.List;


public class TxSpecs {
    public static Specification<Transaction> createdAfter(OffsetDateTime t) {
        return (root, query, cb) ->
                cb.greaterThan(root.get("createDate"), t);
    }

    public static Specification<Transaction> createdBefore(OffsetDateTime t) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("createDate"), t);
    }

    public static Specification<Transaction> hasCategory(List<String> categoryNames) {
        return (root, query, cb) ->
                root.get("category").get("name").in(categoryNames);
    }
}
