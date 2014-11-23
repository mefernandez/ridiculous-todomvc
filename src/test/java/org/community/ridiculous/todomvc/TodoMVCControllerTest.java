package org.community.ridiculous.todomvc;


import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.Serializable;
import java.lang.reflect.Field;

import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;

import org.hamcrest.Matchers;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.tuple.IdentifierProperty;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBootApplication.class)
@WebAppConfiguration
@Transactional
public class TodoMVCControllerTest {

	private final class ResetGenerator implements IdentifierGenerator {
		
		long nextValue = 1;

		@Override
		public Serializable generate(SessionImplementor session, Object object)
				throws HibernateException {
			return nextValue++;
		}
		
		public void reset() {
			nextValue = 1;
		}
	}

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;
	
	@Autowired
	private ITodoRepository repository;
	
	@Autowired
	EntityManagerFactory factory;
	
	ResetGenerator myGenerator = new ResetGenerator();
	
	@Before
	public void setup() throws Exception {
		/* Todo.class has a @GeneratedValue, which is fine for the app but not so good for tests,
		 * so let's swap the IdentifierGenerator for that class.
		 */
		SessionFactoryImplementor sessionFactory = this.factory.unwrap(SessionFactoryImplementor.class);
		EntityPersister persister = sessionFactory.getEntityPersister( Todo.class.getName() );
		IdentifierProperty identifierProperty = persister.getEntityMetamodel().getIdentifierProperty();
		/* Things get tough now, since I've found no way in Hibnerate's API to set a different Generator,
		 * so I'm using Reflection.
		 */
		Field f = identifierProperty.getClass().getDeclaredField("identifierGenerator");
		f.setAccessible(true);
		f.set(identifierProperty, myGenerator);

		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

		myGenerator.reset();
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
	public void shouldToggleAllTodos() throws Exception {
		this.mockMvc.perform(get("/toggle"))
        .andExpect(status().is3xxRedirection())
        .andExpect(header().string("Location", Matchers.startsWith("/?")))
        .andExpect(model().attribute("todos", hasItem(allOf(hasProperty("id", equalTo(1L)), hasProperty("completed", equalTo(true))))))
        .andExpect(model().attribute("todos", hasItem(allOf(hasProperty("id", equalTo(2L)), hasProperty("completed", equalTo(false))))));
	}
	
	@Test
	public void toggleAllTodosShouldUpdateTheView() throws Exception {
		MvcResult redirection = this.mockMvc.perform(get("/toggle"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlPattern("/?*"))
		.andReturn();
		
		this.mockMvc.perform(get(redirection.getResponse().getRedirectedUrl()))
		.andDo(print())
        .andExpect(xpath("//*[@data-id=1]//*[@class='toggle'][@type='checkbox'][@checked='checked']").nodeCount(1))		
        .andExpect(xpath("//*[@data-id=2]//*[@class='toggle'][@type='checkbox'][not(@checked)]").nodeCount(1));		
	}
	
	@Test
	public void shouldAddANewTodoToTheModel() throws Exception {
		MvcResult redirection = this.mockMvc.perform(post("/")
				.param("title", "New Todo"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"))
		.andReturn();

		this.mockMvc.perform(get(redirection.getResponse().getRedirectedUrl()))
		.andExpect(model().attribute("todos", hasSize(3)))
		.andExpect(model().attribute("todos", hasItem(hasProperty("title", equalTo("New Todo")))));
	}
	
	@Test
	public void shouldAddANewTodoToTheView() throws Exception {
		MvcResult redirection = this.mockMvc.perform(post("/")
				.param("title", "New Todo"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"))
		.andReturn();

		this.mockMvc.perform(get(redirection.getResponse().getRedirectedUrl()))
        .andExpect(xpath("//*[@id='todo-list']//label[contains(text(), 'New Todo')]").nodeCount(1));		
	}
	
	@Test
	public void shouldClearTheInputFieldWhenANewTodoIsAdded() throws Exception {
		MvcResult redirection = this.mockMvc.perform(post("/")
				.param("title", "New Todo"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"))
		.andReturn();

		this.mockMvc.perform(get(redirection.getResponse().getRedirectedUrl()))
        .andExpect(xpath("//*[@id='new-todo'][@value]").doesNotExist());		
	}

	@Test
	public void shouldRemoveAnEntryFromTheModel() throws Exception {
		MvcResult redirection = this.mockMvc.perform(delete("/1"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"))
		.andReturn();

		this.mockMvc.perform(get(redirection.getResponse().getRedirectedUrl()))
		.andExpect(model().attribute("todos", hasSize(1)));
	}

	@Test
	public void shouldRemoveAnEntryFromTheView() throws Exception {
		MvcResult redirection = this.mockMvc.perform(delete("/1"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"))
		.andReturn();

		this.mockMvc.perform(get(redirection.getResponse().getRedirectedUrl()))
        .andExpect(xpath("//*[@id='todo-list']//label").nodeCount(1));		
	}

	@Test
	public void shouldUpdateTheElementCount() throws Exception {
		MvcResult redirection = this.mockMvc.perform(delete("/1"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"))
		.andReturn();

		this.mockMvc.perform(get(redirection.getResponse().getRedirectedUrl()))
        .andExpect(xpath("//*[@id='todo-count']/strong/text()").string("1"));		
	}

	@Test
	public void shouldRemoveACompletedEntryFromTheModel() throws Exception {
		MvcResult redirection = this.mockMvc.perform(delete("/completed"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"))
		.andReturn();

		this.mockMvc.perform(get(redirection.getResponse().getRedirectedUrl()))
		.andExpect(model().attribute("todos", hasSize(1)));
	}

	@Test
	public void shouldRemoveACompletedEntryFromTheView() throws Exception {
		MvcResult redirection = this.mockMvc.perform(delete("/completed"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"))
		.andReturn();

		this.mockMvc.perform(get(redirection.getResponse().getRedirectedUrl()))
        .andExpect(xpath("//*[@id='todo-list']//label[contains(text(), 'Hola')]").doesNotExist());		
	}

	@Test
	public void elementCompleteToggleShouldUpdateTheModel() throws Exception {
		MvcResult redirection = this.mockMvc.perform(post("/1/toggle"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"))
		.andReturn();

		this.mockMvc.perform(get(redirection.getResponse().getRedirectedUrl()))
		.andExpect(model().attribute("todos", hasItem(allOf(hasProperty("id", equalTo(1L)), hasProperty("completed", is(true))))));
	}

	@Test
	public void elementCompleteToggleShouldUpdateTheView() throws Exception {
		MvcResult redirection = this.mockMvc.perform(post("/1/toggle"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"))
		.andReturn();

		this.mockMvc.perform(get(redirection.getResponse().getRedirectedUrl()))
        .andExpect(xpath("//*[@class='toggle'][@type='checkbox'][@checked='checked']").nodeCount(2));		

	}

	@Test
	@Ignore
	public void editItemShouldSwitchToEditMode() throws Exception {
		// TODO How to port this test? 
	}

	@Test
	@Ignore
	public void editItemShouldLeaveEditModeOnDone() throws Exception {
		// TODO How to port this test? 
	}
	
	@Test
	public void editItemShouldPersistTheChangesOnDone() throws Exception {
		MvcResult redirection = this.mockMvc.perform(post("/1")
				.param("title", "Edit Todo"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"))
		.andReturn();

		this.mockMvc.perform(get(redirection.getResponse().getRedirectedUrl()))
        .andExpect(xpath("//*[@id='todo-list']//label[contains(text(), 'Edit Todo')]").exists());
	}
	
	@Test
	public void shouldRemoveTheElementFromTheModelWhenPersistingAnEmptyTitle() throws Exception {
		MvcResult redirection = this.mockMvc.perform(post("/1")
				.param("title", ""))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"))
		.andReturn();

		this.mockMvc.perform(get(redirection.getResponse().getRedirectedUrl()))
        .andExpect(xpath("//*[@id='todo-list']//label").nodeCount(1));
	}
	
	@Test
	public void shouldRemoveTheElementFromTheViewWhenPersistingAnEmptyTitle() throws Exception {
		MvcResult redirection = this.mockMvc.perform(post("/1")
				.param("title", ""))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"))
		.andReturn();

		this.mockMvc.perform(get(redirection.getResponse().getRedirectedUrl()))
		.andExpect(model().attribute("todos", not(hasItem(hasProperty("id", equalTo(1L)))))); 
	}
	
	@Test
	@Ignore
	public void shouldLeaveEditModeOnCancel() throws Exception {
		// TODO How to port this test? 
	}

	@Test
	@Ignore
	public void shouldNotPersistTheChangesOnCancel() throws Exception {
		// TODO How to port this test? 
	}

}
