package com.craigrueda.webkeypad.util;

import java.util.HashMap;
import java.util.Map;

public class ParamUtils {
	private ParamUtils() { }
	
	public static final Map<String, Object> createMapForParams(Object... params) {
		if (params.length % 2 > 0)
			throw new IllegalArgumentException("Parameters names must be matched by an acompanying value.");
		
		Map<String, Object> ret = new HashMap<String, Object>();
		
		for (int i = 0; i < params.length; i += 2) 
			ret.put((String) params[i], params[i + 1]);

		return ret;
	}
}
