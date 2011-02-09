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
package org.jboss.weld.bean.builtin.ee;

import static org.jboss.weld.logging.messages.BeanMessage.VALIDATION_SERVICE_NOT_AVAILABLE;

import javax.validation.Validator;

import org.jboss.weld.Container;
import org.jboss.weld.exceptions.IllegalStateException;
import org.jboss.weld.manager.BeanManagerImpl;
import org.jboss.weld.validation.spi.ValidationServices;

/**
 * @author pmuir
 *
 */
public class DefaultValidatorBean extends AbstractEEBean<Validator>
{
   
   private static class ValidatorCallable extends AbstractEECallable<Validator>
   {
      
      private static final long serialVersionUID = 2455850340620002351L;

      public ValidatorCallable(BeanManagerImpl beanManager)
      {
         super(beanManager);
      }

      public Validator call() throws Exception
      {
         if (getBeanManager().getServices().contains(ValidationServices.class))
         {
            return getBeanManager().getServices().get(ValidationServices.class).getDefaultValidatorFactory().getValidator();
         }
         else
         {
            throw new IllegalStateException(VALIDATION_SERVICE_NOT_AVAILABLE);
         }
      }
      
   }
   
   public DefaultValidatorBean(BeanManagerImpl manager)
   {
      super(Validator.class, new ValidatorCallable(manager), manager);
   }
   
   @Override
   public String toString()
   {
      return "Built-in Bean [javax.validator.Validator] with qualifiers [@Default]";
   }

}
