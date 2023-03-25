package grokschema.core

final case class Reference(
    tableSchema: String,
    constraintName: String,
    fromTable: String,
    fromColumn: String,
    toTable: String,
    toColumn: String
):
  override def toString: String =
    s"""[$tableSchema] $toTable.$toColumn <-- $fromTable.$fromColumn ($constraintName)"""

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
      tableName: String,
      columnName: String,
      dataType: String,
      attributes: Set[Attribute]
  )

  object Column:
    enum Attribute(val expr: String):
      case PK extends Attribute("PK")
      case FK extends Attribute("FK")
      case NotNull extends Attribute("not null")

enum ReferentTree:
  case Leaf(table: Table) extends ReferentTree
  case Node(table: Table, referents: Seq[Table]) extends ReferentTree

object ReferentTree:
  def apply(refs: Seq[Reference], root: Table): ReferentTree =
    val referents = refs
      .filter(_.fromTable == root.tableName)
      .map { ref =>
        val referent = refs.find(_.constraintName == ref.constraintName).get
        Table(
          referent.tableSchema,
          referent.toTable,
          Seq.empty
        )
      }
    if referents.isEmpty then Leaf(root)
    else Node(root, referents)
