package com.foo.config

import org.apache.commons.dbcp2.BasicDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.JpaVendorAdapter
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.util.Properties
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = ["com.foo.myapp"])
@ComponentScan(basePackages = ["com.foo.myapp"])
class AppConfig {
    @Bean
    fun dataSource(): DataSource {
        val dataSource = BasicDataSource()
        dataSource.driverClassName = "com.mysql.cj.jdbc.Driver"
        dataSource.url = "jdbc:mysql://127.0.0.1:3306/mydb"
        dataSource.connectionProperties = "serverTimezone=UTC;characterEncoding=UTF-8"
        dataSource.username = "scott"
        dataSource.password = "1234"
        dataSource.defaultAutoCommit = false
        return dataSource
    }

    private fun getHibernateProperties(): Properties {
        val props = Properties()
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect")
        props.setProperty("hibernate.show_sql", "false")
        props.setProperty("hibernate.jdbc.batch_size", "3")
        props.setProperty("hibernate.jdbc.fetch_size", "100")
        props.setProperty("hibernate.hbm2ddl.auto", "create")
        return props
    }

    @Bean
    fun jpaVendorAdapter(): JpaVendorAdapter {
        return HibernateJpaVendorAdapter()
    }

    @Bean
    fun entityManagerFactory(): EntityManagerFactory {
        val factoryBean = LocalContainerEntityManagerFactoryBean()
        factoryBean.setPackagesToScan("com.foo.myapp")
        factoryBean.dataSource = dataSource()
        factoryBean.setJpaProperties(getHibernateProperties())
        factoryBean.jpaVendorAdapter = jpaVendorAdapter()
        factoryBean.afterPropertiesSet()
        return factoryBean.nativeEntityManagerFactory
    }

    @Bean
    fun transactionManager(): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory())
    }
}
