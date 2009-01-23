/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.webbeans.introspector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import org.jboss.webbeans.ManagerImpl;

public abstract class ForwardingAnnotatedMethod<T> extends ForwardingAnnotatedMember<T, Method> implements AnnotatedMethod<T>
{
   
   @Override
   protected abstract AnnotatedMethod<T> delegate();
   
   public Method getAnnotatedMethod()
   {
      return delegate().getAnnotatedMethod();
   }
   
   public List<AnnotatedParameter<?>> getAnnotatedParameters(Class<? extends Annotation> metaAnnotationType)
   {
      return delegate().getAnnotatedParameters(metaAnnotationType);
   }

   public AnnotatedType<?> getDeclaringClass()
   {
      return delegate().getDeclaringClass();
   }

   public Class<?>[] getParameterTypesAsArray()
   {
      return delegate().getParameterTypesAsArray();
   }

   public List<AnnotatedParameter<?>> getParameters()
   {
      return delegate().getParameters();
   }

   public String getPropertyName()
   {
      return delegate().getPropertyName();
   }

   public T invoke(Object instance, ManagerImpl manager)
   {
      return delegate().invoke(instance, manager);
   }

   public T invoke(Object instance, Object... parameters)
   {
      return delegate().invoke(instance, parameters);
   }

   public T invokeOnInstance(Object instance, ManagerImpl manager)
   {
      return delegate().invokeOnInstance(instance, manager);
   }

   public T invokeWithSpecialValue(Object instance, Class<? extends Annotation> specialParam, Object specialVal, ManagerImpl manager)
   {
      return delegate().invokeWithSpecialValue(instance, specialParam, specialVal, manager);
   }

   public boolean isEquivalent(Method method)
   {
      return delegate().isEquivalent(method);
   }
   
   public AnnotatedMethod<T> wrap(Set<Annotation> annotations)
   {
      throw new UnsupportedOperationException();
   }
   
}