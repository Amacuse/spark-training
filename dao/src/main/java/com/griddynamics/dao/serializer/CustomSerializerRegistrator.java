package com.griddynamics.dao.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import com.griddynamics.dao.dto.ProductDto;
import com.griddynamics.dao.dto.UpcDto;
import com.griddynamics.dao.model.Product;
import com.griddynamics.dao.model.Upc;
import org.apache.spark.serializer.KryoRegistrator;

public class CustomSerializerRegistrator implements KryoRegistrator {

    @Override
    public void registerClasses(Kryo kryo) {
        kryo.register(Product.class, new JavaSerializer());
        kryo.register(ProductDto.class, new JavaSerializer());
        kryo.register(Upc.class, new JavaSerializer());
        kryo.register(UpcDto.class, new JavaSerializer());
    }
}
