package com.griddynamics.dao.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.griddynamics.dao.dto.ProductDto;
import com.griddynamics.dao.dto.UpcDto;
import com.griddynamics.dao.model.Product;
import com.griddynamics.dao.model.Upc;
import org.apache.spark.serializer.KryoRegistrator;

import java.util.ArrayList;

public class CustomSerializerRegistrator implements KryoRegistrator {

    @Override
    public void registerClasses(Kryo kryo) {

        kryo.setDefaultSerializer(FieldSerializer.class);

        FieldSerializer<Product> productFieldSerializer = new FieldSerializer<>(kryo, Product.class);
        FieldSerializer<Product> productDtoFieldSerializer = new FieldSerializer<>(kryo, ProductDto.class);

        CollectionSerializer collectionSerializer = new CollectionSerializer();
        collectionSerializer.setElementClass(Integer.class, kryo.getSerializer(Integer.class));
        collectionSerializer.setElementsCanBeNull(true);

        productFieldSerializer.getField("uids").setClass(ArrayList.class, collectionSerializer);
        productDtoFieldSerializer.getField("uids").setClass(ArrayList.class, collectionSerializer);

        kryo.register(Product.class, productFieldSerializer);
        kryo.register(ProductDto.class, productDtoFieldSerializer);
        kryo.register(Upc.class);
        kryo.register(UpcDto.class);
    }
}
