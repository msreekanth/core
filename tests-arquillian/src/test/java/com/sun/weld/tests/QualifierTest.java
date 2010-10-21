/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.weld.tests;

import java.io.IOException;
import java.io.PrintWriter;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.BeanArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.weld.test.Utils;
import org.jboss.weld.tests.producer.field.named.NamedProducerTest;
import org.junit.runner.RunWith;
import org.junit.Assert;
import org.junit.Test;
/**
 *
 * @author sreekanth
 */
@RunWith(Arquillian.class)
public class QualifierTest {

   @Deployment
   public static Archive<?> deploy()
   {
      return ShrinkWrap.create(BeanArchive.class)
         .addPackage(QualifierTest.class.getPackage());
   }

   
    @Inject @School Boy scholBoy;
    @Inject @College Boy collegeBoy;
   
    @Test
    public void testQualifiers()
    {
       Assert.assertArrayEquals("SCHOOL".toCharArray(),scholBoy.gotoSchool().toCharArray()) ;
       Assert.assertArrayEquals("sD".toCharArray(),collegeBoy.gotoSchool().toCharArray());
    } 
   
}
