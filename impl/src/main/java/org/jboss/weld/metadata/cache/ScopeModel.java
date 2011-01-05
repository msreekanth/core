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
package org.jboss.weld.metadata.cache;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static org.jboss.weld.logging.Category.REFLECTION;
import static org.jboss.weld.logging.LoggerFactory.loggerFactory;
import static org.jboss.weld.logging.messages.ReflectionMessage.MISSING_TARGET;
import static org.jboss.weld.logging.messages.ReflectionMessage.MISSING_TARGET_METHOD_FIELD_TYPE;

import java.lang.annotation.Annotation;
import java.lang.annotation.Target;
import java.util.Set;

import javax.enterprise.context.NormalScope;
import javax.inject.Scope;

import org.jboss.weld.resources.ClassTransformer;
import org.jboss.weld.util.collections.Arrays2;
import org.slf4j.cal10n.LocLogger;

/**
 * 
 * Model of a scope
 * 
 * @author Pete Muir
 * 
 */
public class ScopeModel<T extends Annotation> extends AnnotationModel<T>
{
   
   private static final Set<Class<? extends Annotation>> META_ANNOTATIONS = Arrays2.asSet(Scope.class, NormalScope.class);
   
   private static final LocLogger log = loggerFactory().getLogger(REFLECTION);

   private final boolean normal;
   private final boolean passivating;
   
   /**
    * Constrctor
    * 
    * @param scope The scope type
    */
   public ScopeModel(Class<T> scope, ClassTransformer classTransformer)
   {
      super(scope, classTransformer);
      if (isValid())
      {
         if (getAnnotatedAnnotation().isAnnotationPresent(NormalScope.class))
         {
            this.passivating = getAnnotatedAnnotation().getAnnotation(NormalScope.class).passivating();
            this.normal = true;
         }
         else
         {
            this.normal = false;
            this.passivating = false;
         }
      }
      else
      {
         this.normal = false;
         this.passivating = false;
      }
   }
   
   @Override
   protected void check()
   {
      super.check();
      if (isValid())
      {
         if (!getAnnotatedAnnotation().isAnnotationPresent(Target.class))
         {
            log.debug(MISSING_TARGET, getAnnotatedAnnotation());
         }
         else if (!Arrays2.unorderedEquals(getAnnotatedAnnotation().getAnnotation(Target.class).value(), METHOD, FIELD, TYPE))
         {
            log.debug(MISSING_TARGET_METHOD_FIELD_TYPE, getAnnotatedAnnotation());
         }
      }
   }

   /**
    * Indicates if the scope is "normal"
    * 
    * @return True if normal, false otherwise
    */
   public boolean isNormal()
   {
      return normal;
   }

   /**
    * Indicates if the scope is "passivating"
    * 
    * @return True if passivating, false otherwise
    */
   public boolean isPassivating()
   {
      return passivating;
   }

   /**
    * Gets the corresponding meta-annotation type class
    * 
    * @return The ScopeType class
    */
   @Override
   protected Set<Class<? extends Annotation>> getMetaAnnotationTypes() 
   {
      return META_ANNOTATIONS;
   }

   /**
    * Gets a string representation of the scope model
    * 
    * @return The string representation
    */
   @Override
   public String toString()
   {
      String valid = isValid() ? "Valid " : "Invalid";
      String normal = isNormal() ? "normal " : "non-normal ";
      String passivating = isPassivating() ? "passivating " : "pon-passivating ";
      return valid + normal + passivating + " scope model for " + getRawType();
   }

}
