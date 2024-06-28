plugins {
	java
	id("org.springframework.boot") version "3.3.1"
	id("io.spring.dependency-management") version "1.1.5"
	id("com.github.ben-manes.versions") version "0.51.0"
	id("com.autonomousapps.dependency-analysis") version "1.32.0"
	id("jacoco")
}

group = "com.tanyem"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator:3.3.1")
	implementation("org.springframework.boot:spring-boot-starter-cache:3.3.1")
	implementation("org.springframework.boot:spring-boot-starter-validation:3.3.1")
	implementation("org.springframework.boot:spring-boot-starter-web:3.3.1")
	implementation("org.springframework.boot:spring-boot-starter-webflux:3.3.1")
	compileOnly("org.projectlombok:lombok:1.18.34")
	implementation("com.influxdb:influxdb-client-java:7.1.0")
	implementation("com.fasterxml.jackson.core:jackson-annotations:2.17.1")
	implementation("com.fasterxml.jackson.core:jackson-core:2.17.1")
	implementation("com.fasterxml.jackson.core:jackson-databind:2.17.1")
	implementation("io.projectreactor:reactor-core:3.6.7")
	implementation("jakarta.validation:jakarta.validation-api:3.1.0")
	implementation("org.apache.tomcat.embed:tomcat-embed-core:10.1.25")
	implementation("org.slf4j:slf4j-api:2.0.13")
	implementation("org.springframework.boot:spring-boot-autoconfigure:3.3.1")
	implementation("org.springframework.boot:spring-boot:3.3.1")
	implementation("org.springframework:spring-beans:6.1.10")
	implementation("org.springframework:spring-context:6.1.10")
	implementation("org.springframework:spring-core:6.1.10")
	implementation("org.springframework:spring-web:6.1.10")
	implementation("org.springframework:spring-webflux:6.1.10")
	implementation("org.springframework:spring-webmvc:6.1.10")
	annotationProcessor("org.projectlombok:lombok:1.18.34")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	//testImplementation("io.projectreactor:reactor-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.2")
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
	testImplementation("org.mockito:mockito-core:5.12.0")
	testImplementation("org.springframework.boot:spring-boot-test-autoconfigure:3.3.1")
	testImplementation("org.springframework.boot:spring-boot-test:3.3.1")
	testImplementation("org.springframework:spring-test:6.1.10")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<org.springframework.boot.gradle.tasks.run.BootRun> {
	systemProperties = mapOf(
		"api.key" to (project.findProperty("api.key") ?: ""),
		"influxdb.token" to (project.findProperty("influxdb.token") ?: ""),
		"influxdb.bucket" to (project.findProperty("influxdb.bucket") ?: ""),
		"influxdb.org" to (project.findProperty("influxdb.org") ?: ""),
		"influxdb.url" to (project.findProperty("influxdb.url") ?: ""),
	)
}

tasks.withType<Test> {
	useJUnitPlatform()
	systemProperties = mapOf(
		"api.key" to (project.findProperty("api.key") ?: "EXAMPLE_API_KEY"),
		"influxdb.token" to (project.findProperty("influxdb.token") ?: "EXAMPLE_INFLUXDB_TOKEN"),
		"influxdb.bucket" to (project.findProperty("influxdb.bucket") ?: "EXAMPLE_BUCKET"),
		"influxdb.org" to (project.findProperty("influxdb.org") ?: "EXAMPLE_ORG"),
		"influxdb.url" to (project.findProperty("influxdb.url") ?: "http://localhost:8086?timeout=5000&logLevel=BASIC"),
	)
}

tasks.withType<JavaCompile> {
	options.compilerArgs.add("-Xlint:unchecked")
}