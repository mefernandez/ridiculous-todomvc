package org.community.ridiculous.todomvc;

import org.springframework.data.repository.CrudRepository;

public interface ITodoRepository extends CrudRepository<Todo, Long> {

	Iterable<Todo> findByCompleted(boolean completed);

}
