package cn.edu.swpu.wlzx.consumer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 描述：使用swagger2生成api文档
 * @author gongsir
 * @date 2020/3/21 17:24
 * 编码不要畏惧变化，要拥抱变化
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //api扫描路径
                .apis(RequestHandlerSelectors.basePackage("cn.edu.swpu.wlzx.consumer.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * api文档web界面信息
     * @return APIInfo
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("安全多方计算平台API")
                .description("更信人：龚涛 曾文杰")
                .contact(gongTao())
                .version("v1.0.0")
                .build();
    }

    private Contact gongTao() {
        return new Contact("龚涛","https://github.com/gongsir0630","gongsir0630@gmail.com");
    }
}
