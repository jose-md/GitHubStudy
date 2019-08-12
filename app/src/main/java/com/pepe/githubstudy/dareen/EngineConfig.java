package com.pepe.githubstudy.dareen;

/**
 * @author 1one
 * @date 2019/8/7.
 */
public class EngineConfig {
    final IHttpRequest engineRequest;
    final Converter converter;
    public EngineConfig(Builder builder) {
        engineRequest = builder.engineRequest;
        converter = builder.converter;
    }

    public Converter getConverter() {
        return converter;
    }

    public IHttpRequest getEngineRequest() {
        return engineRequest;
    }

    public static class Builder{
        IHttpRequest engineRequest;
        Converter converter;

        public Builder converter(Converter converter){
            this.converter = converter;
            return this;
        }

        public  Builder engineRequest(IHttpRequest engineRequest){
            this.engineRequest = engineRequest;
            return this;
        }

        public EngineConfig builder(){
            // 如果上层不配置返回默认
            if(converter == null){
                converter = Converter.DEFAULT_CONVERTER;
            }
            return new EngineConfig(this);
        }
    }
}
