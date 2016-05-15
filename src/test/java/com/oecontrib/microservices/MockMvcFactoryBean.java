package com.oecontrib.microservices;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import java.util.Collections;
import java.util.List;

public class MockMvcFactoryBean implements FactoryBean<MockMvc>, ApplicationContextAware {
    private WebApplicationContext applicationContext;
    private final List<Filter> filters;

    public MockMvcFactoryBean() {
        this(Collections.<Filter>emptyList());
    }
    public MockMvcFactoryBean(List<Filter> filters) {
        this.filters = filters;
    }

    @Override public MockMvc getObject() {
        return MockMvcBuilders.webAppContextSetup(applicationContext)
                .addFilters(filters.toArray(new Filter[filters.size()])).build();
    }

    @Override public Class<?> getObjectType() {
        return MockMvc.class;
    }

    @Override public boolean isSingleton() {
        return true;
    }

    @Override public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (WebApplicationContext) applicationContext;
    }
}