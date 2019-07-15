package insat.gl.recipies.services;

import insat.gl.recipies.commands.RecipeCommand;
import insat.gl.recipies.domain.Recipe;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecipeService {
    Mono<Recipe> findById(String id);
    Flux<Recipe> getRecipes();
    Mono<RecipeCommand> findCommandById(String id);
    Mono<RecipeCommand>  saveRecipeCommand(RecipeCommand command);
    Mono<Void> deleteById(String idToDelete);

	
}
