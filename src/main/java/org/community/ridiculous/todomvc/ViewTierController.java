package org.community.ridiculous.todomvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.AbstractCachingViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import de.neuland.jade4j.spring.view.JadeViewResolver;

/**
 * Sets which {@link ViewResolver} will render the View.
 * Reorders the {@link ViewResolver} list held in {@link ContentNegotiatingViewResolver}
 * so that it picks the selected {@link ViewResolver}.
 * 
 * @author mariano
 */
@Controller
public class ViewTierController {
	
	@Autowired
	JadeViewResolver jadeViewResolver;

	@Autowired
	ThymeleafViewResolver thymeleafViewResolver;

	@Autowired
	ContentNegotiatingViewResolver contentNegotiatingViewResolver;
	
	List<ViewResolver> viewResolverPrioritizedList = new ArrayList<ViewResolver>();
	
	@PostConstruct
	public void setupViewResolverPriority() {
		this.viewResolverPrioritizedList.add(jadeViewResolver);
		this.viewResolverPrioritizedList.add(thymeleafViewResolver);
	}

	@RequestMapping(value = "/thymeleaf")
	public String switchToThymeleaf() {
		ViewResolver viewResolver = thymeleafViewResolver;
		prioritizeViewResolver(viewResolver);
		return "redirect:/";
	}
	
	@RequestMapping(value = "/jade")
	public String switchToJade() {
		ViewResolver viewResolver = jadeViewResolver;
		prioritizeViewResolver(viewResolver);
		return "redirect:/";
	}

	private void prioritizeViewResolver(ViewResolver viewResolver) {
		int index = viewResolverPrioritizedList.indexOf(viewResolver);
		viewResolverPrioritizedList.remove(index);
		viewResolverPrioritizedList.add(0, viewResolver);
		contentNegotiatingViewResolver.setViewResolvers(viewResolverPrioritizedList);
	}
	
}
