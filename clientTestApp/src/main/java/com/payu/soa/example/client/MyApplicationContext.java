package com.payu.soa.example.client;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

public class MyApplicationContext  extends AnnotationConfigEmbeddedWebApplicationContext  implements ConfigurableApplicationContext {

	private static final String BEAN_FACTORY_FIELD_NAME = "beanFactory";

	public MyApplicationContext() {
		super();
		replaceBeanFactory();
		// TODO Auto-generated constructor stub
	}

	private void replaceBeanFactory() {
		try {
			Class c = this.getClass();
			while (c != null) {
				try {
					Field[] fields = c.getFields();
					Field f = c.getDeclaredField(BEAN_FACTORY_FIELD_NAME);
					f.setAccessible(true);
				     Field modifiersField = Field.class.getDeclaredField("modifiers");
				      modifiersField.setAccessible(true);
				      modifiersField.setInt(f,Modifier.PUBLIC);
					
					f.set(this, new MyBeanFactory());
					return;
				} catch (NoSuchFieldException e) {
				}
				c = c.getSuperclass();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public MyApplicationContext(Class<?>... annotatedClasses) {
		super(annotatedClasses);
		replaceBeanFactory();
		// TODO Auto-generated constructor stub
	}

	public MyApplicationContext(String... basePackages) {
		super(basePackages);
		replaceBeanFactory();		
		// TODO Auto-generated constructor stub
	}
	

	
	
	
	
}
