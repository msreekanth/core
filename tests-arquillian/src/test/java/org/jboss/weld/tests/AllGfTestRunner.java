package org.jboss.weld.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.jboss.shrinkwrap.impl.base.URLPackageScanner;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

public class AllGfTestRunner extends Suite {
	
	static int totalTestCasesAdded,totalTestCasesSkipped;

	public AllGfTestRunner(Class<?> superClass, RunnerBuilder builder)
			throws InitializationError {
		super(builder, superClass, getAllClasses());
	}

	private static Class<?>[] getAllClasses() {
		final List<Class<?>> classes = new ArrayList<Class<?>>();
		
		//Reading the test list properties Begin
		Properties runTestList = new Properties();		
		final ArrayList<String> alRunTestList=new ArrayList<String>();
		final Logger logger = Logger.getLogger("TestCaseInclusionLog.log");		 
		FileHandler fh;
		try {
		      // This block configure the logger with handler and formatter
		      fh = new FileHandler("MyLogFile.log", false);
		      logger.addHandler(fh);
		      logger.setLevel(Level.ALL);
		      SimpleFormatter formatter = new SimpleFormatter();
		      fh.setFormatter(formatter);

		      // the following statement is used to log any messages   
		      logger.log(Level.INFO,"My first log");

		    } catch (SecurityException e) {
		      e.printStackTrace();
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		InputStream is = null;	
		try {
			is = new FileInputStream(new File("src/test/java/org/jboss/weld/tests/runTestList.properties"));
			runTestList.load(is);	
			
			for (Enumeration e1 = runTestList.propertyNames();  e1.hasMoreElements() ;) {
		         alRunTestList.add((String)e1.nextElement());
		     }
		}
		catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		//Reading the test list properties end here
		final ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		URLPackageScanner.newInstance(true, classLoader,
				new URLPackageScanner.Callback() {
					public void classFound(String className) {
						if (!className.endsWith("Test")) {
							return;
						}
						if (className.substring(className.lastIndexOf('.') + 1)
								.length() <= 4) {
							return;
						}
						if((alRunTestList.size()!=0) && !alRunTestList.contains(className))
						{
							//logger.log(Level.INFO,"[GF-QA-QE] : Checking if the class can be added to list");	
							logger.log(Level.INFO,"[GF-QA-QE] : SKIPPING CLASS "+className);
							totalTestCasesSkipped++;
							return;
						}else{
						try {
							logger.log(Level.INFO,"[GF-QA-QE] : ADDING CLASS:" + className);
							 classes.add(classLoader.loadClass(className));
							 totalTestCasesAdded++;
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
						}
					}
				}, AllTestRunner.class.getPackage()).scanPackage();
		// ExampleTest.class.getPackage()).scanPackage();

		Collections.sort(classes, new Comparator<Class<?>>() {
			public int compare(Class<?> o1, Class<?> o2) {
				return o1.getPackage().getName()
						.compareTo(o2.getPackage().getName());
			}
		});
		classes.add(com.sun.weld.tests.QualifierTest.class);
		logger.log(Level.INFO,"[GF-QA-QE] : Total Classes Addded :" + totalTestCasesAdded);
		logger.log(Level.INFO,"[GF-QA-QE] : Total Classes Skipped :" + totalTestCasesSkipped);
		logger.log(Level.INFO,"[GF-QA-QE] : Final Array List Size :" + classes.size());
		
		return classes.toArray(new Class<?>[] {});
	}

	
}
