package grokschema.core

import munit.*
import grokschema.core.ReferentTree
import grokschema.core.ReferentTree.*

class DatastoreMetadataSpec extends FunSuite {

  test("ReferentTree#apply") {
    val refs = Seq(
      ("A", "a1", "B", "b1"),
      ("A", "a2", "C", "c1"),
      ("B", "b2", "D", "d1"),
      ("C", "c2", "D", "d2"),
      ("C", "c3", "E", "e1"),
      ("C", "c4", "F", "f1"),
      ("E", "e2", "F", "f2")
    ).map { (fromTable, fromColumn, toTable, toColumn) =>
      Reference(
        s"$fromColumn->$toColumn",
        TableId("public", fromTable),
        fromColumn,
        TableId("public", toTable),
        toColumn
      )
    }

    val actual = ReferentTree(refs, Seq(TableId("public", "A")))
    val expected = Seq(
      Node(
        TableId("public", "A"),
        Seq(
          Node(
            TableId("public", "B"),
            Seq(
              Leaf(TableId("public", "D"))
            )
          ),
          Node(
            TableId("public", "C"),
            Seq(
              Leaf(TableId("public", "D")),
              Node(
                TableId("public", "E"),
                Seq(
                  Leaf(TableId("public", "F"))
                )
              ),
              Leaf(TableId("public", "F"))
            )
          )
        )
      )
    )

    assertEquals(actual, expected)
  }
}
