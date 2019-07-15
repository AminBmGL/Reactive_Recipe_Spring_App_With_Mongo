package insat.gl.recipies.repositories.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import insat.gl.recipies.domain.Category;
import reactor.core.publisher.Mono;

public interface CategoryReactiveRepository extends ReactiveMongoRepository<Category, String> {

    Mono<Category> findByDescription(String description);
}