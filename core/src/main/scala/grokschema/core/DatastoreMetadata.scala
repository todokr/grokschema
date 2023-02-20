package grokschema.core

class References(val refs: Seq[Reference]):
  def dependencies(from: String): Seq[String] =
    val fromRef = refs.find(_.fromTable == from)
    val linear =
      Iterator
        .iterate(fromRef)(_.flatMap(r => refs.find(_.fromTable == r.toTable)))
        .takeWhile(x => x.nonEmpty && x.exists(r => r.fromTable != r.toTable))
        .collect { case Some(x) => x }
        .toSeq
    linear.head.fromTable +: linear.map(_.toTable)

  override def toString: String =
    refs
      .map { ref =>
        s"${ref.fromTable}.${ref.fromColumn} --(${ref.constraintName})-> ${ref.toTable}.${ref.toColumn}"
      }
      .mkString("\n")

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

class Schema(tbls: Seq[Table]):

  override def toString: String =
    tbls
      .map { t =>
        val cols = t.columns
          .map { col =>
            s"${col.dataType} ${col.columnName} ${col.attributes.map(_.expr).mkString(",")}"
          }
          .mkString("\n")
        s"""${"=" * 30}
         |${t.tableSchema}.${t.tableName}
         |${"=" * 30}
         |$cols
         |""".stripMargin
      }
      .mkString("\n")

final case class Table(
    tableSchema: String,
    tableName: String,
    columns: Seq[Table.Column]
)

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
