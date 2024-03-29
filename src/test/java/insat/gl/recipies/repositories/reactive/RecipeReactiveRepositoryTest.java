package insat.gl.recipies.repositories.reactive;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import insat.gl.recipies.domain.Recipe;

@RunWith(SpringRunner.class)
@DataMongoTest
public class RecipeReactiveRepositoryTest {

	@Autowired
	RecipeReactiveRepository recipeReactiveRepository;
	
	@Before
	public void setUp() {
		recipeReactiveRepository.deleteAll().block();
	}
	
	@Test
	public void testRecipeSave() throws Exception{
		Recipe recipe =new Recipe();
		recipe.setDescription("Yummy");
		recipeReactiveRepository.save(recipe).block();
		
		Long count =recipeReactiveRepository.count().block();
		
		assertEquals(Long.valueOf(1L), count);
	}
	
}
