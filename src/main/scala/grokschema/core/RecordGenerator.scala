package grokschema.core

trait RecordGenerator {

  def generateInsertSql(tables: Seq[Table], trees: Seq[ReferentTree]): Seq[String] =
    val union = trees.map(_.toSet).foldLeft(Set.empty[Referent])(_ ++ _)
    val sorted = union.toSeq.sortBy(_.depth)(Ordering[Int].reverse)
    val tableMap = tables.map(t => t.tableId -> t).toMap
    sorted.map { referent =>
      val table = tableMap(referent.tableId)
      val columns = table.columns.map(_.columnName).mkString(", ")
      val values = table.columns.map(_ => "?").mkString(", ")
      s"INSERT INTO ${table.tableId} ($columns) VALUES ($values)"
    }
}
