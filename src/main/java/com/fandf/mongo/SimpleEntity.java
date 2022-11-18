package com.fandf.mongo;

import java.io.Serializable;

import com.fandf.mongo.annotations.Id;
import com.fandf.mongo.utils.StringUtil;
import org.bson.types.ObjectId;

public abstract class SimpleEntity implements BaseEntity, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    protected String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * Get the time when the document is created, in milliseconds.
     * @return 
     */
    public long getTimestamp() {
        if(StringUtil.isBlank(id)){
            return -1;
        }
        ObjectId oid = new ObjectId(id);
        return oid.getTimestamp() * 1000L;
    }
    
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof SimpleEntity)) {
            return false;
        }
        SimpleEntity o = (SimpleEntity)other;
        String oid = o.getId();
        if( StringUtil.isBlank(id) || StringUtil.isBlank(oid) ){
            return false;
        }
        return id.equalsIgnoreCase(oid);
    }
    
    @Override
    public int hashCode() {
        if(StringUtil.isBlank(id)){
            return -1;
        }
        return new ObjectId(id).hashCode();
    }
    
    @Override
    public String toString(){
        return id;
    }

}
