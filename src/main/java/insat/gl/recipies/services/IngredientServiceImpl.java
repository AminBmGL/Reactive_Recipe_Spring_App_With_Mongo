package insat.gl.recipies.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import insat.gl.recipies.commands.IngredientCommand;
import insat.gl.recipies.converters.IngredientCommandToIngredient;
import insat.gl.recipies.converters.IngredientToIngredientCommand;
import insat.gl.recipies.domain.Ingredient;
import insat.gl.recipies.domain.Recipe;
import insat.gl.recipies.repositories.reactive.RecipeReactiveRepository;
import insat.gl.recipies.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

	   private final IngredientToIngredientCommand ingredientToIngredientCommand;
	   private final IngredientCommandToIngredient ingredientCommandToIngredient;
	   private final RecipeReactiveRepository recipeReactiveRepository;
	   private final UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

	public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand,
			IngredientCommandToIngredient ingredientCommandToIngredient,
			RecipeReactiveRepository recipeReactiveRepository,
			UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository) {
		this.ingredientToIngredientCommand = ingredientToIngredientCommand;
		this.ingredientCommandToIngredient = ingredientCommandToIngredient;
		this.recipeReactiveRepository = recipeReactiveRepository;
		this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
	}

	@Override
    public Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {

		 return recipeReactiveRepository
	                .findById(recipeId)
	                .flatMapIterable(Recipe::getIngredients)
	                .filter(ingredient -> ingredient.getId().equalsIgnoreCase(ingredientId))
	                .single()
	                .map(ingredient -> {
	                    IngredientCommand command = ingredientToIngredientCommand.convert(ingredient);
	                    command.setRecipeId(recipeId);
	                    return command;
	                });
   
    }

	 @Override
	    public Mono<IngredientCommand> saveIngredientCommand(IngredientCommand command) {
	        Recipe recipe = recipeReactiveRepository.findById(command.getRecipeId()).block();

	        if (recipe == null) {

	            //todo toss error if not found!
	            log.error("Recipe not found for id: " + command.getRecipeId());
	            return Mono.just(new IngredientCommand());
	        } else {

	            Optional<Ingredient> ingredientOptional = recipe
	                    .getIngredients()
	                    .stream()
	                    .filter(ingredient -> ingredient.getId().equals(command.getId()))
	                    .findFirst();

	            if(ingredientOptional.isPresent()){
	                Ingredient ingredientFound = ingredientOptional.get();
	                ingredientFound.setDescription(command.getDescription());
	                ingredientFound.setAmount(command.getAmount());
	                ingredientFound.setUom(unitOfMeasureReactiveRepository
	                        .findById(command.getUom().getId()).block());

	                //        .orElseThrow(() -> new RuntimeException("UOM NOT FOUND"))); //todo address this
	                if (ingredientFound.getUom() == null){
	                    new RuntimeException("UOM NOT FOUND");
	                }
	            } else {
	                //add new Ingredient
	                Ingredient ingredient = ingredientCommandToIngredient.convert(command);
	                recipe.addIngredient(ingredient);
	            }

	            Recipe savedRecipe = recipeReactiveRepository.save(recipe).block();

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

	            //todo check for fail

	            //enhance with id value
	            IngredientCommand ingredientCommandSaved = ingredientToIngredientCommand.convert(savedIngredientOptional.get());
	            ingredientCommandSaved.setRecipeId(recipe.getId());

	            return Mono.just(ingredientCommandSaved);
	        }

	    }
    
	@Override
    public Mono<Void> deleteById(String recipeId, String idToDelete) {

        log.debug("Deleting ingredient: " + recipeId + ":" + idToDelete);

        Recipe recipe = recipeReactiveRepository.findById(recipeId).block();

        if(recipe != null){

            log.debug("found recipe");

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(idToDelete))
                    .findFirst();

            if(ingredientOptional.isPresent()){
                log.debug("found Ingredient");

                recipe.getIngredients().remove(ingredientOptional.get());
                recipeReactiveRepository.save(recipe).block();
            }
        } else {
            log.debug("Recipe Id Not found. Id:" + recipeId);
        }
        return Mono.empty();
    }
    
}
