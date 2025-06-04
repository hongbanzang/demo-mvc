package com.foo.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.multipart.support.StandardServletMultipartResolver
import org.springframework.web.servlet.ViewResolver
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.util.pattern.PathPatternParser
import org.thymeleaf.spring5.SpringTemplateEngine
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver
import org.thymeleaf.spring5.view.ThymeleafViewResolver

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = ["com.foo.myapp"])
class WebConfig : WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/static/")
    }

    @Bean
    fun templateResolver(): SpringResourceTemplateResolver {
        val tr = SpringResourceTemplateResolver()
        tr.prefix = "/WEB-INF/views/"
        tr.suffix = ".html"
        tr.setTemplateMode("HTML")
        return tr
    }

    @Bean
    fun templateEngine(): SpringTemplateEngine {
        val te = SpringTemplateEngine()
        te.setTemplateResolver(templateResolver())
        return te
    }

    @Bean
    fun viewResolver(): ViewResolver {
        val vr = ThymeleafViewResolver()
        vr.templateEngine = templateEngine()
        vr.order = 1
        vr.setCharacterEncoding("UTF-8")
        return vr
    }

    @Bean
    fun multipartResolver(): StandardServletMultipartResolver {
        val multipartResolover = StandardServletMultipartResolver()
        multipartResolover.setStrictServletCompliance(true)
        return multipartResolover
    }

    override fun configurePathMatch(configurer: PathMatchConfigurer) {
        configurer.setPatternParser(PathPatternParser())
    }
}
