package insat.gl.recipies.services;


import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import insat.gl.recipies.commands.IngredientCommand;
import insat.gl.recipies.converters.IngredientCommandToIngredient;
import insat.gl.recipies.converters.IngredientToIngredientCommand;
import insat.gl.recipies.converters.UnitOfMeasureCommandToUnitOfMeasure;
import insat.gl.recipies.converters.UnitOfMeasureToUnitOfMeasureCommand;
import insat.gl.recipies.domain.Ingredient;
import insat.gl.recipies.domain.Recipe;
import insat.gl.recipies.repositories.RecipeRepository;
import insat.gl.recipies.repositories.UnitOfMeasureRepository;

public class IngredientServiceImplTest {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    IngredientService ingredientService;

    //init converters
    public IngredientServiceImplTest() {
        this.ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
        this.ingredientCommandToIngredient = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        ingredientService = new IngredientServiceImpl(ingredientToIngredientCommand, recipeRepository,
                ingredientCommandToIngredient, unitOfMeasureRepository);
    }

    @Test
    public void findByRecipeIdAndId() throws Exception {
    }

    @Test
    public void findByRecipeIdAndReceipeIdHappyPath() throws Exception {
        //given
        Recipe recipe = new Recipe();
        recipe.setId("123");

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId("456");

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId("789");

        Ingredient ingredient3 = new Ingredient();
        ingredient3.setId("987");

        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);
        recipe.addIngredient(ingredient3);
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(any())).thenReturn(recipeOptional);

        //then
        IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId("123", "456");

        //when
        assertEquals(String.valueOf("456"), ingredientCommand.getId());
        assertEquals(String.valueOf("123"), ingredientCommand.getRecipeId());
        verify(recipeRepository, times(1)).findById(any());
    }


    @Test
    public void testSaveRecipeCommand() throws Exception {
        //given
        IngredientCommand command = new IngredientCommand();
        command.setId("456");
        command.setRecipeId("123");

        Optional<Recipe> recipeOptional = Optional.of(new Recipe());

        Recipe savedRecipe = new Recipe();
        savedRecipe.addIngredient(new Ingredient());
        savedRecipe.getIngredients().iterator().next().setId("456");

        when(recipeRepository.findById(any())).thenReturn(recipeOptional);
        when(recipeRepository.save(any())).thenReturn(savedRecipe);

        //when
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);

        //then
        assertEquals(String.valueOf("456"), savedCommand.getId());
        verify(recipeRepository, times(1)).findById(any());
        verify(recipeRepository, times(1)).save(any(Recipe.class));

    }

    @Test
    public void testDeleteById() throws Exception {
        //given
        Recipe recipe = new Recipe();
        recipe.setId("123");
        Ingredient ingredient = new Ingredient();
        ingredient.setId("456");
        recipe.addIngredient(ingredient);
        ingredient.setRecipe(recipe);
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(any())).thenReturn(recipeOptional);

        //when
        ingredientService.deleteById("123", "456");

        //then
        verify(recipeRepository, times(1)).findById(any());
        verify(recipeRepository, times(1)).save(any(Recipe.class));
    }
}