import grokschema.core.Config
import grokschema.core.MetadataLoader

@main def main() = Sandbox.run()

object Sandbox {
  def run() =
    val config = Config(
      "org.postgresql.Driver",
      "jdbc:postgresql://localhost:2345/example",
      "root",
      "root"
    )
    val loader = MetadataLoader(config)
    val refs = loader.loadReferences()
    refs.foreach(println)

    val tables = loader.loadTables()
    tables.foreach(println)
}
