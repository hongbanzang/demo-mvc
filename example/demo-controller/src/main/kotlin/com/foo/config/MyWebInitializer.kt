package com.foo.config

import org.springframework.web.filter.CharacterEncodingFilter
import org.springframework.web.filter.HiddenHttpMethodFilter
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer
import javax.servlet.Filter
import javax.servlet.MultipartConfigElement
import javax.servlet.ServletRegistration.Dynamic

class MyWebInitializer : AbstractAnnotationConfigDispatcherServletInitializer() {
    companion object {
        private const val MAX_UPLOAD_SIZE = 2 * 1024 * 1024L
    }

    override fun getRootConfigClasses(): Array<Class<*>>? = null

    override fun getServletConfigClasses(): Array<Class<*>> {
        return arrayOf(
            WebConfig::class.java,
            SecurityConfig::class.java,
            AppConfig::class.java
        )
    }

    override fun getServletMappings(): Array<String> = arrayOf("/")

    override fun getServletFilters(): Array<Filter> {
        val cef = CharacterEncodingFilter()
        cef.encoding = "UTF-8"
        cef.setForceEncoding(true)
        return arrayOf(HiddenHttpMethodFilter(), cef)
    }

    override fun customizeRegistration(registration: Dynamic) {
        registration.setMultipartConfig(MultipartConfigElement(null, 2097152L, MAX_UPLOAD_SIZE * 2L, 1024 * 1024))
    }
}
