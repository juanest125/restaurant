package co.com.jestma.config;

import co.com.jestma.api.helper.SecurityJwt;
import co.com.jestma.usecase.utility.ResponseUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

@Configuration
@ComponentScan(basePackages = "co.com.jestma.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {
        @Bean
        public ResponseUtils userResponseUtils(){
                return ResponseUtils.factory();
        }

        @Bean
        public SecurityJwt securityJwt(){
                return new SecurityJwt(PasswordEncoderFactories.createDelegatingPasswordEncoder());
        }
}
