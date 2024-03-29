import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.6.4"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.10"
	kotlin("plugin.spring") version "1.6.10"
	kotlin("plugin.jpa") version "1.4.30"
	jacoco
}

group = "com.mercadolivro"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}


dependencies {

	implementation("org.springframework.boot:spring-boot-starter-validation:2.6.4")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	// https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt
	implementation("io.jsonwebtoken:jjwt:0.9.1")

	implementation("mysql:mysql-connector-java")
	implementation("org.flywaydb:flyway-core:8.5.4")
	implementation("org.flywaydb:flyway-mysql:8.5.4")

	implementation("io.springfox:springfox-swagger2:3.0.0")
	implementation("io.springfox:springfox-swagger-ui:3.0.0")
	implementation("io.springfox:springfox-boot-starter:3.0.0")

	testImplementation("org.springframework.boot:spring-boot-starter-test")

	// https://mvnrepository.com/artifact/io.mockk/mockk
	testImplementation("io.mockk:mockk:1.13.4")
	testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
	// https://mvnrepository.com/artifact/org.springframework.security/spring-security-test
	testImplementation("org.springframework.security:spring-security-test:5.5.2")


}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
