package insat.gl.recipies.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import insat.gl.recipies.domain.UnitOfMeasure;

public interface UnitOfMeasureRepository extends CrudRepository<UnitOfMeasure, String> {

	Optional<UnitOfMeasure> findByDescription(String description);
}
