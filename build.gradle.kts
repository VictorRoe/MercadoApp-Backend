plugins {
	kotlin("jvm") version "2.4.10"
	kotlin("plugin.spring") version "2.4.10"
	id("org.springframework.boot") version "4.0.8"
	id("io.spring.dependency-management") version "1.1.7"
	id("dev.detekt") version("2.0.0-alpha.6")
	jacoco
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

// Patched versions above what Spring Boot 4.0.8 manages, to fix known CVEs
extra["tomcat.version"] = "11.0.27"
extra["jackson-2-bom.version"] = "2.21.8"
extra["jackson-bom.version"] = "3.1.8"

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
	finalizedBy(tasks.jacocoTestReport)
}

jacoco {
	toolVersion = "0.8.13"
}


// Generated (MapStruct) and data-only classes are not meaningful for coverage
val coverageExclusions = listOf(
	"**/mapper/*Impl*",
	"**/entity/**",
	"**/dto/**",
	"**/model/**",
	"**/MercadoappApplication*"
)

fun JacocoReportBase.excludeFromCoverage() {
	classDirectories.setFrom(files(classDirectories.files.map { fileTree(it) { exclude(coverageExclusions) } }))
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
	excludeFromCoverage()
	reports {
		xml.required = true
		html.required = true
	}
}

tasks.jacocoTestCoverageVerification {
	dependsOn(tasks.test)
	excludeFromCoverage()
	violationRules {
		rule {
			limit {
				counter = "LINE"
				minimum = "0.80".toBigDecimal()
			}
		}
	}
}

tasks.check {
	dependsOn(tasks.jacocoTestCoverageVerification)
}

detekt {
	ignoreFailures = true
}