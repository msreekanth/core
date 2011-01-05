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
package org.jboss.weld.bean;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.Dependent;

import org.jboss.weld.bootstrap.api.ServiceRegistry;
import org.jboss.weld.ejb.InternalEjbDescriptor;
import org.jboss.weld.introspector.WeldClass;
import org.jboss.weld.literal.NewLiteral;
import org.jboss.weld.manager.BeanManagerImpl;
import org.jboss.weld.resources.ClassTransformer;

/**
 * Represents a @New enterprise bean
 * 
 * @author Nicklas Karlsson
 */
public class NewSessionBean<T> extends SessionBean<T> implements NewBean
{

   /**
    * Creates an instance of a NewEnterpriseBean from an annotated class
    * 
    * @param clazz The annotated class
    * @param beanManager The Bean manager
    * @return a new NewEnterpriseBean instance
    */
   public static <T> NewSessionBean<T> of(InternalEjbDescriptor<T> ejbDescriptor, BeanManagerImpl beanManager, ServiceRegistry services)
   {
      WeldClass<T> type = beanManager.getServices().get(ClassTransformer.class).loadClass(ejbDescriptor.getBeanClass());
      return new NewSessionBean<T>(type, ejbDescriptor, createId(NewSessionBean.class.getSimpleName(), ejbDescriptor), beanManager, services);
   }
   
   private Set<Annotation> bindings;

   /**
    * Protected constructor
    * 
    * @param type An annotated class
    * @param beanManager The Bean manager
    */
   protected NewSessionBean(final WeldClass<T> type, InternalEjbDescriptor<T> ejbDescriptor, String idSuffix, BeanManagerImpl beanManager, ServiceRegistry services)
   {
      super(type, ejbDescriptor, idSuffix, beanManager, services);
      this.bindings = new HashSet<Annotation>();
      this.bindings.add(new NewLiteral()
      {
         
         private static final long serialVersionUID = 3953682907943246693L;

         @Override
         public Class<?> value()
         {
            return type.getJavaClass();
         }
         
      });
   }

   /**
    * Gets the scope type
    * 
    * @return @Dependent
    */
   @Override
   public Class<? extends Annotation> getScope()
   {
      return Dependent.class;
   }

   @Override
   public boolean isAlternative()
   {
      return false;
   }

   /**
    * Gets the name of the bean
    * 
    * @return null
    */
   @Override
   public String getName()
   {
      return null;
   }

   /**
    * Gets the bindings
    * 
    * @returns @New
    */
   @Override
   public Set<Annotation> getQualifiers()
   {
      return bindings;
   }
   
   @Override
   public boolean isSpecializing()
   {
      return false;
   }
   
   @Override
   protected void checkScopeAllowed()
   {
      // No-op
   }

   @Override
   protected void registerInterceptors()
   {
      // No - op
   }
   
   @Override
   public String toString()
   {
      return "@New " + super.toString();
   }
}
