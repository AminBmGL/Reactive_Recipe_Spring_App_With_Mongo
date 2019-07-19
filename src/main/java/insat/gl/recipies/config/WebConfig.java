package insat.gl.recipies.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

import insat.gl.recipies.domain.Recipe;
import insat.gl.recipies.services.RecipeService;

@Configuration
public class WebConfig {
@Bean
public RouterFunction<?> routes(RecipeService recipeService){
return RouterFunctions.route(GET("/api/recipies"), 
		serverRequest -> ServerResponse
						.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(recipeService.getRecipes(),Recipe.class));
}
}

