package com.fandf.mongo.encoder;

import com.fandf.mongo.annotations.Default;
import com.fandf.mongo.annotations.Property;

import java.lang.reflect.Field;

public class PropertyEncoder extends AbstractEncoder{
    
    public PropertyEncoder(Object obj, Field field){
        super(obj, field);
    }
    
    @Override
    public String getFieldName(){
        String fieldName = field.getName();
        Property property = field.getAnnotation(Property.class);
        if(property != null){
            String name = property.name();
            if(!name.equals(Default.NAME)){
                fieldName = name;
            }
        }
        return fieldName;
    }
    
    @Override
    public Object encode(){
        //if the field is enum type, save as String
        Class<?> type = field.getType();
        if(type.isEnum()){
            return value.toString();
        }
        return value;
    }
    
}
