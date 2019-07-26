package com.tisson.demo.common.trans;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TransConvter {
    private final static Logger LOGGER = LoggerFactory.getLogger(TransConvter.class);
    private TransConvter() {}

    public Object trans(Object obj,Translate translate) throws Exception {
        String category = translate.category();
        String dictKey = translate.dictKey();
        String setField = translate.setField();
        String setMethod = translate.setMethod();
        LOGGER.info("conver==category:[{}],dictKey:[{}],setField:[{}],setMethod:[{}]",
                category,dictKey,setField,setMethod);
        Field field = obj.getClass().getField(setField);
        Method method = obj.getClass().getMethod(setMethod,field.getType());
        if(field.getType().equals(int.class)) {
            // TODO
        }else if(field.getType().equals(byte.class)) {
            // TODO
        }else if(field.getType().equals(short.class)) {
            // TODO
        }else if(field.getType().equals(long.class)) {
            // TODO
        }else if(field.getType().equals(float.class)) {
            // TODO
        }else if(field.getType().equals(double.class)) {
            // TODO
        }else if(field.getType().equals(char.class)) {
            // TODO
        }else if(field.getType().equals(boolean.class)) {
            // TODO
        }else if(field.getType().equals(String.class)) {
            // TODO
        }else if(field.getType().equals(Byte.class)) {
            // TODO
        }else if(field.getType().equals(Short.class)) {
            // TODO
        }else if(field.getType().equals(Integer.class)) {
            // TODO
        }else if(field.getType().equals(Long.class)) {
            // TODO
        }else if(field.getType().equals(Float.class)) {
            // TODO
        }else if(field.getType().equals(Double.class)) {
            // TODO
        }else if(field.getType().equals(Character.class)) {
            // TODO
        }else if(field.getType().equals(Boolean.class)) {
            // TODO
        }
        return obj;
    }
}