package insat.gl.recipies.repositories.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import insat.gl.recipies.domain.UnitOfMeasure;
import reactor.core.publisher.Mono;

public interface UnitOfMeasureReactiveRepository extends ReactiveMongoRepository<UnitOfMeasure, String> {
Mono<UnitOfMeasure> findByDescription(String description);
}
