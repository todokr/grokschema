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

    val targetTable = TableId("public", "a")
    val actual = ReferentTree(refs, targetTable)
    val expected =
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
      )

    assertEquals(actual, expected)
  }

  test("ReferentTree#linearize") {
    val tree = Node(
      TableId("public", "a"),
      0,
      Set(
        Node(
          TableId("public", "b"),
          1,
          Set(
            Leaf(TableId("public", "f"), 2)
          )
        ),
        Leaf(TableId("public", "x"), 1)
      )
    )

    val actual = tree.linearize
    assertEquals(actual.size, 4)

    val actualOrder = actual.map(_.depth)

    assertEquals(actualOrder, actualOrder.sorted)
  }

  test("ReferentTree#contains - true") {
    val tree = Node(
      TableId("public", "a"),
      0,
      Set(
        Node(
          TableId("public", "b"),
          1,
          Set(
            Leaf(TableId("public", "f"), 2)
          )
        ),
        Leaf(TableId("public", "x"), 1)
      )
    )

    val actual = tree.contains(TableId("public", "f"))
    val expected = true

    assertEquals(actual, expected)
  }

  test("ReferentTree#contains - false") {
    val tree = Node(
      TableId("public", "a"),
      0,
      Set(
        Node(
          TableId("public", "b"),
          1,
          Set(
            Leaf(TableId("public", "f"), 2)
          )
        ),
        Leaf(TableId("public", "x"), 1)
      )
    )

    val actual = tree.contains(TableId("public", "z"))
    val expected = false

    assertEquals(actual, expected)
  }
}
