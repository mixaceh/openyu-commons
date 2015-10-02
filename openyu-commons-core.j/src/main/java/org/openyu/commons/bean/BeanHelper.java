package org.openyu.commons.bean;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.openyu.commons.lang.ClassHelper;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.mark.Supporter;
import org.openyu.commons.util.CollectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanHelper extends BaseHelperSupporter implements Supporter {

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(BeanHelper.class);

	// // appConfig-op.xml
	// public final static String ASSIGN_KEY = "beanHelper.assignKey";
	//
	// private static String assignKey;
	//
	// //
	// public final static String ALGORITHM = "beanHelper.algorithm";
	//
	// private static String algorithm;

	static {
		new Static();
	}

	protected static class Static {
		public Static() {
			try {
				// assignKey = ConfigHelper.getString(ASSIGN_KEY, "assignKey");
				// algorithm = ConfigHelper.getString(ALGORITHM, "DES");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public BeanHelper() {
	}

	// public static String getAssignKey() {
	// return assignKey;
	// }
	//
	// public static void setAssignKey(String assignKey) {
	// BeanHelper.assignKey = assignKey;
	// }
	//
	// public static String getAlgorithm() {
	// return algorithm;
	// }
	//
	// public static void setAlgorithm(String algorithm) {
	// BeanHelper.algorithm = algorithm;
	// }

	/**
	 * 依語系取得名稱
	 * 
	 * @param locale
	 * @param names
	 * @return
	 */
	public static <E extends LocaleNameBean> String getName(Set<E> names,
			Locale locale) {
		String result = null;
		if (CollectionHelper.notEmpty(names)) {
			for (LocaleNameBean entry : names) {
				if (locale != null && entry.getLocale() != null
						&& locale.equals(entry.getLocale())) {
					result = entry.getName();
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * 過濾後,只剩下符合條件的
	 * 
	 * @param value
	 * @param locale
	 * @return
	 */
	protected static void processFilterName(NamesBean value, Locale locale) {
		processFilterName(value, locale, null);
	}

	/**
	 * 過濾後,只剩下符合條件的
	 * 
	 * @param value
	 * @param locale
	 * @param name
	 */
	protected static void processFilterName(NamesBean value, Locale locale,
			String name) {
		processFilterName(value.getNames(), locale, name);
	}

	/**
	 * 
	 * 過濾後,只剩下符合條件的
	 * 
	 * @param values
	 * @param locale
	 * @return
	 */
	protected static void processFilterName(Collection<LocaleNameBean> values,
			Locale locale) {
		processFilterName(values, locale, null);
	}

	/**
	 * 過濾後,只剩下符合條件的
	 * 
	 * @param values
	 * @param locale
	 * @param name
	 */
	protected static void processFilterName(Collection<LocaleNameBean> values,
			Locale locale, String name) {
		if (locale != null && CollectionHelper.notEmpty(values)) {
			LocaleNameBean foundNameBean = null;
			for (LocaleNameBean localeNameBean : values) {
				// 有符合區域的LocaleNameBean
				foundNameBean = getFilterNameBean(localeNameBean, locale, name);
				if (foundNameBean != null) {
					break;
				}
			}
			//
			values.clear();
			if (foundNameBean != null) {
				values.add(foundNameBean);
			}
		}
	}

	/**
	 * 
	 * 有符合區域的
	 * 
	 * @param value
	 * @param locale
	 * @param name
	 * @return
	 */
	protected static LocaleNameBean getFilterNameBean(LocaleNameBean value,
			Locale locale, String name) {
		LocaleNameBean result = null;
		// filter by locale
		if (locale != null && value.getLocale() != null
				&& locale.equals(value.getLocale())) {
			result = value;
			// filter by name
			if (name != null && value.getName() != null
					&& value.getName().indexOf(name) < 0) {
				result = null;
			}
		}
		return result;
	}

	/**
	 * 過濾後,只剩下符合條件的
	 * 
	 * @param value
	 * @param locale
	 */
	public static void filterName(Object value, Locale locale) {
		filterName(value, locale, null);
	}

	/**
	 * 過濾後,只剩下符合條件的
	 * 
	 * @param value
	 * @param locale
	 * @param name
	 */
	@SuppressWarnings("unchecked")
	public static void filterName(Object value, Locale locale, String name) {
		if (value == null) {
			return;
		}

		// 取所有的field值
		List<Object> fieldValues = ClassHelper.getDeclaredFieldValue(value);
		for (Object fieldValue : fieldValues) {
			if (fieldValue instanceof NamesBean) {
				NamesBean namesBean = (NamesBean) fieldValue;
				processFilterName(namesBean, locale, name);
			}
			// set
			else if (fieldValue instanceof Collection<?>) {
				boolean isLocaleNameBean = false;
				LocaleNameBean foundNameBean = null;
				@SuppressWarnings("rawtypes")
				Collection buffs = (Collection) fieldValue;
				for (Object entry : buffs) {
					// 不是LocaleNameBean的Collection
					if (!(entry instanceof LocaleNameBean)) {
						break;
					}
					// 是LocaleNameBean的Collection
					isLocaleNameBean = true;
					LocaleNameBean localeNameBean = (LocaleNameBean) entry;
					foundNameBean = getFilterNameBean(localeNameBean, locale,
							name);
					// 有符合區域的LocaleNameBean
					if (foundNameBean != null) {
						break;
					}
				}
				// 是LocaleNameBean的Collection,且有符合區域的LocaleNameBean
				if (isLocaleNameBean) {
					buffs.clear();
					if (foundNameBean != null) {
						buffs.add(foundNameBean);
					}
				}
			}
		}
	}

	/**
	 * 過濾後,只剩下符合條件的
	 * 
	 * @param values
	 * @param locale
	 */
	public static void filterName(Collection<?> values, Locale locale) {
		filterName(values, locale, null);
	}

	/**
	 * 過濾後,只剩下符合條件的
	 * 
	 * @param values
	 * @param locale
	 * @param name
	 */
	public static void filterName(Collection<?> values, Locale locale,
			String name) {
		if (CollectionHelper.notEmpty(values)) {
			for (Object entry : values) {
				filterName(entry, locale, name);
			}
		}
	}

	/**
	 * 計算所有權重加總
	 * 
	 * @param map
	 * @return
	 */
	public static <V extends WeightBean> int sumOf(Map<?, V> map) {
		int result = 0;
		//
		if (map != null) {
			result = sumOf(map.values());
		}
		return result;
	}

	/**
	 * 
	 * 計算所有權重加總
	 * 
	 * @param values
	 * @return
	 */
	public static <E extends WeightBean> int sumOf(Collection<E> values) {
		int result = 0;
		//
		if (CollectionHelper.notEmpty(values)) {
			for (E entry : values) {
				result += entry.getWeight();
			}
		}
		return result;
	}

	/**
	 * 依權重取
	 * 
	 * @param map
	 * @param weightSum
	 * @return
	 */
	public static <V extends WeightBean> V randomOf(Map<?, V> map, int weightSum) {
		V result = null;
		if (CollectionHelper.notEmpty(map)) {
			result = randomOf(map.values(), weightSum);
		}
		return result;
	}

	/**
	 * 依權重取
	 * 
	 * @param values
	 * @param weightSum
	 * @return
	 */
	public static <E extends WeightBean> E randomOf(Collection<E> values,
			int weightSum) {
		E result = null;
		if (CollectionHelper.notEmpty(values)) {
			// 計算所有權重加總
			// #issue: 每次加總會變慢
			// int sum = calcWeightSum(collection);
			// System.out.println("sum: "+sum);
			//
			// #fix 傳入weightSum, ok
			int random = NumberHelper.randomInt(0, weightSum);
			//
			int accu = 0;// 累計權重
			for (E entry : values) {
				int weight = entry.getWeight();
				int low = accu;
				int high = accu + weight;
				if (random >= low && random < high) {
					result = entry;
				}
				accu = high;
				// 若機率=0,則順便算機率
				if (entry.getProbability() == 0d) {
					// 精確到小數點以下10位
					double probability = NumberHelper.divide(weight, weightSum);// 0.0039525692
					entry.setProbability(probability);
				}
			}
		}
		return result;
	}

	/**
	 * 計算所有機率加總
	 * 
	 * @param map
	 * @return
	 */
	public static <V extends ProbabilityBean> double probSumOf(Map<?, V> map) {
		double result = 0d;
		//
		if (map != null) {
			result = probSumOf(map.values());
		}
		return result;
	}

	/**
	 * 計算所有機率加總
	 * 
	 * @param values
	 * @return
	 */
	public static <E extends ProbabilityBean> double probSumOf(
			Collection<E> values) {
		double result = 0d;
		//
		if (CollectionHelper.notEmpty(values)) {
			for (E entry : values) {
				result = NumberHelper.add(result, entry.getProbability());
			}
		}
		return result;
	}

	/**
	 * 依機率取
	 * 
	 * @param map
	 * @param probSum
	 * @return
	 */
	public static <V extends ProbabilityBean> V probRandomOf(Map<?, V> map,
			double probSum) {
		V result = null;
		if (CollectionHelper.notEmpty(map)) {
			result = probRandomOf(map.values(), probSum);
		}
		return result;
	}

	/**
	 * 依機率取
	 * 
	 * @param values
	 * @param probSum
	 * @return
	 */
	public static <E extends ProbabilityBean> E probRandomOf(
			Collection<E> values, double probSum) {
		E result = null;
		if (CollectionHelper.notEmpty(values)) {
			// 計算所有機率加總
			// #issue: 每次加總會變慢
			// double sum = calcProbabilitySum(collection);
			// System.out.println("sum: "+sum);
			// 與randomByWeight的不同之處,直接用機率比對
			//
			// #fix 傳入probabilitySum, ok
			double random = NumberHelper.randomDouble(0, probSum);
			//
			double accu = 0d;
			for (E entry : values) {
				double low = accu;
				double high = accu + entry.getProbability();
				if (random >= low && random < high) {
					result = entry;
				}
				accu = high;
			}
		}
		return result;
	}

	// //依權重,隨機取
	// private int getRandomProvisionLevel(List<ProvisionSlot> provisionSlots)
	// {
	// int sum = 0;
	// for (ProvisionSlot slot : provisionSlots)
	// {
	// sum += slot.getWeight();
	// }
	//
	// int rand = Util.genNextRandomInteger(1, sum + 1);
	//
	// sum = 0;
	// int counter = 0;
	// for (ProvisionSlot slot : provisionSlots)
	// {
	// sum += slot.getWeight();
	// //rand is in weight range
	// if (rand <= sum)
	// {
	// return counter;
	// }
	// ++counter;
	// }
	//
	// throw new RuntimeException("uh??? random not in range??!");
	// }

}
