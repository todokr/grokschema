import grokschema.core.Config
import grokschema.core.MetadataLoader
@main def main() =
  val config = Config(
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost:2345/example",
    "root",
    "root"
  )
  val loader = MetadataLoader(config)
  val refs = loader.loadReferences()
  println(refs)

  val schema = loader.loadSchema(refs)
  println(schema)
