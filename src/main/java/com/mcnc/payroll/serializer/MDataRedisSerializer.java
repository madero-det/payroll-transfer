package com.mcnc.payroll.serializer;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcnc.payroll.model.MData;

public class MDataRedisSerializer implements RedisSerializer<MData> {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public byte[] serialize(@Nullable MData mData) throws SerializationException {
		if (mData == null) {
			return new byte[0];
		}
		try {
			return objectMapper.writeValueAsBytes(mData);
		} catch (Exception e) {
			throw new SerializationException("Could not serialize MData object", e);
		}
	}

	@Override
	public MData deserialize(@Nullable byte[] bytes) throws SerializationException {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		try {
			return objectMapper.readValue(bytes, MData.class);
		} catch (Exception e) {
			throw new SerializationException("Could not deserialize byte array to MData", e);
		}
	}

	@Override
	public Class<?> getTargetType() {
		return MData.class;
	}

}
