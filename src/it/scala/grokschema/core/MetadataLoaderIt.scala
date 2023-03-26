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
        fromTable = "a",
        fromColumn = "c_id",
        toTable = "c",
        toColumn = "id"
      ),
      Reference(
        constraintName = "a_y_id_fkey",
        fromTable = "a",
        fromColumn = "y_id",
        toTable = "y",
        toColumn = "id"
      ),
      Reference(
        constraintName = "b_d_id_fkey",
        fromTable = "b",
        fromColumn = "d_id",
        toTable = "d",
        toColumn = "id"
      ),
      Reference(
        constraintName = "b_x_id_fkey",
        fromTable = "b",
        fromColumn = "x_id",
        toTable = "x",
        toColumn = "id"
      ),
      Reference(
        constraintName = "c_e_id_fkey",
        fromTable = "c",
        fromColumn = "e_id",
        toTable = "e",
        toColumn = "id"
      ),
      Reference(
        constraintName = "d_f_id_fkey",
        fromTable = "d",
        fromColumn = "f_id",
        toTable = "f",
        toColumn = "id"
      ),
      Reference(
        constraintName = "y_x_id_fkey",
        fromTable = "y",
        fromColumn = "x_id",
        toTable = "x",
        toColumn = "id"
      )
    )

    assertEquals(actual, expected)
  }
