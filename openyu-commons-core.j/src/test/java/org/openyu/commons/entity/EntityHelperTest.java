package org.openyu.commons.entity;

import static org.junit.Assert.*;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.dog.po.impl.DogPoImpl;
import org.openyu.commons.entity.supporter.LocaleNameEntitySupporter;
import org.openyu.commons.entity.supporter.NamesEntitySupporter;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class EntityHelperTest extends BaseTestSupporter {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 0, concurrency = 100)
	public void audit() {
		DogPoImpl value = new DogPoImpl();
		//
		boolean result = false;
		//
		result = EntityHelper.audit(value, "sys");

		System.out.println(result + ", " + value);
		assertTrue(result);
	}

	@Test
	// 1000000 times: 114 mills.
	// 1000000 times: 114 mills.
	// 1000000 times: 115 mills.
	// verified
	public void getNameByLocale() {
		Set<LocaleNameEntity> value = new LinkedHashSet<LocaleNameEntity>();

		LocaleNameEntity name = new LocaleNameEntitySupporter();
		name.setLocale(Locale.TRADITIONAL_CHINESE);
		name.setName("拉拉");
		value.add(name);
		//
		name = new LocaleNameEntitySupporter();
		name.setLocale(Locale.US);
		name.setName("LaLa");
		value.add(name);

		//
		String result = null;
		//
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = EntityHelper.getName(value, Locale.TRADITIONAL_CHINESE);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertEquals("拉拉", result);
		//
		result = EntityHelper.getName(value, Locale.US);
		System.out.println(result);
		assertEquals("LaLa", result);
	}

	@Test
	// 1000000 times: 159 mills.
	// 1000000 times: 162 mills.
	// 1000000 times: 158 mills.
	// verified
	public void filterName() {
		NamesEntity namesEntity = new NamesEntitySupporter();
		//
		LocaleNameEntity name = new LocaleNameEntitySupporter();
		name.setLocale(Locale.TRADITIONAL_CHINESE);
		name.setName("拉拉");
		namesEntity.getNames().add(name);
		//
		name = new LocaleNameEntitySupporter();
		name.setLocale(Locale.US);
		name.setName("LaLa");
		namesEntity.getNames().add(name);
		//
		int result = 0;
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			EntityHelper.filterName(namesEntity, Locale.TRADITIONAL_CHINESE);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(namesEntity);
		assertEquals(1, namesEntity.getNames().size());
	}

	@Test
	// 1000000 times: 177 mills.
	// 1000000 times: 171 mills.
	// 1000000 times: 160 mills.
	// verified
	public void filterName2() {
		NamesEntity namesEntity = new NamesEntitySupporter();
		//
		LocaleNameEntity name = new LocaleNameEntitySupporter();
		name.setLocale(Locale.TRADITIONAL_CHINESE);
		name.setName("拉拉");
		namesEntity.getNames().add(name);
		//
		name = new LocaleNameEntitySupporter();
		name.setLocale(Locale.US);
		name.setName("LaLa");
		namesEntity.getNames().add(name);
		//
		int result = 0;
		int count = 1000000;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			EntityHelper.filterName(namesEntity, Locale.US);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(namesEntity);
		assertEquals(1, namesEntity.getNames().size());
	}

}
