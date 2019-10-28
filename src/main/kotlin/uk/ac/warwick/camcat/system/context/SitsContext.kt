package uk.ac.warwick.camcat.system.context

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource

@Configuration
@EnableJpaRepositories(
  basePackages = ["uk.ac.warwick.camcat.sits"],
  entityManagerFactoryRef = "sitsEntityManagerFactory"
)
class SitsContext {
  @Bean(name = ["sitsDataSource"])
  @ConfigurationProperties(prefix = "spring.datasource.sits")
  fun dataSource(): DataSource = DataSourceBuilder.create().build()

  @Bean(name = ["sitsEntityManagerFactory"])
  fun entityManagerFactory(
    builder: EntityManagerFactoryBuilder
  ): LocalContainerEntityManagerFactoryBean =
    builder
      .dataSource(dataSource())
      .packages("uk.ac.warwick.camcat.sits")
      .persistenceUnit("sits")
      .build()

  @Bean(name = ["sitsTransactionManager"])
  fun transactionManager(
    @Qualifier("sitsEntityManagerFactory") entityManagerFactory: EntityManagerFactory
  ): PlatformTransactionManager = JpaTransactionManager(entityManagerFactory)
}
