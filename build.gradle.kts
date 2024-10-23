plugins {
    id("java")
    `maven-publish`
}

group = "net.botwithus.cpu"
version = "1.0.0"

repositories {
    mavenLocal()
    mavenCentral()
    maven { setUrl("https://jitpack.io") }
    maven {
        setUrl("https://nexus.botwithus.net/repository/maven-snapshots/")
    }
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "20"
    targetCompatibility = "20"
    options.compilerArgs.add("--enable-preview")
}

val copyJar by tasks.register<Copy>("copyJar") {
    from("build/libs/")
    into("${System.getProperty("user.home")}\\BotWithUs\\scripts\\local\\")
    include("*.jar")
}

configurations {
    create("includeInJar") {
        this.isTransitive = false
    }
}

tasks.named<Jar>("jar") {
    from({
        configurations["includeInJar"].map { zipTree(it) }
    })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    finalizedBy(copyJar)
}

dependencies {
    testImplementation("net.bytebuddy:byte-buddy:1.15.4")
    implementation("net.botwithus.rs3:botwithus-api:1.0.0-SNAPSHOT")
    implementation("net.botwithus.xapi.public:api:1.0.0-SNAPSHOT")
    "includeInJar"("net.botwithus.xapi.public:api:1.0.0-SNAPSHOT")
    implementation("com.google.code.gson:gson:2.10.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    testImplementation("org.mockito:mockito-core:3.6.28")
    testImplementation("org.mockito:mockito-junit-jupiter:3.6.28")
    testImplementation("com.google.flogger:flogger:0.7.4")
    testImplementation("com.google.flogger:flogger-system-backend:0.7.4")
    testImplementation("com.google.guava:guava:32.1.2-jre")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
     testLogging {
        events("passed", "skipped", "failed")

        showStandardStreams = true
    }
    jvmArgs("--enable-preview")
}
