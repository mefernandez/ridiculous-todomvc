package org.community.ridiculous.todomvc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TodoMVCController {
	
	List<Todo> todos = new ArrayList<Todo>();

	@RequestMapping("/")
	public String listTodos() {
		return "index";
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String createTodo(Todo todo) {
		this.todos.add(todo);
		return "index";
	}
	
	@ModelAttribute("todos")
	public List<Todo> todosModelAttribute() {
		return todos;
	}

}
