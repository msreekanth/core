package org.jboss.weld.tests.threadlocal;

import javax.inject.Inject;

public class Baz
{

   @Inject Bar bar;
   
   public Bar getBar()
   {
      return bar;
   }
   
}
