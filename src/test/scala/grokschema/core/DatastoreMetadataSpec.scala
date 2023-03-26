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
        s"$fromColumn-->$toColumn",
        fromTable,
        fromColumn,
        toTable,
        toColumn
      )
    }

    val actual = ReferentTree(refs, Seq("a", "b"))
    val expected = Seq(
      Node(
        "a",
        0,
        Set(
          Node(
            "c",
            1,
            Set(
              Leaf("e", 2)
            )
          ),
          Node(
            "y",
            1,
            Set(
              Leaf("x", 2)
            )
          )
        )
      ),
      Node(
        "b",
        0,
        Set(
          Node(
            "d",
            1,
            Set(
              Leaf("f", 2)
            )
          ),
          Leaf("x", 1)
        )
      )
    )

    assertEquals(actual, expected)
  }
}
