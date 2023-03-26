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

  test("MetadataLoader#loadReferences") {
    val actual = loader.loadReferences()
    val expected = Set(
      Reference(
        "c_id-->id",
        TableId("public", "a"),
        "c_id",
        TableId("public", "c"),
        "id"
      ),
      Reference(
        "d_id-->id",
        TableId("public", "b"),
        "d_id",
        TableId("public", "d"),
        "id"
      ),
      Reference(
        "e_id-->id",
        TableId("public", "c"),
        "e_id",
        TableId("public", "e"),
        "id"
      ),
      Reference(
        "f_id-->id",
        TableId("public", "d"),
        "f_id",
        TableId("public", "f"),
        "id"
      ),
      Reference(
        "y_id-->id",
        TableId("public", "a"),
        "y_id",
        TableId("public", "y"),
        "id"
      ),
      Reference(
        "x_id-->id",
        TableId("public", "b"),
        "x_id",
        TableId("public", "x"),
        "id"
      ),
      Reference(
        "x_id-->id",
        TableId("public", "y"),
        "x_id",
        TableId("public", "x"),
        "id"
      )
    )

    assertEquals(actual, expected)
  }
