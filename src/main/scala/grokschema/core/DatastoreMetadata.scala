package grokschema.core

/** A value that uniquely identifies the table */
final case class TableId(schema: String, table: String):
  override def toString: String = s"$schema.$table"

/** Represents a data structure of the table */
final case class Table(
    tableId: TableId,
    columns: Set[Column]
):
  override def toString(): String =
    val cols = columns
      .map { col =>
        s"${col.dataType} ${col.columnName} ${col.attributes.map(_.expr).mkString(",")}"
      }
      .mkString("\n")
    s"""${"=" * 30}
         |$tableId
         |${"=" * 30}
         |$cols
         |""".stripMargin

final case class Column(
    columnName: String,
    dataType: String,
    attributes: Set[ColumnAttribute]
)

enum ColumnAttribute(val expr: String):
  case PK extends ColumnAttribute("PK")
  case FK(table: TableId, column: String) extends ColumnAttribute("FK")
  case NotNull extends ColumnAttribute("not null")

/** Represents a reference between tables */
final case class Reference(
    constraintName: String,
    fromTable: TableId,
    fromColumn: String,
    toTable: TableId,
    toColumn: String
):
  override def toString: String =
    s"""$fromTable.$fromColumn --> $toTable.$toColumn ($constraintName)"""

/** Represents a tree of referents */
enum ReferentTree(tableId: TableId, depth: Int):
  case Leaf(tableId: TableId, depth: Int) extends ReferentTree(tableId, depth)
  case Node(tableId: TableId, depth: Int, referents: Set[ReferentTree]) extends ReferentTree(tableId, depth)

  def contains(tableId: TableId): Boolean = this match
    case Leaf(`tableId`, _)         => true
    case Node(`tableId`, _, _)      => true
    case Node(_, _, referents)      => referents.exists(_.contains(tableId))
    case Leaf(_, _) | Node(_, _, _) => false

  def linearize: Seq[Referent] = this match
    case Leaf(tableId, depth) =>
      Seq(Referent(tableId, depth))
    case Node(tableId, depth, referents) =>
      Referent(tableId, depth) +: referents.toSeq.flatMap(_.linearize).sortBy(_.depth)

  override def toString(): String =
    val indentStr = "- "
    def loop(tree: ReferentTree): String =
      tree match
        case Leaf(tableId, depth) => s"${"  " * depth + indentStr}$tableId"
        case Node(tableId, depth, referents) =>
          val referentsStr = referents.map(loop).mkString("\n")
          s"""${"  " * depth + indentStr}$tableId
             |$referentsStr""".stripMargin
    loop(this)

object ReferentTree:
  def apply(refs: Set[Reference], rootTable: TableId): ReferentTree =
    def loop(from: TableId, depth: Int): ReferentTree =
      val referents = refs.filter(_.fromTable == from).map(_.toTable)
      if referents.isEmpty then Leaf(from, depth) else Node(from, depth, referents.map(loop(_, depth + 1)))
    loop(rootTable, 0)

final case class Referent(tableId: TableId, depth: Int)
