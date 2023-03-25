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
        "public",
        s"$fromColumn->$toColumn",
        fromTable,
        fromColumn,
        toTable,
        toColumn
      )
    }

    val actual = ReferentTree(refs, Seq("A"))
    val expected = Seq(
      Node(
        "A",
        Seq(
          Node(
            "B",
            Seq(
              Leaf("D")
            )
          ),
          Node(
            "C",
            Seq(
              Leaf("D"),
              Node(
                "E",
                Seq(
                  Leaf("F")
                )
              ),
              Leaf("F")
            )
          )
        )
      )
    )

    assertEquals(actual, expected)
  }
}
