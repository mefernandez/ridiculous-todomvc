package org.community.ridiculous.todomvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TodoMVCController {
	
	@RequestMapping("/")
	public String helloWorld() {
		return "index";
	}

}
