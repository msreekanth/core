/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
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

package org.jboss.weld.tests.interceptors.circularInvocation;

import java.lang.UnsupportedOperationException;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.BeanArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.weld.bean.proxy.InterceptionDecorationContext;
import org.jboss.weld.exceptions.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Marius Bogoevici
 */
@RunWith(Arquillian.class)
public class SelfInvokingClassTest
{
   @Deployment
   public static Archive<?> deploy()
   {
      return ShrinkWrap.create(BeanArchive.class)
         .intercept(AllPurposeInterceptor.class)
         .decorate(SomeBeanDecorator.class)
         .addPackage(SelfInvokingClassTest.class.getPackage());
   }

   @Test
   public void testSelfInvokingClassWithFailingBean(@Failing SomeBean someBean)
   {
      AllPurposeInterceptor.interceptedMethods.clear();
      SomeBeanDecorator.calls.clear();
      try
      {
         someBean.methodA();
         Assert.assertEquals(2, AllPurposeInterceptor.interceptedMethods.size());
         Assert.assertEquals("methodA", AllPurposeInterceptor.interceptedMethods.get(0).getName());
         Assert.assertEquals("methodB", AllPurposeInterceptor.interceptedMethods.get(1).getName());
         Assert.assertEquals(2, SomeBeanDecorator.calls.size());
         Assert.assertEquals("methodA", SomeBeanDecorator.calls.get(0));
         Assert.assertEquals("methodB", SomeBeanDecorator.calls.get(1));
      }
      catch (Exception e)
      {
         Assert.assertTrue(e instanceof UnsupportedOperationException);
      }
      Assert.assertTrue(InterceptionDecorationContext.empty());
   }

   @Test
   public void testSelfInvokingClassWithSucceedingBean(@Succeeding SomeBean someBean)
   {
      AllPurposeInterceptor.interceptedMethods.clear();
      SomeBeanDecorator.calls.clear();
      someBean.methodA();
      Assert.assertEquals(2, AllPurposeInterceptor.interceptedMethods.size());
      Assert.assertEquals("methodA", AllPurposeInterceptor.interceptedMethods.get(0).getName());
      Assert.assertEquals("methodB", AllPurposeInterceptor.interceptedMethods.get(1).getName());
      Assert.assertEquals(2, SomeBeanDecorator.calls.size());
      Assert.assertEquals("methodA", SomeBeanDecorator.calls.get(0));
      Assert.assertEquals("methodB", SomeBeanDecorator.calls.get(1));

      Assert.assertTrue(InterceptionDecorationContext.empty());
   }

}
