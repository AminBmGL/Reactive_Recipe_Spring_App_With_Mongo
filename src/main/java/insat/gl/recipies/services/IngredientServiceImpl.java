package insat.gl.recipies.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import insat.gl.recipies.commands.IngredientCommand;
import insat.gl.recipies.converters.IngredientCommandToIngredient;
import insat.gl.recipies.converters.IngredientToIngredientCommand;
import insat.gl.recipies.domain.Ingredient;
import insat.gl.recipies.domain.Recipe;
import insat.gl.recipies.repositories.RecipeRepository;
import insat.gl.recipies.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final RecipeRepository recipeRepository;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    

    public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand,
			RecipeRepository recipeRepository, IngredientCommandToIngredient ingredientCommandToIngredient,
			UnitOfMeasureRepository unitOfMeasureRepository) {
		super();
		this.ingredientToIngredientCommand = ingredientToIngredientCommand;
		this.recipeRepository = recipeRepository;
		this.ingredientCommandToIngredient = ingredientCommandToIngredient;
		this.unitOfMeasureRepository = unitOfMeasureRepository;
	}

	@Override
    public IngredientCommand findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {

        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if (!recipeOptional.isPresent()){
            //todo impl error handling
            log.error("recipe id not found. Id: " + recipeId);
        }

        Recipe recipe = recipeOptional.get();

        Optional<IngredientCommand> ingredientCommandOptional = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map( ingredient -> ingredientToIngredientCommand.convert(ingredient)).findFirst();

        if(!ingredientCommandOptional.isPresent()){
            //todo impl error handling
            log.error("Ingredient id not found: " + ingredientId);
        }
        
        //enhance command object with recipe id
        IngredientCommand ingredientCommand = ingredientCommandOptional.get();
        ingredientCommand.setRecipeId(recipe.getId());

        return ingredientCommandOptional.get();
    }

	@Override
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());

        if(!recipeOptional.isPresent()){

            //todo toss error if not found!
            log.error("Recipe not found for id: " + command.getRecipeId());
            return new IngredientCommand();
        } else {
            Recipe recipe = recipeOptional.get();

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(command.getId()))
                    .findFirst();

            if(ingredientOptional.isPresent()){
                Ingredient ingredientFound = ingredientOptional.get();
                ingredientFound.setDescription(command.getDescription());
                ingredientFound.setAmount(command.getAmount());
                ingredientFound.setUom(unitOfMeasureRepository
                        .findById(command.getUom().getId())
                        .orElseThrow(() -> new RuntimeException("UOM NOT FOUND"))); //todo address this
            } else {
                //add new Ingredient
                Ingredient ingredient = ingredientCommandToIngredient.convert(command);
                ingredient.setRecipe(recipe);
                recipe.addIngredient(ingredient);
            }

            Recipe savedRecipe = recipeRepository.save(recipe);

            Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
                    .filter(recipeIngredients -> recipeIngredients.getId().equals(command.getId()))
                    .findFirst();

            //check by description
            if(!savedIngredientOptional.isPresent()){
                //not totally safe... But best guess
                savedIngredientOptional = savedRecipe.getIngredients().stream()
                        .filter(recipeIngredients -> recipeIngredients.getDescription().equals(command.getDescription()))
                        .filter(recipeIngredients -> recipeIngredients.getAmount().equals(command.getAmount()))
                        .filter(recipeIngredients -> recipeIngredients.getUom().getId().equals(command.getUom().getId()))
                        .findFirst();
            }

            //to do check for fail
            
            //enhance with id value
            IngredientCommand ingredientCommandSaved = ingredientToIngredientCommand.convert(savedIngredientOptional.get());
            ingredientCommandSaved.setRecipeId(recipe.getId());

            return ingredientCommandSaved;     
            
        }

    }
    
	 @Override
	    public void deleteById(String recipeId, String idToDelete) {

	        log.debug("Deleting ingredient: " + recipeId + ":" + idToDelete);

	        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

	        if(recipeOptional.isPresent()){
	            Recipe recipe = recipeOptional.get();
	            log.debug("found recipe");

	            Optional<Ingredient> ingredientOptional = recipe
	                    .getIngredients()
	                    .stream()
	                    .filter(ingredient -> ingredient.getId().equals(idToDelete))
	                    .findFirst();

	            if(ingredientOptional.isPresent()){
	                log.debug("found Ingredient");
	                Ingredient ingredientToDelete = ingredientOptional.get();
	                ingredientToDelete.setRecipe(null);
	                recipe.getIngredients().remove(ingredientOptional.get());
	                recipeRepository.save(recipe);
	            }
	        } else {
	            log.debug("Recipe Id Not found. Id:" + recipeId);
	        }
	    }
    
}
