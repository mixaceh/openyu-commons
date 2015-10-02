package org.openyu.commons.processor;

import org.openyu.commons.model.BaseModel;

/**
 * 處理器
 * 
 * 與collector(靜態)不同,processor是動態可改變,所以不會從.ser讀取回來,而是從bean注入設定
 * 
 * 這種bean類似service內含邏輯,但屬性是可動態改變的
 */
public interface BaseProcessor extends BaseModel {

}
