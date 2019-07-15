package insat.gl.recipies.domain;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;


public class Ingredient {

    private String id=UUID.randomUUID().toString();
    private String description;
    private BigDecimal amount;

    @DBRef
    private UnitOfMeasure uom;

    private Recipe recipe;

    
      public Ingredient() {
		// TODO Auto-generated constructor stub
    	  } 


	public Ingredient(String description, BigDecimal amount, UnitOfMeasure uom) {
		super();
		this.description = description;
		this.amount = amount;
		this.uom = uom;
	}


	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }


	public UnitOfMeasure getUom() {
		return uom;
	}


	public void setUom(UnitOfMeasure uom) {
		this.uom = uom;
	}
    
}
