package org.community.ridiculous.todomvc;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Todo {
	
	String title;
	boolean completed = false;
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	Long id;

	public Todo() {
		
	}

	public Todo(String title, boolean completed) {
		this.title = title;
		this.completed = completed;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
