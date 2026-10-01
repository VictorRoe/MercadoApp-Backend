plugins {
	kotlin("jvm") version "2.4.10"
	kotlin("plugin.spring") version "2.4.10"
	id("org.springframework.boot") version "4.0.6"
	id("io.spring.dependency-management") version "1.1.7"
	id("dev.detekt") version("2.0.0-alpha.6")
	kotlin("plugin.jpa") version "2.2.21"
	kotlin("kapt") version "2.2.21"

}

group = "dev.victorroe"
version = "1.0.0"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {

//	Spring Web Starter
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("tools.jackson.module:jackson-module-kotlin")

//	Swagger / OpenAPI
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.3")

//	H2 Persistence
	implementation("org.springframework.boot:spring-boot-h2console")
	runtimeOnly("com.h2database:h2")

//	PostgresDB

	runtimeOnly("org.postgresql:postgresql")

//	JPA

	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

//	Spring Security
	implementation("org.springframework.boot:spring-boot-starter-security")

//	Bean Validation
	implementation("org.springframework.boot:spring-boot-starter-validation")

//	JJWT
	implementation("io.jsonwebtoken:jjwt-api:0.12.6")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

//	Spring Boot DevTools
	developmentOnly("org.springframework.boot:spring-boot-devtools")


//	MapStruct
	implementation("org.mapstruct:mapstruct:1.6.3")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
	kapt("org.mapstruct:mapstruct-processor:1.6.3")

//	Testing Libraries
	testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.springframework.boot:spring-boot-starter-session-jdbc-test")
	testImplementation("org.springframework.boot:spring-boot-starter-actuator-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
	}
}

allOpen{
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

detekt {
	ignoreFailures = true
}