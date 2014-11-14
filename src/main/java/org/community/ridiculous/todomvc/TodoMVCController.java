package org.community.ridiculous.todomvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TodoMVCController {
	
	@Autowired
	ITodoRepository repository;

	@RequestMapping("/")
	public String listAllTodos(Model model) {
		Iterable<Todo> todos = repository.findAll();
		model.addAttribute("todos", todos);
		int countCompleted = countCompleted(todos);
		model.addAttribute("countCompleted", countCompleted);
		boolean allComplete = (countCompleted == repository.count());
		model.addAttribute("allComplete", allComplete);
		return "index";
	}

	private int countCompleted(Iterable<Todo> todos) {
		int countCompleted = 0;
		for (Todo todo: todos) {
			if (todo.isCompleted()) {
				countCompleted++;
			}
		}
		return countCompleted;
	}

	@RequestMapping("/active")
	public String listActiveTodos(Model model) {
		model.addAttribute("todos", repository.findByCompleted(false));
		return "index";
	}

	@RequestMapping("/completed")
	public String listCompletedTodos(Model model) {
		model.addAttribute("todos", repository.findByCompleted(true));
		return "index";
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String createTodo(Todo todo) {
		repository.save(todo);
		return "index";
	}
	
}
