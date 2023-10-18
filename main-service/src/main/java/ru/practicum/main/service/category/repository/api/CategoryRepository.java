package ru.practicum.main.service.category.repository.api;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.service.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
