package insat.gl.recipies.services;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import insat.gl.recipies.commands.UnitOfMeasureCommand;
import insat.gl.recipies.converters.UnitOfMeasureToUnitOfMeasureCommand;
import insat.gl.recipies.repositories.UnitOfMeasureRepository;
import insat.gl.recipies.repositories.reactive.UnitOfMeasureReactiveRepository;
import reactor.core.publisher.Flux;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

	 private final UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;
	 private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;
	 
	 public UnitOfMeasureServiceImpl(UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository,
			UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand) {
		super();
		this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
		this.unitOfMeasureToUnitOfMeasureCommand = unitOfMeasureToUnitOfMeasureCommand;
	}




	@Override
	public Flux<UnitOfMeasureCommand> listAllUoms() {
	   return unitOfMeasureReactiveRepository.findAll()
			   .map(unitOfMeasureToUnitOfMeasureCommand::convert);
	}

}
