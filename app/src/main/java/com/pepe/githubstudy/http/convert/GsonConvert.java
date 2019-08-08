package com.pepe.githubstudy.http.convert;

import com.google.gson.Gson;
import com.pepe.githubstudy.http.Converter;

/**
 * @author 1one
 * @date 2019/8/7.
 */
public class GsonConvert implements Converter {
    @Override
    public <T> T convert(String value, Class<T> type){
        return new Gson().fromJson(value,type);
    }
}
