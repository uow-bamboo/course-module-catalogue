package uk.ac.warwick.camcat.system.context

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource

@Configuration
@EnableJpaRepositories(
  basePackages = ["uk.ac.warwick.camcat.data"],
  entityManagerFactoryRef = "entityManagerFactory"
)
class DataContext {
  @Bean
  @Primary
  @ConfigurationProperties(prefix = "spring.datasource")
  fun dataSource(): DataSource = DataSourceBuilder.create().build()

  @Bean
  @Primary
  fun entityManagerFactory(
    dataSource: DataSource,
    builder: EntityManagerFactoryBuilder
  ): LocalContainerEntityManagerFactoryBean =
    builder
      .dataSource(dataSource)
      .packages("uk.ac.warwick.camcat.data")
      .persistenceUnit("primary")
      .build()

  @Bean
  @Primary
  fun transactionManager(
    entityManagerFactory: EntityManagerFactory
  ): PlatformTransactionManager = JpaTransactionManager(entityManagerFactory)
}
