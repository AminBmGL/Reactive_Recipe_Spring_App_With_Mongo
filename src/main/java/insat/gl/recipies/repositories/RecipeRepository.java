package insat.gl.recipies.repositories;

import org.springframework.data.repository.CrudRepository;

import insat.gl.recipies.domain.Recipe;

public interface RecipeRepository extends CrudRepository<Recipe, String> {

}
