package com.mcnc.payroll.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * grid01
 */
public class MData extends LinkedHashMap<String, Object> implements MDataProtocol {
	private static final long serialVersionUID = 1L;

	private ObjectMapper objectMapper = new ObjectMapper();

	public MData() {
	}

	public MData(Map<String, Object> map) {
		super(map);
	}

	public MData setString(String key, String value) {
		this.put(key, value);
		return this;
	}

	public String getString(String key) {
		if (get(key) != null) {
			return String.valueOf(get(key)).trim();
		}
		return null;
	}

	public MData setBigDecimal(String key, BigDecimal value) {
		this.put(key, value);
		return this;
	}

	public BigDecimal getBigDecimal(String key) {
		if (get(key) != null && !getString(key).isEmpty()) {
			return new BigDecimal(getString(key));
		}
		return BigDecimal.ZERO;
	}

	public MData setLong(String key, long value) {
		this.put(key, value);
		return this;
	}

	public long getLong(String key) {
		if (get(key) != null) {
			return Long.parseLong(getString(key));
		}
		return 0L;
	}

	public MData setInt(String key, int value) {
		this.put(key, value);
		return this;
	}

	public int getInt(String key) {
		if (get(key) != null) {
			return Integer.parseInt(getString(key));
		}
		return 0;
	}

	public MData setBoolean(String key, boolean value) {
		this.put(key, value);
		return this;
	}

	public boolean getBoolean(String key) {
		if (get(key) != null) {
			return Boolean.parseBoolean(getString(key));
		}
		return false;
	}

	public MData setShort(String key, short value) {
		this.put(key, value);
		return this;
	}

	public short getShort(String key) {
		if (get(key) != null) {
			return Short.parseShort(getString(key));
		}
		return 0;
	}

	public MData setDouble(String key, double value) {
		this.put(key, value);
		return this;
	}

	public double getDouble(String key) {
		if (get(key) != null) {
			return Double.parseDouble(getString(key));
		}
		return 0.0D;
	}

	public MData setFloat(String key, float value) {
		this.put(key, value);
		return this;
	}

	public float getFloat(String key) {
		if (get(key) != null) {
			return Float.parseFloat(getString(key));
		}
		return 0.0F;
	}

	public MData set(String key, Object value) {
		this.put(key, value);
		return this;
	}

	public MData setMData(String key, MData value) {
		this.put(key, value);
		return this;
	}

	public MData getMData(String key) {
		try {
			Object obj = get(key);
			return objectMapper.convertValue(obj, MData.class);
		} catch (Exception e) {
			return new MData();
		}
	}

	public MData setListMData(String key, List<MData> value) {
		this.put(key, value);
		return this;
	}

	public List<MData> getListMData(String key) {
		try {
			Object obj = get(key);
			return objectMapper.convertValue(obj, new TypeReference<List<MData>>() {});
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public MData appendFrom(MData data) {
		this.putAll(data);
		return this;
	}

	@Override
	public boolean equals(Object data) {
		return super.equals(data);
	}
}
