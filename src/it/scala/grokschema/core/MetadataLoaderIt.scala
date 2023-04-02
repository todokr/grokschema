package grokschema.core

import munit.*
import grokschema.core.*

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

  test("MetadataLoader#loadTables") {
    val refs = Set(
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
    import ColumnAttribute.*

    val actual = loader.loadTables(refs)
    val expected: Set[Table] = Set(
      Table(
        tableId = TableId("public", "a"),
        columns = Set(
          Column("id", "integer", Set(PK, NotNull)),
          Column("c_id", "integer", Set(FK(TableId("public", "c"), "id"), NotNull)),
          Column("y_id", "integer", Set(FK(TableId("public", "y"), "id"), NotNull))
        )
      ),
      Table(
        tableId = TableId("public", "b"),
        columns = Set(
          Column("id", "integer", Set(PK, NotNull)),
          Column("d_id", "integer", Set(FK(TableId("public", "d"), "id"), NotNull)),
          Column("x_id", "integer", Set(FK(TableId("public", "x"), "id"), NotNull))
        )
      ),
      Table(
        tableId = TableId("public", "c"),
        columns = Set(
          Column("id", "integer", Set(PK, NotNull)),
          Column("e_id", "integer", Set(FK(TableId("public", "e"), "id"), NotNull))
        )
      ),
      Table(
        tableId = TableId("public", "d"),
        columns = Set(
          Column("id", "integer", Set(PK, NotNull)),
          Column("f_id", "integer", Set(FK(TableId("public", "f"), "id"), NotNull))
        )
      ),
      Table(
        tableId = TableId("public", "e"),
        columns = Set(
          Column("id", "integer", Set(PK, NotNull))
        )
      ),
      Table(
        tableId = TableId("public", "f"),
        columns = Set(
          Column("id", "integer", Set(PK, NotNull))
        )
      ),
      Table(
        tableId = TableId("public", "x"),
        columns = Set(
          Column("id", "integer", Set(PK, NotNull))
        )
      ),
      Table(
        tableId = TableId("public", "y"),
        columns = Set(
          Column("id", "integer", Set(PK, NotNull)),
          Column("x_id", "integer", Set(FK(TableId("public", "x"), "id"), NotNull))
        )
      )
    )

    assertEquals(actual, expected)
  }
