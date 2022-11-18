package com.fandf.mongo.utils;

import java.lang.reflect.Field;

import com.fandf.mongo.annotations.Id;
import com.fandf.mongo.cache.FieldsCache;
import org.bson.types.ObjectId;

public final class IdUtil {

    /**
     * Convert the id string to object, which matching the id data in mongoDB.
     *
     * @param clazz
     * @param idStr
     * @return
     */
    public static Object toDbId(Class<?> clazz, String idStr) {
        if (StringUtil.isBlank(idStr)) {
            return null;
        }
        Object result = null;
        Field idField = FieldsCache.getInstance().getIdField(clazz);
        Id idAnnotation = idField.getAnnotation(Id.class);

        //the idStr maybe illegal value, have to catch exception here
        try {
            switch (idAnnotation.type()) {
                case AUTO_GENERATE:
                    result = new ObjectId(idStr);
                    break;
                case AUTO_INCREASE:
                    result = Long.parseLong(idStr);
                    break;
                case USER_DEFINE:
                    result = idStr;
                    break;
            }
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("idStr can not convert to legal database ID.", ex);
        }
        return result;
    }

}
