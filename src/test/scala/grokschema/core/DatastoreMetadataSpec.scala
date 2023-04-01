package grokschema.core

import munit.*
import grokschema.core.ReferentTree
import grokschema.core.ReferentTree.*

class DatastoreMetadataSpec extends FunSuite {

  test("ReferentTree#apply") {
    val refs = Set(
      ("a", "c_id", "c", "id"),
      ("b", "d_id", "d", "id"),
      ("c", "e_id", "e", "id"),
      ("d", "f_id", "f", "id"),
      ("a", "y_id", "y", "id"),
      ("b", "x_id", "x", "id"),
      ("y", "x_id", "x", "id")
    ).map { (fromTable, fromColumn, toTable, toColumn) =>
      Reference(
        constraintName = s"$fromColumn-->$toColumn",
        fromTable = TableId("public", fromTable),
        fromColumn = fromColumn,
        toTable = TableId("public", toTable),
        toColumn = toColumn
      )
    }

    val targetTables = Seq(TableId("public", "a"), TableId("public", "b"))
    val actual = ReferentTree(refs, targetTables)
    val expected = Seq(
      Node(
        TableId("public", "a"),
        0,
        Set(
          Node(
            TableId("public", "c"),
            1,
            Set(
              Leaf(TableId("public", "e"), 2)
            )
          ),
          Node(
            TableId("public", "y"),
            1,
            Set(
              Leaf(TableId("public", "x"), 2)
            )
          )
        )
      ),
      Node(
        TableId("public", "b"),
        0,
        Set(
          Node(
            TableId("public", "d"),
            1,
            Set(
              Leaf(TableId("public", "f"), 2)
            )
          ),
          Leaf(TableId("public", "x"), 1)
        )
      )
    )

    assertEquals(actual, expected)
  }

  test("ReferentTree#toSeq") {
    val tree = Node(
      TableId("public", "b"),
      0,
      Set(
        Node(
          TableId("public", ""),
          1,
          Set(
            Leaf(TableId("public", "f"), 2)
          )
        ),
        Leaf(TableId("public", "x"), 1)
      )
    )

    // val actual = tree.toSet
  }
}
