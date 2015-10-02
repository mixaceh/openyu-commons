package org.openyu.commons.core;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Collator;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

	public static void main(String[] args) {
		System.out
				.println("------------------------------------------------------------------------");
		System.out.println("Pre Testing");
		System.out
				.println("------------------------------------------------------------------------");
		long begTime = System.nanoTime();
		//
		// print applicationContext-init.xml
		InputStream in = App.class
				.getResourceAsStream("/applicationContext-init.xml");
		InputStreamReader inputStreamReader = new InputStreamReader(in);
		//
		//
		System.out.println("--- Loading applicationContext-init.xml ---");
		BufferedReader bufferedReader = null;
		StringBuilder buff = new StringBuilder();
		String line;
		try {
			bufferedReader = new BufferedReader(inputStreamReader);
			while ((line = bufferedReader.readLine()) != null) {
				buff.append(line);
				buff.append("\r\n");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (Exception ex) {
				}
			}
		}
		//
		System.out.println(buff);
		//
		System.out.println("--- Loading applicationContext.xml ---");
		// applicationContext
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				new String[] { "applicationContext.xml" });
		//
		System.out.println(applicationContext);
		String[] beanNames = applicationContext.getBeanDefinitionNames();
		Arrays.sort(beanNames, Collator.getInstance(java.util.Locale.ENGLISH));
		//
		System.out.println();
		System.out.println("--- Printing spring beans ---");

		for (int i = 0; i < beanNames.length; i++) {
			System.out.println("[" + i + "] " + beanNames[i]);
		}
		System.out
				.println("------------------------------------------------------------------------");
		System.out.println("PRE TESTING SUCCESS");
		System.out
				.println("------------------------------------------------------------------------");
		//
		long endTime = System.nanoTime();
		long durTime = endTime - begTime;
		durTime = TimeUnit.NANOSECONDS.toMillis(durTime);
		//
		String msgPattern = "Total time: {0} ms";
		StringBuilder msg = new StringBuilder(MessageFormat.format(msgPattern,
				durTime));
		System.out.println(msg);
		//
		msgPattern = "Total beans: {0}";
		msg = new StringBuilder(MessageFormat.format(msgPattern,
				beanNames.length));
		System.out.println(msg);
		//
		System.out
				.println("------------------------------------------------------------------------");
		System.exit(0);
	}
}
