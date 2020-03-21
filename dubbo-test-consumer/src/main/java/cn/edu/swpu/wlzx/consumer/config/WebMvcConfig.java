package cn.edu.swpu.wlzx.consumer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 描述：配置静态资源映射
 * @author gongsir
 * @date 2020/3/21 17:40
 * 编码不要畏惧变化，要拥抱变化
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${image.location}")
    private String path;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploadImg/**").addResourceLocations("file:"+path);
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
}
