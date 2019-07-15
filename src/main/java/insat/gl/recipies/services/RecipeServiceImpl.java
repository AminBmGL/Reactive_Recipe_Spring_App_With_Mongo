package insat.gl.recipies.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import insat.gl.recipies.commands.RecipeCommand;
import insat.gl.recipies.converters.RecipeCommandToRecipe;
import insat.gl.recipies.converters.RecipeToRecipeCommand;
import insat.gl.recipies.domain.Recipe;
import insat.gl.recipies.exceptions.NotFoundException;
import insat.gl.recipies.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@Primary
public class RecipeServiceImpl implements RecipeService {

	private final RecipeRepository recipeRepository;
	private final RecipeCommandToRecipe recipeCommandToRecipe;
	private final RecipeToRecipeCommand recipeToRecipeCommand;
	

	public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeCommandToRecipe recipeCommandToRecipe,
			RecipeToRecipeCommand recipeToRecipeCommand) {
		super();
		this.recipeRepository = recipeRepository;
		this.recipeCommandToRecipe = recipeCommandToRecipe;
		this.recipeToRecipeCommand = recipeToRecipeCommand;
	}
	

	@Override
	public Set<Recipe> getRecipes() {
		 Set<Recipe> recipeSet =new HashSet<>();
		 
		 recipeRepository.findAll().iterator().forEachRemaining(recipeSet::add);
		 return recipeSet;
	}


	@Override
	public Recipe findById(String l) {
		Optional<Recipe> recipeReturned =recipeRepository.findById(l);
		if (!recipeReturned.isPresent()) {
            throw new NotFoundException("Recipe Not Found for ID " + l.toString());
		}
		return recipeReturned.get();
	}

	 @Override
	    public RecipeCommand findCommandById(String id) {
	       RecipeCommand recipeCommand = recipeToRecipeCommand.convert(findById(id));

	        //enhance command object with id value
	        if(recipeCommand.getIngredients() != null && recipeCommand.getIngredients().size() > 0){
	            recipeCommand.getIngredients().forEach(rc -> {
	                rc.setRecipeId(recipeCommand.getId());
	            });
	        }

	        return recipeCommand;	   
	        
	 }


	@Override
	public RecipeCommand saveRecipeCommand(RecipeCommand command) {
		Recipe detachedRecipe = recipeCommandToRecipe.convert(command);
		Recipe savedRecipe = recipeRepository.save(detachedRecipe);
		
        log.debug("Saved RecipeId:" + savedRecipe.getId());
        return recipeToRecipeCommand.convert(savedRecipe);
	}
	
	@Override
    public void deleteById(String idToDelete) {
        recipeRepository.deleteById(idToDelete);
    }

}
