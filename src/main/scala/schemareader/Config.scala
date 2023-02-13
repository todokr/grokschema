package schemareader

trait ConfigLoader:
  def load() = ???

case class JdbcConfig(
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
