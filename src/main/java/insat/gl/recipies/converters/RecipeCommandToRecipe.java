package insat.gl.recipies.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import insat.gl.recipies.commands.RecipeCommand;
import insat.gl.recipies.domain.Recipe;

@Component
public class RecipeCommandToRecipe implements Converter<RecipeCommand, Recipe> {

    private final CategoryCommandToCategory categoryCommandToCategory;
	private final NotesCommandToNotes notesCommandToNotes;
	private final IngredientCommandToIngredient ingredientCommandToIngredient;
	
	
	
	public RecipeCommandToRecipe(CategoryCommandToCategory categoryCommandToCategory,
			IngredientCommandToIngredient ingredientCommandToIngredient, NotesCommandToNotes notesCommandToNotes) {
		super();
		this.categoryCommandToCategory = categoryCommandToCategory;
		this.notesCommandToNotes = notesCommandToNotes;
		this.ingredientCommandToIngredient = ingredientCommandToIngredient;
	}



	@Override
	public Recipe convert(RecipeCommand source) {
		// TODO Auto-generated method stub
		if(source == null) {return null;}
		
		final Recipe recipe=new Recipe();
		recipe.setId(source.getId());
        recipe.setCookTime(source.getCookTime());
        recipe.setPrepTime(source.getPrepTime());
        recipe.setDescription(source.getDescription());
        recipe.setDifficulty(source.getDifficulty());
        recipe.setDirections(source.getDirections());
        recipe.setServings(source.getServings());
        recipe.setSource(source.getSource());
        recipe.setUrl(source.getUrl());
        recipe.setNotes(notesCommandToNotes.convert(source.getNotes()));
        
        if(source.getCategories() != null && source.getCategories().size()>0) {
        	source.getCategories()
        	.forEach(categoryCommand -> recipe.getCategories().add(categoryCommandToCategory.convert(categoryCommand)));
        }
        
        if(source.getIngredients() != null && source.getIngredients().size()>0) {
        	source.getIngredients()
        	.forEach(ingredientCommand -> recipe.getIngredients().add(ingredientCommandToIngredient.convert(ingredientCommand)));
        }
     
        return recipe;
	}
	

}
