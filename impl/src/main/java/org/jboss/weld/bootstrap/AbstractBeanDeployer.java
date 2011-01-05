/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.weld.bootstrap;

import static org.jboss.weld.logging.Category.BOOTSTRAP;
import static org.jboss.weld.logging.LoggerFactory.loggerFactory;
import static org.jboss.weld.logging.messages.BootstrapMessage.FOUND_BEAN;
import static org.jboss.weld.logging.messages.BootstrapMessage.FOUND_DECORATOR;
import static org.jboss.weld.logging.messages.BootstrapMessage.FOUND_INTERCEPTOR;
import static org.jboss.weld.logging.messages.BootstrapMessage.FOUND_OBSERVER_METHOD;

import java.lang.reflect.Member;
import java.util.Set;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;

import org.jboss.weld.bean.AbstractClassBean;
import org.jboss.weld.bean.AbstractProducerBean;
import org.jboss.weld.bean.DecoratorImpl;
import org.jboss.weld.bean.DisposalMethod;
import org.jboss.weld.bean.InterceptorImpl;
import org.jboss.weld.bean.ManagedBean;
import org.jboss.weld.bean.NewBean;
import org.jboss.weld.bean.NewManagedBean;
import org.jboss.weld.bean.NewSessionBean;
import org.jboss.weld.bean.ProducerField;
import org.jboss.weld.bean.ProducerMethod;
import org.jboss.weld.bean.RIBean;
import org.jboss.weld.bean.SessionBean;
import org.jboss.weld.bean.builtin.ee.EEResourceProducerField;
import org.jboss.weld.bootstrap.api.ServiceRegistry;
import org.jboss.weld.bootstrap.events.ProcessBeanImpl;
import org.jboss.weld.bootstrap.events.ProcessBeanInjectionTarget;
import org.jboss.weld.bootstrap.events.ProcessManagedBeanImpl;
import org.jboss.weld.bootstrap.events.ProcessObserverMethodImpl;
import org.jboss.weld.bootstrap.events.ProcessProducerFieldImpl;
import org.jboss.weld.bootstrap.events.ProcessProducerImpl;
import org.jboss.weld.bootstrap.events.ProcessProducerMethodImpl;
import org.jboss.weld.bootstrap.events.ProcessSessionBeanImpl;
import org.jboss.weld.ejb.EJBApiAbstraction;
import org.jboss.weld.ejb.InternalEjbDescriptor;
import org.jboss.weld.event.ObserverFactory;
import org.jboss.weld.event.ObserverMethodImpl;
import org.jboss.weld.introspector.WeldClass;
import org.jboss.weld.introspector.WeldField;
import org.jboss.weld.introspector.WeldMethod;
import org.jboss.weld.manager.BeanManagerImpl;
import org.jboss.weld.persistence.PersistenceApiAbstraction;
import org.jboss.weld.util.Beans;
import org.jboss.weld.util.reflection.Reflections;
import org.jboss.weld.ws.WSApiAbstraction;
import org.slf4j.cal10n.LocLogger;

public class AbstractBeanDeployer<E extends BeanDeployerEnvironment>
{
   
   private static final LocLogger log = loggerFactory().getLogger(BOOTSTRAP);
   
   private final BeanManagerImpl manager;
   private final ServiceRegistry services;
   private final E environment;
   
   public AbstractBeanDeployer(BeanManagerImpl manager, ServiceRegistry services, E environment)
   {
      this.manager = manager;
      this.services = services;
      this.environment = environment;
   }
   
   protected BeanManagerImpl getManager()
   {
      return manager;
   }
   
