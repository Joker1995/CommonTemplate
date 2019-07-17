package com.tisson.demo.common.trans;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;


/**
 *    方法返回结果转换,返回类型成员变量带@Translate会根据配置参数进行翻译
 * @author Administrator
 *
 */
public class TranslateHandler implements HandlerMethodReturnValueHandler {
	private final static Logger LOGGER = LoggerFactory.getLogger(TranslateHandler.class);
	private RequestResponseBodyMethodProcessor target;
	
	public TranslateHandler(RequestResponseBodyMethodProcessor target) {
		this.target = target;
	}

	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		return (AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ResponseBody.class) ||
				returnType.hasMethodAnnotation(ResponseBody.class));
	}

	@Override
	public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest) throws Exception {
		Class<?> clz = returnValue.getClass();
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
		target.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
	}
}
