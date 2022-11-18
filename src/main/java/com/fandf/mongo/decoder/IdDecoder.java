package com.fandf.mongo.decoder;

import com.fandf.mongo.utils.FieldUtil;
import com.fandf.mongo.utils.Operator;
import com.mongodb.DBObject;
import java.lang.reflect.Field;

public class IdDecoder extends AbstractDecoder{
    
    public IdDecoder(Field field, DBObject dbo){
        super(field);
        value = dbo.get(Operator.ID);
    }
    
    @Override
    public void decode(Object obj){
        FieldUtil.set(obj, field, value.toString());
    }
    
}
