package insat.gl.recipies.services;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import insat.gl.recipies.commands.RecipeCommand;
import insat.gl.recipies.converters.RecipeCommandToRecipe;
import insat.gl.recipies.converters.RecipeToRecipeCommand;
import insat.gl.recipies.domain.Recipe;
import insat.gl.recipies.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Slf4j
@Service
@Primary
public class RecipeServiceImpl implements RecipeService {

	private final RecipeReactiveRepository recipeReactiveRepository;
	private final RecipeCommandToRecipe recipeCommandToRecipe;
	private final RecipeToRecipeCommand recipeToRecipeCommand;
	

	public RecipeServiceImpl(RecipeReactiveRepository recipeReactiveRepository, RecipeCommandToRecipe recipeCommandToRecipe,
			RecipeToRecipeCommand recipeToRecipeCommand) {
		super();
		this.recipeReactiveRepository = recipeReactiveRepository;
		this.recipeCommandToRecipe = recipeCommandToRecipe;
		this.recipeToRecipeCommand = recipeToRecipeCommand;
	}
	

	@Override
	public Flux<Recipe> getRecipes() {
		 
		 return recipeReactiveRepository.findAll();
	}


	@Override
	public Mono<Recipe> findById(String l) {
	return recipeReactiveRepository.findById(l);
	}

	 @Override
	    public Mono<RecipeCommand> findCommandById(String id) {
		 
		 return recipeReactiveRepository.findById(id)
				 .map(recipe ->{
					 RecipeCommand command=recipeToRecipeCommand.convert(recipe);
					 command.getIngredients().forEach(rc -> {
	                        rc.setRecipeId(command.getId());
	                    });
					 
					 return command;
					 
				 });
				 	        
	 }


	@Override
	public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand command) {
		
		 return recipeReactiveRepository.save(recipeCommandToRecipe.convert(command))
	                .map(recipeToRecipeCommand::convert);
	    }

	
	@Override
    public Mono<Void> deleteById(String idToDelete) {
        recipeReactiveRepository.deleteById(idToDelete).block();
        return Mono.empty();
        
    }

}
