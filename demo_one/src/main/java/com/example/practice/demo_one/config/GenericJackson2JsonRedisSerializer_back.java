package com.example.practice.demo_one.config;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.fasterxml.jackson.databind.ObjectMapper;

public record GenericJackson2JsonRedisSerializer_back() implements RedisSerializer {



	@Override
	public byte[] serialize(Object value) throws SerializationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object deserialize(byte[] bytes) throws SerializationException {
		// TODO Auto-generated method stub
		return null;
	}

}
