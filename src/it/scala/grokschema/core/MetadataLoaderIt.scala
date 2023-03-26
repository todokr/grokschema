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
        constraintName = "a_c_id_fkey",
        fromTable = TableId("public", "a"),
        fromColumn = "c_id",
        toTable = TableId("public", "c"),
        toColumn = "id"
      ),
      Reference(
        constraintName = "a_y_id_fkey",
        fromTable = TableId("public", "a"),
        fromColumn = "y_id",
        toTable = TableId("public", "y"),
        toColumn = "id"
      ),
      Reference(
        constraintName = "b_d_id_fkey",
        fromTable = TableId("public", "b"),
        fromColumn = "d_id",
        toTable = TableId("public", "d"),
        toColumn = "id"
      ),
      Reference(
        constraintName = "b_x_id_fkey",
        fromTable = TableId("public", "b"),
        fromColumn = "x_id",
        toTable = TableId("public", "x"),
        toColumn = "id"
      ),
      Reference(
        constraintName = "c_e_id_fkey",
        fromTable = TableId("public", "c"),
        fromColumn = "e_id",
        toTable = TableId("public", "e"),
        toColumn = "id"
      ),
      Reference(
        constraintName = "d_f_id_fkey",
        fromTable = TableId("public", "d"),
        fromColumn = "f_id",
        toTable = TableId("public", "f"),
        toColumn = "id"
      ),
      Reference(
        constraintName = "y_x_id_fkey",
        fromTable = TableId("public", "y"),
        fromColumn = "x_id",
        toTable = TableId("public", "x"),
        toColumn = "id"
      )
    )

    assertEquals(actual, expected)
  }
