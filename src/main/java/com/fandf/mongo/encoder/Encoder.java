package com.fandf.mongo.encoder;

public interface Encoder {
    
    public boolean isNullField();
    
    public String getFieldName();
    
    public Object encode();
    
    public void setWithoutCascade(boolean withoutCascade);
    
}
