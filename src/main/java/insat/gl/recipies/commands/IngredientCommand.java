package insat.gl.recipies.commands;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IngredientCommand {
    private String id;
    private String recipeId;
    
    @NotBlank
    private String description;
    
    @NotNull
    @Min(1)
    private BigDecimal amount;
    
    @NotNull
    private UnitOfMeasureCommand uom;

}