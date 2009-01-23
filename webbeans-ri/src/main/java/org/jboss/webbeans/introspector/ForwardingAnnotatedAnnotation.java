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
import java.util.Set;

public abstract class ForwardingAnnotatedAnnotation<T extends Annotation> extends ForwardingAnnotatedType<T> implements AnnotatedAnnotation<T>
{
   
   @Override
   protected abstract AnnotatedAnnotation<T> delegate();
   
   public Set<AnnotatedMethod<?>> getAnnotatedMembers(Class<? extends Annotation> annotationType)
   {
      return delegate().getAnnotatedMembers(annotationType);
   }
   
   public Set<AnnotatedMethod<?>> getMembers()
   {
      return delegate().getMembers();
   }
   
   public AnnotatedAnnotation<T> wrap(Set<Annotation> annotations)
   {
      throw new UnsupportedOperationException();
   }
   
}
