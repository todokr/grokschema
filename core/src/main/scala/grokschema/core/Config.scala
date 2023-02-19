package grokschema.core

import java.nio.file.Path
import java.nio.charset.StandardCharsets

import scala.io.Source
import java.util.Properties

object Config:
  def loadFromProperties(path: Path): Config =
    val prop = new Properties()
    val reader = Source.fromFile(path.toFile, StandardCharsets.UTF_8.name).bufferedReader
    prop.load(reader)
    
    val driver   = prop.getString(DriverKey)
    val url      = prop.getString(UrlKey)
    val user     = prop.getString(UserKey)
    val password = prop.getString(PasswordKey)
    val schema   = prop.getStringOr(SchemaKey, DefaultSchema)
    Config(driver, url, user, password, schema)
  
  private val DriverKey   = "db.driver"
  private val UrlKey      = "db.url"
  private val UserKey     = "db.user"
  private val PasswordKey = "db.password"
  private val SchemaKey   = "db.schema"
  private val DefaultSchema = "public"

  extension (p: Properties)
    def getString(key: String): String =
      Option(p.getProperty(key)).getOrElse(throw new JdbcConfigException(key))

    def getStringOr(key: String, default: String): String =
      Option(p.getProperty(key)).getOrElse(default)

case class Config(
    driver: String,
    url: String,
    user: String,
    password: String,
    schema: String
):
  override def toString(): String =
    s"""driver=$driver
       |url=$url
       |user=$user
       |password=${password.map(_ => "*").mkString}""".stripMargin

class JdbcConfigException(key: String) extends Exception(s"$key is not set")

