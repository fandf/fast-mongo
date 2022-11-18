package com.fandf.mongo.decoder;

import com.fandf.mongo.BaseEntity;
import com.fandf.mongo.annotations.Default;
import com.fandf.mongo.annotations.Embed;
import com.fandf.mongo.exception.AnnotationException;
import com.fandf.mongo.utils.FieldUtil;
import com.fandf.mongo.utils.MapperUtil;
import com.mongodb.DBObject;
import java.lang.reflect.Field;

public class EmbedDecoder extends AbstractDecoder{
        
    public EmbedDecoder(Field field, DBObject dbo){
        super(field);
        String fieldName = field.getName();
        Embed embed = field.getAnnotation(Embed.class);
        String name = embed.name();
        if(!name.equals(Default.NAME)){
            fieldName = name;
        }
        value = dbo.get(fieldName);
    }
    
    @Override
    public void decode(Object obj){
        Class<?> type = field.getType();
        if(type.isEnum()){
            FieldUtil.set(obj, field, Enum.valueOf((Class<Enum>)type, (String)value));
            return;
        }
        Object o = MapperUtil.fromDBObject(field.getType(), (DBObject)value);
        FieldUtil.set(obj, field, o);
        
        //tip for wrong use of @Embed
        if(o instanceof BaseEntity){
            throw new AnnotationException("The Embed object should not be BuguEntity!");
        }
    }
    
}