   public AbstractBeanDeployer<E> deploy()
   {
      Set<? extends RIBean<?>> beans = getEnvironment().getBeans();
      // ensure that all decorators are initialized before initializing 
      // the rest of the beans
      for (DecoratorImpl<?> bean : getEnvironment().getDecorators())
      {
         bean.initialize(getEnvironment());
         ProcessBeanImpl.fire(getManager(), bean);
         manager.addDecorator(bean);
         log.debug(FOUND_DECORATOR, bean);
      }
      for (InterceptorImpl<?> bean: getEnvironment().getInterceptors())
      {
         bean.initialize(getEnvironment());
         ProcessBeanImpl.fire(getManager(), bean);
         manager.addInterceptor(bean);
         log.debug(FOUND_INTERCEPTOR, bean);
      }
      for (RIBean<?> bean : beans)
      {
         bean.initialize(getEnvironment());
         if (!(bean instanceof NewBean))
         {
            if (bean instanceof AbstractProducerBean<?, ?, ?>)
            {
               ProcessProducerImpl.fire(manager, Reflections.<AbstractProducerBean<?, ?, Member>>cast(bean));
            }
            else if (bean instanceof AbstractClassBean<?>)
            {
               ProcessBeanInjectionTarget.fire(manager, (AbstractClassBean<?>) bean);
            }
            if (bean instanceof ManagedBean<?>)
            {
               ProcessManagedBeanImpl.fire(manager, (ManagedBean<?>) bean);
            }
            else if (bean instanceof SessionBean<?>)
            {
               ProcessSessionBeanImpl.fire(manager, Reflections.<SessionBean<Object>>cast(bean));
            }
            else if (bean instanceof ProducerField<?, ?>)
            {
               ProcessProducerFieldImpl.fire(manager, (ProducerField<?, ?>) bean);
            }
            else if (bean instanceof ProducerMethod<?, ?>)
            {
               ProcessProducerMethodImpl.fire(manager, (ProducerMethod<?, ?>) bean);
            }
            else
            {
               ProcessBeanImpl.fire(getManager(), bean);
            }
         }
         manager.addBean(bean);
         log.debug(FOUND_BEAN, bean);
      }
      for (ObserverMethodImpl<?, ?> observer : getEnvironment().getObservers())
      {
         log.debug(FOUND_OBSERVER_METHOD, observer);
         observer.initialize();
         ProcessObserverMethodImpl.fire(manager, observer);
         manager.addObserver(observer);
      }
      return this;
   }

   /**
    * Creates the sub bean for an class (simple or enterprise) bean
    * 
    * @param bean
    *           The class bean
    * 
    */
   protected <T> void createObserversProducersDisposers(AbstractClassBean<T> bean)
   {
      createProducerMethods(bean, bean.getWeldAnnotated());
      createProducerFields(bean, bean.getWeldAnnotated());
      if (manager.isBeanEnabled(bean))
      {
         createObserverMethods(bean, bean.getWeldAnnotated());
      }
      createDisposalMethods(bean, bean.getWeldAnnotated());
      
   }
   
   protected <X> void createProducerMethods(AbstractClassBean<X> declaringBean, WeldClass<X> annotatedClass)
   {
      for (WeldMethod<?, ? super X> method : annotatedClass.getDeclaredWeldMethods(Produces.class))
      {
         createProducerMethod(declaringBean, method);         
      }
   }
   
   protected <X> void createDisposalMethods(AbstractClassBean<X> declaringBean, WeldClass<X> annotatedClass)
   {
      for (WeldMethod<?, ? super X> method : annotatedClass.getDeclaredWeldMethodsWithAnnotatedParameters(Disposes.class))
      {
         DisposalMethod<? super X, ?> disposalBean = DisposalMethod.of(manager, method, declaringBean, services);
         disposalBean.initialize(getEnvironment());
         getEnvironment().addDisposesMethod(disposalBean);
      }
   }
   
   protected <X, T> void createProducerMethod(AbstractClassBean<X> declaringBean, WeldMethod<T, ? super X> annotatedMethod)
   {
      ProducerMethod<? super X, T> bean = ProducerMethod.of(annotatedMethod, declaringBean, manager, services);
      getEnvironment().addProducerMethod(bean);
   }
   
   protected <X, T> void createProducerField(AbstractClassBean<X> declaringBean, WeldField<T, ? super X> field)
   {
      ProducerField<X, T> bean;
      if (isEEResourceProducerField(field))
      {
         bean = EEResourceProducerField.of(field, declaringBean, manager, services);
      }
      else
      {
         bean = ProducerField.of(field, declaringBean, manager, services);
      }
      getEnvironment().addProducerField(bean);
   }
   
   protected <X> void createProducerFields(AbstractClassBean<X> declaringBean, WeldClass<X> annotatedClass)
   {
      for (WeldField<?, ? super X> field : annotatedClass.getDeclaredWeldFields(Produces.class))
      {
         createProducerField(declaringBean, field);
      }
   }
   
   protected <X> void createObserverMethods(RIBean<X> declaringBean, WeldClass<? super X> annotatedClass)
   {
	   for (WeldMethod<?, ? super X> method : Beans.getObserverMethods(annotatedClass))
	   {
         createObserverMethod(declaringBean, method);
	   }
   }
   
