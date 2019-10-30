package uk.ac.warwick.camcat.context

import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.junit4.SpringRunner

@ActiveProfiles("integrationTest")
@AutoConfigureEmbeddedDatabase(beanName = "dataSource")
@RunWith(SpringRunner::class)
@SpringBootTest
@Sql(
  scripts = ["/sits-schema.sql", "/sits-data.sql"],
  config = SqlConfig(dataSource = "sitsDataSource", transactionManager = "sitsTransactionManager")
)
abstract class ContextTest
