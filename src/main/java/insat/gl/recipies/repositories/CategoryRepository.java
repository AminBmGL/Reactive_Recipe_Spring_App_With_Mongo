package insat.gl.recipies.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import insat.gl.recipies.domain.Category;

public interface CategoryRepository extends CrudRepository<Category, String> {
	Optional<Category> findByDescription(String description);

}