   protected <T, X> void createObserverMethod(RIBean<X> declaringBean, WeldMethod<T, ? super X> method)
   {
      ObserverMethodImpl<T, ? super X> observer = ObserverFactory.create(method, declaringBean, manager);
      getEnvironment().addObserverMethod(observer);
   }

   protected <T> ManagedBean<T> createManagedBean(WeldClass<T> annotatedClass)
   {
      ManagedBean<T> bean = ManagedBean.of(annotatedClass, manager, services);
      getEnvironment().addManagedBean(bean);
      createObserversProducersDisposers(bean);
      return bean;
   }
   
   protected <T> void createNewManagedBean(WeldClass<T> annotatedClass)
   {
      getEnvironment().addManagedBean(NewManagedBean.of(annotatedClass, manager, services));
   }
   
   protected <T> void createDecorator(WeldClass<T> annotatedClass)
   {
      DecoratorImpl<T> bean = DecoratorImpl.of(annotatedClass, manager, services);
      getEnvironment().addDecorator(bean);
   }

   protected <T> void createInterceptor(WeldClass<T> annotatedClass)
   {
      InterceptorImpl<T> bean = InterceptorImpl.of(annotatedClass, manager, services);
      getEnvironment().addInterceptor(bean);
   }
   
   protected <T> SessionBean<T> createSessionBean(InternalEjbDescriptor<T> ejbDescriptor)
   {
      // TODO Don't create enterprise bean if it has no local interfaces!
      SessionBean<T> bean = SessionBean.of(ejbDescriptor, manager, services);
      getEnvironment().addSessionBean(bean);
      createObserversProducersDisposers(bean);
      return bean;
   }
   
   protected <T> SessionBean<T> createSessionBean(InternalEjbDescriptor<T> ejbDescriptor, WeldClass<T> weldClass)
   {
      // TODO Don't create enterprise bean if it has no local interfaces!
      SessionBean<T> bean = SessionBean.of(ejbDescriptor, manager, weldClass, services);
      getEnvironment().addSessionBean(bean);
      createObserversProducersDisposers(bean);
      return bean;
   }

   protected <T> void createNewSessionBean(InternalEjbDescriptor<T> ejbDescriptor)
   {
      getEnvironment().addSessionBean(NewSessionBean.of(ejbDescriptor, manager, services));
   }
   
   /**
    * Indicates if the type is a simple Web Bean
    * 
    * @param type
    *           The type to inspect
    * @return True if simple Web Bean, false otherwise
    */
   protected boolean isTypeManagedBeanOrDecoratorOrInterceptor(WeldClass<?> clazz)
   {
      Class<?> javaClass = clazz.getJavaClass();
      return !Extension.class.isAssignableFrom(clazz.getJavaClass()) &&
             !(clazz.isAnonymousClass() || (clazz.isMemberClass() && !clazz.isStatic())) &&
             !Reflections.isParamerterizedTypeWithWildcard(javaClass) &&
             hasSimpleWebBeanConstructor(clazz);
   }
   
   protected boolean isEEResourceProducerField(WeldField<?, ?> field)
   {
      EJBApiAbstraction ejbApiAbstraction = manager.getServices().get(EJBApiAbstraction.class);
      PersistenceApiAbstraction persistenceApiAbstraction = manager.getServices().get(PersistenceApiAbstraction.class);
      WSApiAbstraction wsApiAbstraction = manager.getServices().get(WSApiAbstraction.class);
      return field.isAnnotationPresent(ejbApiAbstraction.EJB_ANNOTATION_CLASS) || field.isAnnotationPresent(ejbApiAbstraction.RESOURCE_ANNOTATION_CLASS) || field.isAnnotationPresent(persistenceApiAbstraction.PERSISTENCE_UNIT_ANNOTATION_CLASS) || field.isAnnotationPresent(persistenceApiAbstraction.PERSISTENCE_CONTEXT_ANNOTATION_CLASS) || field.isAnnotationPresent(wsApiAbstraction.WEB_SERVICE_REF_ANNOTATION_CLASS); 
   }
   
   private static boolean hasSimpleWebBeanConstructor(WeldClass<?> type)
   {
      return type.getNoArgsWeldConstructor() != null || type.getWeldConstructors(Inject.class).size() > 0;
   }
      
   public E getEnvironment()
   {
      return environment;
   }
   
}
