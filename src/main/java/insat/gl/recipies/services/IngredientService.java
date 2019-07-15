package insat.gl.recipies.services;

import insat.gl.recipies.commands.IngredientCommand;

public interface IngredientService {

    IngredientCommand findByRecipeIdAndIngredientId(String recipeId, String ingredientId);
    IngredientCommand saveIngredientCommand(IngredientCommand command);
    void deleteById(String recipeId, String idToDelete);

}