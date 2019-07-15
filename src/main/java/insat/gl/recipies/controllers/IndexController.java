package insat.gl.recipies.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import insat.gl.recipies.services.RecipeService;

@Controller
public class IndexController {
	
	RecipeService recipeService;
	
	
	public IndexController(RecipeService recipeService) {
		this.recipeService = recipeService;
	}


	@RequestMapping({"","/","/index"})
	public String getIndexPage(Model model) {
        model.addAttribute("recipies",recipeService.getRecipes());
		return "index";
		
	}
}
