package org.larin.budget.data.repository;

import org.larin.budget.data.entity.Category;
import org.springframework.data.repository.CrudRepository;



public interface CategoryRepository extends CrudRepository<Category, Long> {
}
