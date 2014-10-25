package org.community.ridiculous.todomvc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TodoMVCController {
	
	@RequestMapping("/")
	public String listTodos() {
		return "index";
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String createTodo() {
		return "index";
	}
	
	@ModelAttribute("todos")
	public List<Todo> todosModelAttribute() {
		List<Todo> todos = new ArrayList<Todo>();
		todos.add(new Todo("Dummy", true));
		return todos;
	}

}
