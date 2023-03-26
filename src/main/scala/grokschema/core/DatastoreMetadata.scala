package grokschema.core

final case class Reference(
    constraintName: String,
    fromTable: TableId,
    fromColumn: String,
    toTable: TableId,
    toColumn: String
):
  override def toString: String =
    s"""${toTable.tableSchema}.${toTable.tableName}.$toColumn <-- ${fromTable.tableSchema}.${fromTable.tableName}.$fromColumn ($constraintName)"""

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

enum ReferentTree(tableId: TableId, depth: Int):
  case Leaf(tableId: TableId, depth: Int) extends ReferentTree(tableId, depth)
  case Node(tableId: TableId, depth: Int, referents: Set[ReferentTree]) extends ReferentTree(tableId, depth)
  override def toString(): String =
    val indentStr = "- "
    def loop(tree: ReferentTree): String =
      tree match
        case Leaf(tableId, depth) => s"${"  " * depth + indentStr}${tableId.tableSchema}.${tableId.tableName}"
        case Node(tableId, depth, referents) =>
          val referentsStr = referents.map(loop).mkString("\n")
          s"""${"  " * depth + indentStr}${tableId.tableSchema}.${tableId.tableName}
             |$referentsStr""".stripMargin
    loop(this)

object ReferentTree:
  def apply(refs: Set[Reference], rootTables: Seq[TableId]): Seq[ReferentTree] =
    def loop(from: TableId, depth: Int): ReferentTree =
      val referents = refs.filter(_.fromTable == from).map(_.toTable)
      if referents.isEmpty then Leaf(from, depth) else Node(from, depth, referents.map(loop(_, depth + 1)))
    rootTables.map(loop(_, 0))

opaque type TableId = (String, String)
extension (tableId: TableId)
  def tableSchema: String = tableId._1
  def tableName: String = tableId._2
object TableId:
  def apply(tableSchema: String, tableName: String): TableId = (tableSchema, tableName)
