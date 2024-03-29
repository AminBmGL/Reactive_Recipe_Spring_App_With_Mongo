package insat.gl.recipies.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import insat.gl.recipies.commands.IngredientCommand;
import insat.gl.recipies.commands.RecipeCommand;
import insat.gl.recipies.commands.UnitOfMeasureCommand;
import insat.gl.recipies.services.IngredientService;
import insat.gl.recipies.services.RecipeService;
import insat.gl.recipies.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Controller
public class IngredientController {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    
    private WebDataBinder webDataBinder;

	public IngredientController(RecipeService recipeService, IngredientService ingredientService,
			UnitOfMeasureService unitOfMeasureService) {
		super();
		this.recipeService = recipeService;
		this.ingredientService = ingredientService;
		this.unitOfMeasureService = unitOfMeasureService;
	}
	
	 @InitBinder("ingredient")
	    public void initBinder(WebDataBinder webDataBinder){
	        this.webDataBinder = webDataBinder;
	    }

    @GetMapping("/recipe/{recipeId}/ingredients")
    public String listIngredients(@PathVariable String recipeId, Model model){
        log.debug("Getting ingredient list for recipe id: " + recipeId);

        // use command object to avoid lazy load errors in Thymeleaf.
        model.addAttribute("recipe", recipeService.findCommandById(recipeId));

        return "recipe/ingredient/list";
    }
	
	@GetMapping("recipe/{recipeId}/ingredient/{id}/show")
    public String showRecipeIngredient(@PathVariable String recipeId,
                                       @PathVariable String id, Model model){
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, id));
        return "recipe/ingredient/show";
    }
	
	 @GetMapping("recipe/{recipeId}/ingredient/{id}/update")
	    public String updateRecipeIngredient(@PathVariable String recipeId,
	                                         @PathVariable String id, Model model){
	        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, id).block());
	        return "recipe/ingredient/ingredientform";
	    }

	    @PostMapping("recipe/{recipeId}/ingredient")
	    public String saveOrUpdate(@ModelAttribute("ingredient") IngredientCommand command, @PathVariable String recipeId,
                Model model){

			webDataBinder.validate();
			BindingResult bindingResult = webDataBinder.getBindingResult();
			
			if(bindingResult.hasErrors()){
			
			bindingResult.getAllErrors().forEach(objectError -> {
			 log.debug(objectError.toString());
			});
			
			return "recipe/ingredient/ingredientform";
			}
	        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command).block();

	        log.debug("saved receipe id:" + savedCommand.getRecipeId());
	        log.debug("saved ingredient id:" + savedCommand.getId());

	        return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show";
	    }
	    
	    @GetMapping("recipe/{recipeId}/ingredient/new")
	    public String newRecipeIngredient(@PathVariable String recipeId, Model model){

	        //make sure we have a good id value
	        RecipeCommand recipeCommand = recipeService.findCommandById(recipeId).block();
	        //todo raise exception if null

	        //need to return back parent id for hidden form property
	        IngredientCommand ingredientCommand = new IngredientCommand();
	        ingredientCommand.setRecipeId(recipeId);
	        model.addAttribute("ingredient", ingredientCommand);

	        //init uom
	        ingredientCommand.setUom(new UnitOfMeasureCommand());
	        return "recipe/ingredient/ingredientform";
	    }
	    
	    @GetMapping("recipe/{recipeId}/ingredient/{id}/delete")
	    public String deleteIngredient(@PathVariable String recipeId,
	                                   @PathVariable String id){

	        log.debug("deleting ingredient id:" + id);
	        ingredientService.deleteById(recipeId, id).block();

	        return "redirect:/recipe/" + recipeId + "/ingredients";
	    }
	    
	    @ModelAttribute("uomList")
	    public Flux<UnitOfMeasureCommand> populateUomList(){
	        return unitOfMeasureService.listAllUoms();
	    }

}
