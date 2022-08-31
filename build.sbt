import com.amazonaws.auth.{AWSCredentialsProviderChain, DefaultAWSCredentialsProviderChain}
import com.amazonaws.auth.profile.ProfileCredentialsProvider

name := "spark-mssql-connector"

organization := "com.microsoft.sqlserver.jdbc.spark"

version := "1.0.2-aiq1"

scalaVersion := "2.11.12"
crossScalaVersions := Seq("2.11.12", "2.12.15")
val sparkVersion = "2.4.7"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
  // Spark Testing Utilities
  "org.apache.spark" %% "spark-core" % sparkVersion % "test" classifier
      "tests",
  "org.apache.spark" %% "spark-sql" % sparkVersion% "test" classifier
      "tests",
  "org.apache.spark" %% "spark-catalyst" % sparkVersion % "test" classifier
      "tests",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "com.novocode" % "junit-interface" % "0.11" % "test",

  //SQLServer JDBC jars
  "com.microsoft.sqlserver" % "mssql-jdbc" % "8.4.1.jre8"

)

scalacOptions := Seq("-unchecked", "-deprecation", "evicted")

// Exclude scala-library from this fat jar. The scala library is already there in spark package.
assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)

val s3Base: String = "s3://s3-us-east-1.amazonaws.com/aiq-artifacts"

// Loads the credentials from .aws/credentials
s3CredentialsProvider := { (bucket: String) =>
  new AWSCredentialsProviderChain(
    new ProfileCredentialsProvider("default"),
    DefaultAWSCredentialsProviderChain.getInstance()
  )
}
publishMavenStyle := true
publishTo := Some("AIQ Snapshots" at s"$s3Base/app-bin/snapshots/com/microsoft/azure/spark-mssql-connector/")
