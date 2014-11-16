package org.community.ridiculous.todomvc;


import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBootApplication.class)
@WebAppConfiguration
@Transactional
public class TodoMVCControllerTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;
	
	@Autowired
	private ITodoRepository repository;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		repository.save(new Todo("New Active Todo", false));
		repository.save(new Todo("New Completed Todo", true));
	}

	@Test
	public void showEntriesOnStartup() throws Exception {
		this.mockMvc.perform(get("/"))
	        .andExpect(status().isOk())
	        .andExpect(model().attributeExists("todos"));		
	}

	@Test
	public void shouldShowAllEntriesWithoutARoute() throws Exception {
		this.mockMvc.perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("todos", hasSize(2)));		
	}
	
	@Test
	public void shouldShowAllEntriesWithoutAllRoute() throws Exception {
		this.mockMvc.perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("todos", hasSize(2)));		
	}

	@Test
	public void shouldShowActiveEntries() throws Exception {
		this.mockMvc.perform(get("/active"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("todos", hasSize(1)));		
	}
	
	@Test
	public void shouldShowCompletedEntries() throws Exception {
		this.mockMvc.perform(get("/completed"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("todos", hasSize(1)));		
	}
	
	@Test
	public void shouldShowTheContentBlockWhenTodosExists() throws Exception {
		this.mockMvc.perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(xpath("//*[@id='main'][@style='display: block']").exists());		
	}
	
	@Test
	public void shouldHideTheContentBlockWhenNoTodosExists() throws Exception {
		repository.deleteAll();
		this.mockMvc.perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(xpath("//*[@id='main'][@style='display: none']").exists());		
	}	
	
	@Test
	public void shouldCheckTheToggleAllButtonIfAllTodosAreCompleted() throws Exception {
		for (Todo todo : repository.findAll()) {
			todo.setCompleted(true);
			repository.save(todo);
		}
		this.mockMvc.perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(xpath("//*[@id='toggle-all'][@checked='checked']").exists());		
	}

	@Test
	public void shouldSetTheClearCompletedButton() throws Exception {
		this.mockMvc.perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(xpath("//*[@id='clear-completed']/text()").string("Clear completed (1)"));		
	}
	
	@Test
	public void shouldHighlightAllFilterByDefault() throws Exception {
		this.mockMvc.perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(xpath("//*[@id='filters']/li/a[@class='selected']/text()").string("All"));		
	}

	@Test
	public void shouldHighlightActiveFilterWhenSwitchingToActiveView() throws Exception {
		this.mockMvc.perform(get("/active"))
        .andExpect(status().isOk())
        .andExpect(xpath("//*[@id='filters']/li/a[@class='selected']/text()").string("Active"));		
	}
	
	@Test
	public void shouldToggleAllTodosToCompleted() throws Exception {
		this.mockMvc.perform(get("/toggle"))
        .andExpect(status().is3xxRedirection())
        .andExpect(header().string("Location", startsWith("/?")))
        .andExpect(model().attribute("todos", everyItem(hasProperty("completed", is(true)))));
	}
	
	@Test
	public void shouldUpdateTheView() throws Exception {
		MvcResult redirection = this.mockMvc.perform(get("/toggle"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlPattern("/?*"))
		.andReturn();
		
		this.mockMvc.perform(get(redirection.getResponse().getRedirectedUrl()))
        .andExpect(xpath("//*[@class='toggle'][@type='checkbox'][@checked='checked']").nodeCount(2));		
	}
	
	@Test
	public void shouldAddANewTodoToTheModel() throws Exception {
		MvcResult redirection = this.mockMvc.perform(post("/")
				.param("title", "New Todo"))
		.andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"))
		.andReturn();

		this.mockMvc.perform(get(redirection.getResponse().getRedirectedUrl()))
		.andExpect(model().attribute("todos", hasSize(3)))
		.andExpect(model().attribute("todos", hasItem(hasProperty("title", equalTo("New Todo")))));
	}	
		
}
