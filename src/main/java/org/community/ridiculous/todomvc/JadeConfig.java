package org.community.ridiculous.todomvc;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.spring.template.SpringTemplateLoader;
import de.neuland.jade4j.spring.view.JadeViewResolver;

@Configuration
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class JadeConfig {
	
	@Autowired
	private final ResourceLoader resourceLoader = new DefaultResourceLoader();	

    @Bean
    public SpringTemplateLoader templateLoader() {
        SpringTemplateLoader templateLoader = new SpringTemplateLoader();
        templateLoader.setResourceLoader(resourceLoader);
        templateLoader.setBasePath("classpath:/templates/");
        templateLoader.setEncoding("UTF-8");
        templateLoader.setSuffix(".jade");
        return templateLoader;
    }

    @Bean
    public JadeConfiguration jadeConfiguration() {
        JadeConfiguration configuration = new JadeConfiguration();
        configuration.setCaching(false);
        configuration.setTemplateLoader(templateLoader());
        return configuration;
    }

    @Bean
    public JadeViewResolver jadeViewResolver() {
        JadeViewResolver viewResolver = new JadeViewResolver();
        viewResolver.setConfiguration(jadeConfiguration());
        //viewResolver.setOrder(Ordered.HIGHEST_PRECEDENCE);
		viewResolver.setContentType("text/html;charset=UTF-8");
        return viewResolver;
    }
    
	@PostConstruct
	public void checkTemplateLocationExists() {
			Resource resource = this.resourceLoader.getResource("classpath:/templates/");
			Assert.state(resource.exists(), "Cannot find template location: "
					+ resource + " (please add some templates "
					+ "or check your Jade configuration)");
	}

}
