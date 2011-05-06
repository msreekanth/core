/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
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

package org.jboss.weld.environment.servlet.test.injection;


import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.runner.RunWith;

import javax.enterprise.inject.spi.BeanManager;

import static org.jboss.weld.environment.servlet.test.util.JettyDeployments.JETTY_ENV;

/**
 * @author Ales Justin
 */
@RunWith(Arquillian.class)
public class LookupTest extends LookupTestBase
{
   @Deployment
   public static WebArchive deployment()
   {
      return LookupTestBase.deployment().addWebResource(JETTY_ENV, "jetty-env.xml");
   }

   @Override
   public void testManagerInJndi(Mouse mouse, BeanManager beanManager) throws Exception
   {
      // TODO -- fix me
   }

   @Override
   public void testResource(Vole vole, BeanManager beanManager) throws Exception
   {
      // TODO -- fix me
   }
}
