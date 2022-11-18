package com.fandf.mongo.geo;


import com.fandf.mongo.utils.MapperUtil;

public abstract class GeoJSON {
    
    protected String type;

    public String getType() {
        return type;
    }
    
    @Override
    public String toString() {
        return MapperUtil.toDBObject(this, true).toString();
    }
    
}
