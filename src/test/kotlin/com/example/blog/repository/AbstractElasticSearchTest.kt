package com.example.blog.repository

import org.junit.runner.RunWith
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.testcontainers.elasticsearch.ElasticsearchContainer


@RunWith(SpringRunner::class)
@SpringBootTest
@ContextConfiguration(initializers = [AbstractElasticsearchTest.Initializer::class])
@AutoConfigureMockMvc
abstract class AbstractElasticsearchTest {

    companion object {
        const val ELASTICSEARCH_IMAGE = "docker.elastic.co/elasticsearch/elasticsearch:7.17.0"
        const val ES_PASSWORD = "password"

        val container = ElasticsearchContainer(ELASTICSEARCH_IMAGE)
            .apply { withPassword(ES_PASSWORD).withExposedPorts(9200) }
    }

    internal class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
            container.start()

            TestPropertyValues.of(
                "spring.elasticsearch.rest.uris=http://localhost:${container.getMappedPort(9200)}"
            ).applyTo(configurableApplicationContext.environment)
        }
    }
}
