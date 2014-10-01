package com.payu.soa.example.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.AutowireCandidateResolver;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

public class MyBeanFactory extends DefaultListableBeanFactory implements AutowireCapableBeanFactory {
	
	private final DefaultListableBeanFactory wrapped = new DefaultListableBeanFactory();
	
	private AutowireCapableBeanFactory proxy  = (AutowireCapableBeanFactory) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),  new Class[]{AutowireCapableBeanFactory.class}, new InvocationHandler() {
		
		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			System.err.println("Called method " + method.getName() + " with args " + args);
			return method.invoke(wrapped, args);
		}
	});

	private MyAutowireCandidateResolver myAutowireCandidateResolver = new MyAutowireCandidateResolver(); {
		myAutowireCandidateResolver.setBeanFactory(this);
	}

//	
//	
//	public <T> T createBean(Class<T> beanClass) throws BeansException {
//		return wrapped.createBean(beanClass);
//	}
//
//	public void autowireBean(Object existingBean) throws BeansException {
//		wrapped.autowireBean(existingBean);
//	}
//
//	public Object configureBean(Object existingBean, String beanName)
//			throws BeansException {
//		return wrapped.configureBean(existingBean, beanName);
//	}
//
//	public Object getBean(String name) throws BeansException {
//		return wrapped.getBean(name);
//	}
//
//	public Object resolveDependency(DependencyDescriptor descriptor,
//			String beanName) throws BeansException {
//		return wrapped.resolveDependency(descriptor, beanName);
//	}
//
//	public <T> T getBean(String name, Class<T> requiredType)
//			throws BeansException {
//		return wrapped.getBean(name, requiredType);
//	}
//
//	public Object createBean(Class<?> beanClass, int autowireMode,
//			boolean dependencyCheck) throws BeansException {
//		return wrapped.createBean(beanClass, autowireMode, dependencyCheck);
//	}
//
//	public <T> T getBean(Class<T> requiredType) throws BeansException {
//		return wrapped.getBean(requiredType);
//	}
//
//	public Object autowire(Class<?> beanClass, int autowireMode,
//			boolean dependencyCheck) throws BeansException {
//		return wrapped.autowire(beanClass, autowireMode, dependencyCheck);
//	}
//
//	public Object getBean(String name, Object... args) throws BeansException {
//		return wrapped.getBean(name, args);
//	}
//
//	public void autowireBeanProperties(Object existingBean, int autowireMode,
//			boolean dependencyCheck) throws BeansException {
//		wrapped.autowireBeanProperties(existingBean, autowireMode,
//				dependencyCheck);
//	}
//
//	public boolean containsBean(String name) {
//		return wrapped.containsBean(name);
//	}
//
//	public boolean isSingleton(String name)
//			throws NoSuchBeanDefinitionException {
//		return wrapped.isSingleton(name);
//	}
//
//	public void applyBeanPropertyValues(Object existingBean, String beanName)
//			throws BeansException {
//		wrapped.applyBeanPropertyValues(existingBean, beanName);
//	}
//
//	public boolean isPrototype(String name)
//			throws NoSuchBeanDefinitionException {
//		return wrapped.isPrototype(name);
//	}
//
//	public Object initializeBean(Object existingBean, String beanName)
//			throws BeansException {
//		return wrapped.initializeBean(existingBean, beanName);
//	}
//
//	public boolean isTypeMatch(String name, Class<?> targetType)
//			throws NoSuchBeanDefinitionException {
//		return wrapped.isTypeMatch(name, targetType);
//	}
//
//	public Object applyBeanPostProcessorsBeforeInitialization(
//			Object existingBean, String beanName) throws BeansException {
//		return wrapped.applyBeanPostProcessorsBeforeInitialization(existingBean,
//				beanName);
//	}
//
//	public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
//		return wrapped.getType(name);
//	}
//
//	public Object applyBeanPostProcessorsAfterInitialization(
//			Object existingBean, String beanName) throws BeansException {
//		return wrapped.applyBeanPostProcessorsAfterInitialization(existingBean,
//				beanName);
//	}
//
//	public String[] getAliases(String name) {
//		return wrapped.getAliases(name);
//	}
//
//	public void destroyBean(Object existingBean) {
//		wrapped.destroyBean(existingBean);
//	}
//
//	public Object resolveDependency(DependencyDescriptor descriptor,
//			String beanName, Set<String> autowiredBeanNames,
//			TypeConverter typeConverter) throws BeansException {
//		return wrapped.resolveDependency(descriptor, beanName,
//				autowiredBeanNames, typeConverter);
//	}
//	
	
	
	public AutowireCandidateResolver getAutowireCandidateResolver() { 
		return myAutowireCandidateResolver;
	}
}
