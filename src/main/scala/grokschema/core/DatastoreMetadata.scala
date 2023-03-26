package grokschema.core

final case class Reference(
    constraintName: String,
    fromTable: String,
    fromColumn: String,
    toTable: String,
    toColumn: String
):
  override def toString: String =
    s"""$fromTable.$fromColumn --> $toTable.$toColumn ($constraintName)"""

final case class Table(
    tableSchema: String,
    tableName: String,
    columns: Seq[Table.Column]
):
  override def toString(): String =
    val cols = columns
      .map { col =>
        s"${col.dataType} ${col.columnName} ${col.attributes.map(_.expr).mkString(",")}"
      }
      .mkString("\n")
    s"""${"=" * 30}
         |${tableSchema}.${tableName}
         |${"=" * 30}
         |$cols
         |""".stripMargin

object Table:
  import Column.Attribute
  final case class Column(
      columnName: String,
      dataType: String,
      attributes: Set[Attribute]
  )

  object Column:
    enum Attribute(val expr: String):
      case PK extends Attribute("PK")
      case FK extends Attribute("FK")
      case NotNull extends Attribute("not null")

/** Represents a tree of referents */
enum ReferentTree(tableName: String, depth: Int):
  case Leaf(tableName: String, depth: Int) extends ReferentTree(tableName, depth)
  case Node(tableName: String, depth: Int, referents: Set[ReferentTree]) extends ReferentTree(tableName, depth)
  override def toString(): String =
    val indentStr = "- "
    def loop(tree: ReferentTree): String =
      tree match
        case Leaf(tableId, depth) => s"${"  " * depth + indentStr}$tableName"
        case Node(tableId, depth, referents) =>
          val referentsStr = referents.map(loop).mkString("\n")
          s"""${"  " * depth + indentStr}$tableName
             |$referentsStr""".stripMargin
    loop(this)

object ReferentTree:
  def apply(refs: Set[Reference], rootTables: Seq[String]): Seq[ReferentTree] =
    def loop(from: String, depth: Int): ReferentTree =
      val referents = refs.filter(_.fromTable == from).map(_.toTable)
      if referents.isEmpty then Leaf(from, depth) else Node(from, depth, referents.map(loop(_, depth + 1)))
    rootTables.map(loop(_, 0))

final case class TableId(tableSchema: String, tableName: String)
