package org.jboss.weld.tests.beanDeployment.circular;

import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.Classes;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.jboss.weld.exceptions.DeploymentException;
import org.testng.annotations.Test;

@Artifact(addCurrentPackage = false)
@ExpectedDeploymentException(DeploymentException.class)
@Classes( { Fish.class, Water.class })
public class DependentCircularInjectionTest
{
   @Test
   public void test()
   {

   }

}
