package cn.edu.huel.user.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.ObjectUtils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author 张晓华
 * @date 2023-2-19
 */
public class FastJson2RedisSerializer<T> implements RedisSerializer<T> {

	private Class cla;

	public FastJson2RedisSerializer(Class cla) {
		this.cla = cla;
	}

	@Override
	public byte[] serialize(T t) throws SerializationException {
		if (Objects.isNull(t)) {
			return new byte[0];
		} else {
			return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(StandardCharsets.UTF_8);
		}
	}

	@Override
	public T deserialize(byte[] bytes) throws SerializationException {
		if (ObjectUtils.isEmpty(bytes)) {
			return null;
		} else {
			return (T) JSON.parseObject(new String(bytes, StandardCharsets.UTF_8), cla);
		}
	}


}
