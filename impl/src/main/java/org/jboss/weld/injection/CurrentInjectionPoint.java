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
package org.jboss.weld.injection;

import java.util.EmptyStackException;
import java.util.Stack;

import javax.enterprise.inject.spi.InjectionPoint;

import org.jboss.weld.bootstrap.api.Service;

public class CurrentInjectionPoint implements Service
{
   
   private final ThreadLocal<Stack<InjectionPoint>> currentInjectionPoint;
   
   public CurrentInjectionPoint()
   {
      this.currentInjectionPoint = new ThreadLocal<Stack<InjectionPoint>>();
   }
      
   /**
    * Replaces (or adds) the current injection point. If a current injection 
    * point exists, it will be replaced. If no current injection point exists, 
    * one will be added.
    * 
    * @param injectionPoint the injection point to use
    * @return the injection point added, or null if previous existed did not exist
    */
   public void push(InjectionPoint injectionPoint)
   {
      Stack<InjectionPoint> stack = currentInjectionPoint.get();
      if (stack == null)
      {
         stack = new Stack<InjectionPoint>();
         currentInjectionPoint.set(stack);
      }
      stack.push(injectionPoint);
   }
   
   public InjectionPoint pop()
   {
      Stack<InjectionPoint> stack = currentInjectionPoint.get();
      if (stack == null)
      {
         throw new EmptyStackException();
      }
      try
      {
         return stack.pop();
      }
      finally
      {
         if (stack.isEmpty())
         {
            currentInjectionPoint.remove();
         }
      }
   }
   
   /**
    * The injection point being operated on for this thread
    * 
    * @return the current injection point
    */
   public InjectionPoint peek()
   {
      Stack<InjectionPoint> stack = currentInjectionPoint.get();
      if (stack == null)
      {
         return null;
      }
      if (!stack.empty())
      {
         return stack.peek();
      }
      else
      {
         return null;
      }
   }

   public void cleanup()
   {

   }

}
