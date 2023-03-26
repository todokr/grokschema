package grokschema.core

import java.sql.DriverManager
import java.sql.Connection

import scala.util.Using

import grokschema.core.Table.Column
import grokschema.core.Table.Column.Attribute
import java.time.LocalDate

class MetadataLoader(conf: Config):
  import MetadataLoader.*

  Class.forName(conf.driver)

  /** Load table references from the database */
  def loadReferences(): Set[Reference] =
    Using.resource(DriverManager.getConnection(conf.url, conf.user, conf.password)) { conn =>
      val stmt = conn.prepareStatement(ReferencesSql)
      val paramCount = ReferencesSql.count(_ == '?')
      (1 to paramCount).foreach(i => stmt.setString(i, conf.schema))
      val rs = stmt.executeQuery()
      Iterator
        .continually(rs)
        .takeWhile(_.next())
        .map { rs =>
          val from = TableId(rs.getString("table_schema"), rs.getString("from_table"))
          val to = TableId(rs.getString("table_schema"), rs.getString("to_table"))
          Reference(
            rs.getString("constraint_name"),
            from,
            rs.getString("from_column"),
            to,
            rs.getString("to_column")
          )
        }
        .toSet
    }

  /** Load tables and columns from the database */
  def loadTables(): Set[Table] =
    Using.resource(DriverManager.getConnection(conf.url, conf.user, conf.password)) { conn =>
      val stmt = conn.prepareStatement(TableStructureSql)
      val paramCount = TableStructureSql.count(_ == '?')
      (1 to paramCount).foreach(i => stmt.setString(i, conf.schema))
      val rs = stmt.executeQuery()
      val records = Iterator
        .continually(rs)
        .takeWhile(_.next())
        .map { rs =>
          (
            rs.getString("table_schema"),
            rs.getString("table_name"),
            rs.getString("column_name"),
            rs.getString("data_type"),
            rs.getBoolean("is_primary_key"),
            rs.getBoolean("is_nullable")
          )
        }
        .toSeq

      records
        .groupBy { case (schema, table, _, _, _, _) => (schema, table) }
        .map { case ((schema, table), cols) =>
          val columns = cols.map { case (_, tName, cName, tpe, isPk, nullable) =>
            import Attribute.*
            // val isFk = references.refs.find(r => r.fromTable == tName && r.fromColumn == cName).nonEmpty
            val mapping = Set(
              isPk -> PK,
              // isFk -> FK,
              !nullable -> NotNull
            )
            val attrs = mapping.collect { case (cond, attr) if cond => attr }
            Table.Column(cName, tpe, attrs)
          }
          Table(schema, table, columns)
        }
        .toSet
    }

object MetadataLoader:
  private val ReferencesSql: String =
    """select
        |    tc.constraint_name,
        |    tc.table_schema,
        |    ccu.table_name as to_table,
        |    ccu.column_name as to_column,
        |    kcu.table_name as from_table,
        |    kcu.column_name as from_column
        |from information_schema.table_constraints as tc
        |inner join information_schema.constraint_column_usage as ccu on tc.constraint_name = ccu.constraint_name
        |inner join information_schema.key_column_usage as kcu on tc.constraint_name = kcu.constraint_name
        |where
        |    tc.table_schema = ? and
        |    tc.constraint_type = 'FOREIGN KEY'
        |""".stripMargin

  private val TableStructureSql =
    """select c.table_schema,
      |       c.table_name,
      |       c.column_name,
      |       c.data_type,
      |       pk is not null as is_primary_key,
      |       c.is_nullable = 'YES' as is_nullable
      |from information_schema.columns as c
      |         left join (select cc.table_schema,
      |                           cc.table_name,
      |                           cc.column_name,
      |                           cc.constraint_name,
      |                           tc.constraint_type
      |                    from information_schema.constraint_column_usage as cc
      |                             inner join information_schema.table_constraints as tc
      |                                        on tc.constraint_type = 'PRIMARY KEY' and
      |                                           tc.table_schema = ? and
      |                                           cc.table_name = tc.table_name and
      |                                           cc.constraint_name = tc.constraint_name) as pk
      |                   on c.table_schema = pk.table_schema and
      |                      c.table_name = pk.table_name and
      |                      c.column_name = pk.column_name
      |where c.table_schema = ?
      |order by c.table_name, c.ordinal_position""".stripMargin
