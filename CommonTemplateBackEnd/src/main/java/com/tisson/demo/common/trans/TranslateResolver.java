package com.tisson.demo.common.trans;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;


/**
 * 方法参数转换,参数类型成员变量带@Translate会根据配置参数进行翻译
 * @author Administrator
 *
 */
public class TranslateResolver implements HandlerMethodArgumentResolver{
    private final static Logger LOGGER = LoggerFactory.getLogger(TranslateResolver.class);
    private RequestResponseBodyMethodProcessor target;
    public TranslateResolver(RequestResponseBodyMethodProcessor target) {
        this.target = target;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> clz = parameter.getParameterType();
        Field[] fields = clz.getFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Translate.class)) {
                return true;
            }
        }
        return target.supportsParameter(parameter);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Class<?> clz = parameter.getClass();
        Field[] fields = clz.getFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Translate.class)) {
                Translate translate = field.getAnnotation(Translate.class);
                // TODO
                String category = translate.category();
                String dictKey = translate.dictKey();
                String setField = translate.setField();
                String setMethod = translate.setMethod();
                LOGGER.info("annotation:category--[{}],dictKey--[{}],setField--[{}],setMethod--[{}]",
                        category, dictKey, setField,setMethod);
            }
        }
        return target.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
    }

}