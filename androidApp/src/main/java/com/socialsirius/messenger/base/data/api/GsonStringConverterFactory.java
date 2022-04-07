package com.socialsirius.messenger.base.data.api;

import androidx.annotation.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class GsonStringConverterFactory extends Converter.Factory {
    private static final MediaType MEDIA_TYPE = MediaType.parse("text/plain");


    @Nullable
    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }

   /* @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] annotations) {
        if (String.class.equals(type))// || (type instanceof Class && ((Class<?>) type).isEnum()))
        {
            return new Converter<String, RequestBody>() {
                @Override
                public RequestBody convert(String value) throws IOException {
                    return RequestBody.create(MEDIA_TYPE, value);
                }
            };
        }
        return null;
    }*/
}
