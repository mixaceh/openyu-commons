package org.openyu.commons.cat.service;

import java.io.Serializable;

import org.openyu.commons.cat.vo.impl.CatImpl;
import org.openyu.commons.service.CommonService;

public interface CatService extends CommonService {

	Serializable insertCat(CatImpl cat);

}
