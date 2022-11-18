package com.fandf.mongo.encoder;

import com.fandf.mongo.BaseEntity;
import com.fandf.mongo.access.InternalDao;
import com.fandf.mongo.annotations.Default;
import com.fandf.mongo.annotations.Ref;
import com.fandf.mongo.cache.DaoCache;
import com.fandf.mongo.utils.FieldUtil;
import com.fandf.mongo.utils.ReferenceUtil;

import java.lang.reflect.Field;

@SuppressWarnings("unchecked")
public class RefEncoder extends AbstractEncoder {

    private final Ref ref;

    public RefEncoder(Object obj, Field field) {
        super(obj, field);
        ref = field.getAnnotation(Ref.class);
    }

    @Override
    public String getFieldName() {
        String fieldName = field.getName();
        String name = ref.name();
        if (!name.equals(Default.NAME)) {
            fieldName = name;
        }
        return fieldName;
    }

    @Override
    public Object encode() {
        BaseEntity entity = (BaseEntity) value;
        if (!withoutCascade) {
            if (ref.cascade().toUpperCase().indexOf(Default.CASCADE_CREATE) != -1 || ref.cascade().toUpperCase().indexOf(Default.CASCADE_UPDATE) != -1) {
                Class<?> cls = FieldUtil.getRealType(field);
                InternalDao dao = DaoCache.getInstance().get(cls);
                dao.saveWithoutCascade(entity, true);
            }
        }

        return ReferenceUtil.toDbReference(ref, entity.getClass(), entity.getId());
    }

}
