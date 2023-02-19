import grokschema.core.Config
import grokschema.core.MetadataLoader
@main def main() = 
  val config = Config(
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost:2345/example",
    "root",
    "root",
  )
  val loader = MetadataLoader(config)
  val refs = loader.loadReferences()
  val deps = refs.dependencies("target_users")
  println(deps)
  
  val tables = loader.loadTables()
  println(tables)
