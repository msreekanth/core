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

package org.jboss.weld.tests.interceptors.injectionWithMethodExclusions;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.BeanArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.weld.tests.category.Integration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

/**
 * @author Marius Bogoevici
 */

@Category(Integration.class)
@RunWith(Arquillian.class)
public class TestInjectionWithExclusionsOnMethods
{
   @Deployment
   public static Archive<?> deploy()
   {
      return ShrinkWrap.create(BeanArchive.class)
            .addPackage(TestInjectionWithExclusionsOnMethods.class.getPackage());
   }

   @Test
   public void testBeanWithExcludeDefaultInterceptorsInjected(Simple simple) throws Exception
   {
      Assert.assertNotNull(simple.getHelper());

      Counter.count = 0;
      EjbInterceptor.count = 0;
      EjbInterceptor2.count = 0;
      EjbInterceptor3.count = 0;
      EjbInterceptor4.count = 0;
      Assert.assertNotNull(simple.getHelper());
      Assert.assertEquals(1, EjbInterceptor.count);
      Assert.assertEquals(2, EjbInterceptor2.count);
      Assert.assertEquals(0, EjbInterceptor3.count);
      Assert.assertEquals(0, EjbInterceptor4.count);

      Counter.count = 0;
      EjbInterceptor.count = 0;
      EjbInterceptor2.count = 0;
      EjbInterceptor3.count = 0;
      EjbInterceptor4.count = 0;
      simple.doSomething();
      Assert.assertEquals(1, EjbInterceptor.count);
      Assert.assertEquals(2, EjbInterceptor2.count);
      Assert.assertEquals(3, EjbInterceptor3.count);
      Assert.assertEquals(4, EjbInterceptor4.count);

      simple.getHelper().help();
   }

   @Test
   public void testBeanWithExcludeClassInterceptors(Simple2 simple) throws Exception
   {
      Counter.count = 0;
      EjbInterceptor.count = 0;
      EjbInterceptor2.count = 0;
      EjbInterceptor3.count = 0;
      EjbInterceptor4.count = 0;
      Assert.assertNotNull(simple.getHelper());
      Assert.assertEquals(1, EjbInterceptor.count);
      Assert.assertEquals(2, EjbInterceptor2.count);
      Assert.assertEquals(0, EjbInterceptor3.count);
      Assert.assertEquals(0, EjbInterceptor4.count);

      Counter.count = 0;
      EjbInterceptor.count = 0;
      EjbInterceptor2.count = 0;
      EjbInterceptor3.count = 0;
      EjbInterceptor4.count = 0;
      simple.doSomething();
      Assert.assertEquals(0, EjbInterceptor.count);
      Assert.assertEquals(0, EjbInterceptor2.count);
      Assert.assertEquals(1, EjbInterceptor3.count);
      Assert.assertEquals(2, EjbInterceptor4.count);

      simple.getHelper().help();
   }
}
