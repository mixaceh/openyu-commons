package org.openyu.commons.processor.supporter;

import org.openyu.commons.mark.Supporter;
import org.openyu.commons.model.supporter.BaseModelSupporter;
import org.openyu.commons.processor.BaseProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseProcessorSupporter extends BaseModelSupporter implements
		BaseProcessor, Supporter {

	private static final long serialVersionUID = 6743496445453663026L;

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(BaseProcessorSupporter.class);

	public BaseProcessorSupporter() {

	}
}
