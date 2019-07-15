package insat.gl.recipies.repositories.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import insat.gl.recipies.domain.Recipe;

public interface RecipeReactiveRepository extends ReactiveMongoRepository<Recipe, String>{
}
