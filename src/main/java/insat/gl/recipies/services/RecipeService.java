package insat.gl.recipies.services;

import java.util.Set;

import insat.gl.recipies.commands.RecipeCommand;
import insat.gl.recipies.domain.Recipe;

public interface RecipeService {
	Recipe findById(String l);
	Set<Recipe> getRecipes();
    RecipeCommand findCommandById(String l);
	RecipeCommand saveRecipeCommand(RecipeCommand command);
    void deleteById(String idToDelete);

	
}
