package com.tisson.demo.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import com.tisson.demo.common.trans.TranslateHandler;
import com.tisson.demo.common.trans.TranslateResolver;


@Configuration
public class TranslationInitializingConfig  implements InitializingBean{
    @Autowired
    private RequestMappingHandlerAdapter adapter;
    @Override
    public void afterPropertiesSet() throws Exception {
        //为什么new ArrayList,因为adapter返回的list是Collections.unmodifiableList
        List<HandlerMethodReturnValueHandler> returnValueHandlers =
                new ArrayList<HandlerMethodReturnValueHandler>(adapter.getReturnValueHandlers());
        this.decorateHandlers(returnValueHandlers);
        adapter.setReturnValueHandlers(returnValueHandlers);
        List<HandlerMethodArgumentResolver> argumentResolvers =
                new ArrayList<HandlerMethodArgumentResolver>(adapter.getArgumentResolvers());
        this.decorateArgumentResolver(argumentResolvers);
        adapter.setArgumentResolvers(argumentResolvers);
    }

    private void decorateArgumentResolver(List<HandlerMethodArgumentResolver> resolvers) {
        for(HandlerMethodArgumentResolver resolver:resolvers) {
            if(resolver instanceof RequestResponseBodyMethodProcessor) {
                TranslateResolver decorator =
                        new TranslateResolver((RequestResponseBodyMethodProcessor)resolver);
                int index = resolvers.indexOf(resolver);
                resolvers.set(index, decorator);
                break;
            }
        }
    }

    private void decorateHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        for (HandlerMethodReturnValueHandler handler : handlers) {
            if (handler instanceof RequestResponseBodyMethodProcessor) {
                TranslateHandler decorator = new TranslateHandler(
                        (RequestResponseBodyMethodProcessor) handler);
                int index = handlers.indexOf(handler);
                handlers.set(index, decorator);
                break;
            }
        }
    }
}
