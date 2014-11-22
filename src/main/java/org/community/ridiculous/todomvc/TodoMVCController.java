package org.community.ridiculous.todomvc;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TodoMVCController {
	
	@Autowired
	ITodoRepository repository;

	@RequestMapping(value = "/")
	public String listAllTodos(Model model) {
		Iterable<Todo> todos = repository.findAll();
		addModelAttributes(model, todos);
		model.addAttribute("filter", "All");
		return "index";
	}

	@RequestMapping("/active")
	public String listActiveTodos(Model model) {
		Iterable<Todo> todos = repository.findByCompleted(false);
		addModelAttributes(model, todos);
		model.addAttribute("filter", "Active");
		return "index";
	}

	@RequestMapping("/completed")
	public String listCompletedTodos(Model model) {
		Iterable<Todo> todos = repository.findByCompleted(true);
		addModelAttributes(model, todos);
		model.addAttribute("filter", "Completed");
		return "index";
	}

	@RequestMapping("/toggle")
	public String toggleAllTodos(Model model) {
		Iterable<Todo> todos = repository.findAll();
		for (Todo todo : todos) {
			todo.setCompleted(true);
			repository.save(todo);
		}
		addModelAttributes(model, todos);
		return "redirect:/";
	}

	private void addModelAttributes(Model model, Iterable<Todo> todos) {
		model.addAttribute("todos", todos);
		int countCompleted = countCompleted(todos);
		model.addAttribute("countCompleted", countCompleted);
		boolean allComplete = (countCompleted == repository.count());
		model.addAttribute("allComplete", allComplete);
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

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String createTodo(Todo todo) {
		repository.save(todo);
		return "redirect:/";
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String deleteTodo(@PathVariable("id") Long id) {
		repository.delete(id);
		return "redirect:/";
	}
	
	@RequestMapping(value = "/completed", method = RequestMethod.DELETE)
	public String deleteCompleted() {
		Iterable<Todo> completed = repository.findByCompleted(true);
		repository.delete(completed);
		return "redirect:/";
	}
	
	@RequestMapping(value = "/{id}/toggle", method = RequestMethod.POST)
	public String toggleTodo(@PathVariable("id") Long id) {
		Todo todo = repository.findOne(id);
		boolean completed = todo.isCompleted();
		todo.setCompleted(!completed);
		repository.save(todo);
		return "redirect:/";
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String editTodo(@PathVariable("id") Long id, @RequestParam("title") String title) {
		if (title.isEmpty()) {
			repository.delete(id);
		} else {
			Todo todo = repository.findOne(id);
			todo.setTitle(title);
			repository.save(todo);
		}
		return "redirect:/";
	}
	
}
