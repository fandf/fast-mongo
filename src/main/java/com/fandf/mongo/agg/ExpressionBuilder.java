package com.fandf.mongo.agg;

public final class ExpressionBuilder {
    
    public static CondBuilder cond(){
        return new CondBuilder();
    }
    
    public static BoolBuilder bool(){
        return new BoolBuilder();
    }
    
    public static CompareBuilder compare(){
        return new CompareBuilder();
    }

}
