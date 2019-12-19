import com.moowork.gradle.node.npm.NpmTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
  id("org.springframework.boot") version "2.2.2.RELEASE"
  id("io.spring.dependency-management") version "1.0.8.RELEASE"
  kotlin("jvm") version "1.3.61"
  kotlin("plugin.spring") version "1.3.61"
  kotlin("plugin.jpa") version "1.3.61"
  id("com.moowork.node") version "1.3.1"
}

group = "uk.ac.warwick"
version = "1.0.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

val developmentOnly by configurations.creating
configurations {
  runtimeClasspath {
    extendsFrom(developmentOnly)
  }
  compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
  }
}

repositories {
  mavenCentral()
  maven {
    url = uri("https://mvn.elab.warwick.ac.uk/nexus/content/groups/public-anonymous")
  }
  maven {
    url = uri("https://oss.jfrog.org/artifactory/libs-snapshot")
  }
}

dependencyManagement {
  imports {
    mavenBom("org.springframework.cloud:spring-cloud-dependencies:Hoxton.RELEASE")
  }
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-freemarker")
  implementation("org.springframework.boot:spring-boot-starter-mail")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("com.googlecode.owasp-java-html-sanitizer:owasp-java-html-sanitizer:20191001.1")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  developmentOnly("org.springframework.boot:spring-boot-devtools")
  runtimeOnly("org.postgresql:postgresql")
  runtimeOnly("com.oracle.ojdbc:ojdbc8")
  testImplementation("com.h2database:h2")
  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework.security:spring-security-test")
  implementation("org.flywaydb:flyway-core:6.0.3")
  implementation("uk.ac.warwick.sso:sso-client:2.76")
  implementation("io.sixhours:memcached-spring-boot-starter:2.0.0-SNAPSHOT")
  implementation("io.sixhours:memcached-spring-boot-autoconfigure:2.0.0-SNAPSHOT")
  implementation("com.amazonaws:elasticache-java-cluster-client")
  runtimeOnly("org.springframework.cloud:spring-cloud-context") //  // TODO bin this off, it should be optional
  implementation("uk.ac.warwick.util:warwickutils-core:20190916")
  implementation("uk.ac.warwick.util:warwickutils-web:20190916")
  implementation("org.springframework.boot:spring-boot-starter-quartz")
  implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.0")
  implementation("com.itextpdf:kernel:7.1.8")
  implementation("com.itextpdf:html2pdf:2.1.5")

  implementation("net.rakugakibox.spring.boot:logback-access-spring-boot-starter:2.7.1")
  compile("uk.ac.warwick:warwick-logging:1.2:all")
  compileOnly("ch.qos.logback:logback-classic:1.2.3")
  compileOnly("net.logstash.logback:logstash-logback-encoder:5.3") {
    exclude(group = "com.fasterxml.jackson.core")
  }
  compileOnly("org.slf4j:slf4j-api:1.7.26")
  compile("org.slf4j:log4j-over-slf4j:1.7.26")
  compile("org.slf4j:jcl-over-slf4j:1.7.26")
  compileOnly("commons-logging:commons-logging:1.2")

  testImplementation("io.zonky.test:embedded-database-spring-test:1.5.1")
  testImplementation("pl.allegro.tech:embedded-elasticsearch:2.7.0")
  testImplementation("net.sourceforge.htmlunit:htmlunit")

  implementation("com.vladsch.flexmark:flexmark-all:0.50.44")
  implementation("com.github.mfornos:humanize-slim:1.2.2")
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "1.8"
  }
}

tasks.named("classes") {
  dependsOn(":assets")
}

tasks.register<NpmTask>("installNpmDependencies") {
  inputs.file("package.json").withPathSensitivity(PathSensitivity.RELATIVE)
  outputs.dir("node_modules")

  setArgs(mutableListOf("ci"))
}

tasks.register<NpmTask>("assets") {
  dependsOn(":installNpmDependencies")

  inputs.files("webpack.config.babel.js", "package.json").withPathSensitivity(PathSensitivity.RELATIVE)
  inputs.dir("build-tooling").withPathSensitivity(PathSensitivity.RELATIVE)
  inputs.dir("src/main/assets").withPathSensitivity(PathSensitivity.RELATIVE)
  outputs.dir("build/resources/main/static/assets")
  outputs.cacheIf { true }

  setArgs(mutableListOf("run", "build"))
}

tasks.withType<BootJar> {
  archiveFileName.set("app.jar")
  launchScript()

  manifest {
    attributes("Main-Class" to "org.springframework.boot.loader.PropertiesLauncher")
  }
}

tasks.withType<BootRun> {
  classpath += files("external-conf")
}
