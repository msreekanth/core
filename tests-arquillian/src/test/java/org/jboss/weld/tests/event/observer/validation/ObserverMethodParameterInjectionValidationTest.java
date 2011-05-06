/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.weld.tests.event.observer.validation;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.weld.tests.category.Broken;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

/**
 * @author Dan Allen
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@RunWith(Arquillian.class)
@Category(Broken.class) // TODO -- enable this when upgraded to Arq Alpha5
public class ObserverMethodParameterInjectionValidationTest
{
   @Deployment
   public static JavaArchive getDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class, "test.jar")
            .addManifestResource(EmptyAsset.INSTANCE, "beans.xml")
            .addClasses(SimpleTarget.class, SimpleObserver.class);
   }

   @Test
   public void testDeployment()
   {
      Assert.fail("Excepted deployment error: 'Unsatisfied dependencies for type [File] with qualifiers [@Default] on observer method injection point'");
   }

   /**
    * This test should not run, but if it does, it shows Weld reporting the ambiguous error in WELD-870:
    * WELD-001324 Argument bean must not be null
    *
    * @param beanManager the bean manager
   public void testNullInjectionOnObserverMethod(BeanManager beanManager)
   {
      beanManager.fireEvent("message");
   }
   */
}

