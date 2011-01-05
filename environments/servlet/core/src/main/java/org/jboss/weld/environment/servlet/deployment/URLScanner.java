/**
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.weld.environment.servlet.deployment;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * This class provides file-system orientated scanning
 *
 * @author Pete Muir
 * @author Ales Justin
 */
public class URLScanner
{
   private static final Logger log = LoggerFactory.getLogger(URLScanner.class);

   private final ClassLoader classLoader;

   public URLScanner(ClassLoader classLoader)
   {
      this.classLoader = classLoader;
   }

   protected ClassLoader getClassLoader()
   {
      return classLoader;
   }

   protected void handle(String name, URL url, Set<String> classes, Set<URL> urls)
   {
      if (name.endsWith(".class"))
      {
         classes.add(filenameToClassname(name));
      }
      else if (name.equals(WebAppBeanDeploymentArchive.META_INF_BEANS_XML))
      {
         urls.add(url);
      }
   }

   public void scanDirectories(File[] directories, Set<String> classes, Set<URL> urls)
   {
      for (File directory : directories)
      {
         handleDirectory(directory, null, classes, urls);
      }
   }

   public void scanResources(String[] resources, Set<String> classes, Set<URL> urls)
   {
      Set<String> paths = new HashSet<String>();

      for (String resourceName : resources)
      {
         try
         {
            Enumeration<URL> urlEnum = classLoader.getResources(resourceName);

            while (urlEnum.hasMoreElements())
            {
               String urlPath = urlEnum.nextElement().getFile();

               if (urlPath.startsWith("file:"))
               {
                  urlPath = urlPath.substring(5);
               }

               if (urlPath.indexOf('!') > 0)
               {
                  urlPath = urlPath.substring(0, urlPath.indexOf('!'));
               }
               else
               {
                  File dirOrArchive = new File(urlPath);

                  if ((resourceName != null) && (resourceName.lastIndexOf('/') > 0))
                  {
                     // for META-INF/beans.xml
                     dirOrArchive = dirOrArchive.getParentFile();
                  }

                  urlPath = dirOrArchive.getParent();
               }

               paths.add(urlPath);
            }
         }
         catch (IOException ioe)
         {
            log.warn("could not read: " + resourceName, ioe);
         }
      }

      handle(paths, classes, urls);
   }

   protected void handle(Set<String> paths, Set<String> classes, Set<URL> urls)
   {
      for (String urlPath : paths)
      {
         try
         {
            log.trace("scanning: " + urlPath);

            File file = new File(urlPath);

            if (file.isDirectory())
            {
               handleDirectory(file, null, classes, urls);
            }
            else
            {
               handleArchiveByFile(file, classes, urls);
            }
         }
         catch (IOException ioe)
         {
            log.warn("could not read entries", ioe);
         }
      }
   }

   protected void handleArchiveByFile(File file, Set<String> classes, Set<URL> urls) throws IOException
   {
      try
      {
         log.trace("archive: " + file);

         ZipFile zip = new ZipFile(file);
         Enumeration<? extends ZipEntry> entries = zip.entries();
         // Replace out the default classloader with one for the zip
         URLClassLoader classLoader = new URLClassLoader(new URL[] { file.toURI().toURL() });
         while (entries.hasMoreElements())
         {
            ZipEntry entry = entries.nextElement();
            String name = entry.getName();
            handle(name, classLoader.getResource(name), classes, urls);
         }
      }
      catch (ZipException e)
      {
         throw new RuntimeException("Error handling file " + file, e);
      }
   }

   private void handleDirectory(File file, String path, Set<String> classes, Set<URL> urls)
   {
      handleDirectory(file, path, new File[0], classes, urls);
   }

   private void handleDirectory(File file, String path, File[] excludedDirectories, Set<String> classes, Set<URL> urls)
   {
      for (File excludedDirectory : excludedDirectories)
      {
         if (file.equals(excludedDirectory))
         {
            log.trace("skipping excluded directory: " + file);

            return;
         }
      }

      log.trace("handling directory: " + file);

      for (File child : file.listFiles())
      {
         String newPath = (path == null) ? child.getName() : (path + '/' + child.getName());

         if (child.isDirectory())
         {
            handleDirectory(child, newPath, excludedDirectories, classes, urls);
         }
         else
         {
            try
            {
               handle(newPath, child.toURI().toURL(), classes, urls);
            }
            catch (MalformedURLException e)
            {
               log.error("Error loading file " + newPath);
            }
         }
      }
   }

   /**
    * Convert a path to a class file to a class name
    *
    * @param filename the file name
    * @return classname
    */
   public static String filenameToClassname(String filename)
   {
      return filename.substring(0, filename.lastIndexOf(".class")).replace('/', '.').replace('\\', '.');
   }
}
