package insat.gl.recipies.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import insat.gl.recipies.commands.CategoryCommand;
import insat.gl.recipies.domain.Category;
import lombok.Synchronized;
@Component
public class CategoryCommandToCategory implements Converter<CategoryCommand, Category> {
    @Synchronized
    @Nullable
	@Override
	public Category convert(CategoryCommand source) {
		// TODO Auto-generated method stub
    	if(source==null) {
		return null;
    	}
    	final Category category =new Category();
    	category.setId(source.getId());
    	category.setDescription(source.getDescription());
    	return category;
	}

}
