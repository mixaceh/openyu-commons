package org.openyu.commons.enumz;

/**
 * 列舉成員命名，請使用英文大寫形式
 */
public interface LimitEnum extends BaseEnum
{
	/**
	 * 最大值,上限
	 * 
	 * @return
	 */
	int maxValue();

	/**
	 * 最小值,下限
	 * 
	 * @return
	 */
	int minValue();
}
