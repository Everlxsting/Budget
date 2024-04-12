package org.larin.budget.service;

import org.larin.budget.OrikaMapper;
import org.larin.budget.data.dto.CategoryDTO;
import org.larin.budget.data.entity.Category;
import org.larin.budget.data.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;




@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repository;

    @Autowired
    private OrikaMapper mapper;

    @Transactional
    public List<CategoryDTO> getCategories() {
        Iterable<Category> p = repository.findAll();
        return mapper.mapAsList(p, CategoryDTO.class);
    }


    @Transactional
    public void create(CategoryDTO cater) {

        repository.save(mapper.map(cater, Category.class));
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

}
