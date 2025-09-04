package com.sportygroup.providerapi.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.sportygroup.providerapi.annotation.UseDeserializer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.nio.charset.StandardCharsets;

public record UseDeserializerArgumentResolver(ObjectMapper baseMapper) implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UseDeserializer.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        var ann = parameter.getParameterAnnotation(UseDeserializer.class);
        var deserClass = ann.value();

        @SuppressWarnings({"unchecked"})
        Class<Object> paramType = (Class<Object>) parameter.getParameterType();

        var request = webRequest.getNativeRequest(HttpServletRequest.class);
        byte[] body = StreamUtils.copyToByteArray(request.getInputStream());

        try {
            var mapper = baseMapper.copy();
            var descInstance = deserClass.getDeclaredConstructor().newInstance();

            var module = new SimpleModule().addDeserializer(paramType, descInstance);
            mapper.registerModule(module);
            return mapper.readValue(body, mapper.constructType(parameter.getGenericParameterType()));
        } catch (Exception ex) {
            String snippet = new String(body, StandardCharsets.UTF_8);
            String message = "Failed to deserialize request body with %s into %s. Body: %s"
                    .formatted(deserClass.getSimpleName(), paramType.getSimpleName(), snippet);
            throw new Exception(message, ex);
        }
    }
}
