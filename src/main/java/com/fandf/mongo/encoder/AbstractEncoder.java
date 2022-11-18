package com.fandf.mongo.encoder;

import com.fandf.mongo.utils.FieldUtil;

import java.lang.reflect.Field;

public abstract class AbstractEncoder implements Encoder{
    
    protected Field field;
    protected Object value;
    protected Class<?> clazz;
    
    protected boolean withoutCascade;
    
    protected AbstractEncoder(Object obj, Field field){
        this.field = field;
        value = FieldUtil.get(obj, field);
        clazz = obj.getClass();
    }
    
    @Override
    public boolean isNullField(){
        return value == null;
    }

    @Override
    public void setWithoutCascade(boolean withoutCascade) {
        this.withoutCascade = withoutCascade;
    }
    
}
