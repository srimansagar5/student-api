package com.vidyasagar.attendance.api.v1.specification;

import com.vidyasagar.attendance.entity.Student;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudentSpecification {

    public static Specification<Student> withSearchAndFilters(String search, Map<String, Object> filters) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // ✅ Search logic (name, email)
            if (search != null && !search.trim().isEmpty()) {
                String pattern = "%" + search.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), pattern),
                        cb.like(cb.lower(root.get("email")), pattern)
                ));
            }

            // ✅ Filters logic (equals-based)
            if (filters != null && !filters.isEmpty()) {
                filters.forEach((field, value) -> {
                    try {
                        if (field != null && !field.trim().isEmpty() && value != null) {
                            predicates.add(cb.equal(root.get(field), value));
                        }
                    } catch (IllegalArgumentException e) {
                        System.err.println("⚠️ Invalid filter field: " + field);
                    }
                });
            }

            // ✅ Combine all using AND
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
