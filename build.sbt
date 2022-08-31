import com.amazonaws.auth.{AWSCredentialsProviderChain, DefaultAWSCredentialsProviderChain}
import com.amazonaws.auth.profile.ProfileCredentialsProvider

val pkgOrg = Seq("com", "microsoft", "sqlserver", "jdbc", "spark")
val pkgName = "spark-mssql-connector"
val s3Base: String = "s3://s3-us-east-1.amazonaws.com/aiq-artifacts"
val sparkVersion = "2.4.7"

name := pkgName

organization := pkgOrg.mkString(".")

version := "1.0.2-aiq1"

scalaVersion := "2.11.12"
crossScalaVersions := Seq("2.11.12", "2.12.15")

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

s3CredentialsProvider := { (bucket: String) =>
  new AWSCredentialsProviderChain(
    new ProfileCredentialsProvider("default"),
    DefaultAWSCredentialsProviderChain.getInstance()
  )
}
publishMavenStyle := true
publishTo := Some("AIQ Snapshots" at s"$s3Base/app-bin/snapshots/${pkgOrg.mkString("/")}/${pkgName}/")

// Publish fails to create docs for some reason
sources in (Compile, doc) := Seq.empty
