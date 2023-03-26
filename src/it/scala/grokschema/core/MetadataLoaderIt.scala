package grokschema.core

import munit.*
import grokschema.core.MetadataLoader

class MetadataLoaderIt extends FunSuite:
  val config = Config(
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost:5432/test_db",
    "test",
    "test"
  )
  val loader = MetadataLoader(config)

  test("hoge") {
    val refs = loader.loadReferences()
    refs.foreach(println)
  }
