plugins {
	java
	id("org.springframework.boot") version "3.3.0"
	id("io.spring.dependency-management") version "1.1.5"
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
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-cache")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	compileOnly("org.projectlombok:lombok")
	implementation("com.influxdb:influxdb-client-java:7.1.0")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
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