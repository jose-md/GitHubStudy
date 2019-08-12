package com.pepe.githubstudy.dareen;

/**
 * @author 1one
 * @date 2019/8/7.
 */
public interface Converter {
    Converter DEFAULT_CONVERTER = new Converter() {
        @Override
        public <T> T convert(String value, Class<T> type){
            return (T) value;
        }
    };

    <T> T convert(String value, Class<T> type);
}
