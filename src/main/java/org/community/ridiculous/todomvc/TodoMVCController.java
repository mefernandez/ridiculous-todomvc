package org.community.ridiculous.todomvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TodoMVCController {
	
	@Autowired
	ITodoRepository repository;

	@RequestMapping("/")
	public String listTodos() {
		return "index";
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String createTodo(Todo todo) {
		repository.save(todo);
		return "index";
	}
	
	@ModelAttribute("todos")
	public Iterable<Todo> todosModelAttribute() {
		return repository.findAll();
	}

}
