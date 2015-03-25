package org.apache.spark.sql.jdbc;
/**
 * An RDD representing a table in a database accessed via JDBC.  Both the
 * driver code and the workers must be able to access the database; the driver
 * needs to fetch the schema while the workers need to fetch the data.
 */
public  class JDBCRDD extends org.apache.spark.rdd.RDD<org.apache.spark.sql.Row> {
  // no position
  public  class BooleanConversion$ extends org.apache.spark.sql.jdbc.JDBCRDD.JDBCConversion implements scala.Product, scala.Serializable {
    public   BooleanConversion$ () { throw new RuntimeException(); }
  }
  // no position
  public  class DateConversion$ extends org.apache.spark.sql.jdbc.JDBCRDD.JDBCConversion implements scala.Product, scala.Serializable {
    public   DateConversion$ () { throw new RuntimeException(); }
  }
  // no position
  public  class DecimalConversion$ extends org.apache.spark.sql.jdbc.JDBCRDD.JDBCConversion implements scala.Product, scala.Serializable {
    public   DecimalConversion$ () { throw new RuntimeException(); }
  }
  // no position
  public  class DoubleConversion$ extends org.apache.spark.sql.jdbc.JDBCRDD.JDBCConversion implements scala.Product, scala.Serializable {
    public   DoubleConversion$ () { throw new RuntimeException(); }
  }
  // no position
  public  class FloatConversion$ extends org.apache.spark.sql.jdbc.JDBCRDD.JDBCConversion implements scala.Product, scala.Serializable {
    public   FloatConversion$ () { throw new RuntimeException(); }
  }
  // no position
  public  class IntegerConversion$ extends org.apache.spark.sql.jdbc.JDBCRDD.JDBCConversion implements scala.Product, scala.Serializable {
    public   IntegerConversion$ () { throw new RuntimeException(); }
  }
  // no position
  public  class LongConversion$ extends org.apache.spark.sql.jdbc.JDBCRDD.JDBCConversion implements scala.Product, scala.Serializable {
    public   LongConversion$ () { throw new RuntimeException(); }
  }
  // no position
  public  class BinaryLongConversion$ extends org.apache.spark.sql.jdbc.JDBCRDD.JDBCConversion implements scala.Product, scala.Serializable {
    public   BinaryLongConversion$ () { throw new RuntimeException(); }
  }
  // no position
  public  class StringConversion$ extends org.apache.spark.sql.jdbc.JDBCRDD.JDBCConversion implements scala.Product, scala.Serializable {
    public   StringConversion$ () { throw new RuntimeException(); }
  }
  // no position
  public  class TimestampConversion$ extends org.apache.spark.sql.jdbc.JDBCRDD.JDBCConversion implements scala.Product, scala.Serializable {
    public   TimestampConversion$ () { throw new RuntimeException(); }
  }
  // no position
  public  class BinaryConversion$ extends org.apache.spark.sql.jdbc.JDBCRDD.JDBCConversion implements scala.Product, scala.Serializable {
    public   BinaryConversion$ () { throw new RuntimeException(); }
  }
  public abstract class JDBCConversion {
    public   JDBCConversion () { throw new RuntimeException(); }
  }
  /**
   * Maps a JDBC type to a Catalyst type.  This function is called only when
   * the DriverQuirks class corresponding to your database driver returns null.
   * <p>
   * @param sqlType - A field of java.sql.Types
   * @return The Catalyst type corresponding to sqlType.
   */
  static private  org.apache.spark.sql.types.DataType getCatalystType (int sqlType) { throw new RuntimeException(); }
  /**
   * Takes a (schema, table) specification and returns the table's Catalyst
   * schema.
   * <p>
   * @param url - The JDBC url to fetch information from.
   * @param table - The table name of the desired table.  This may also be a
   *   SQL query wrapped in parentheses.
   * <p>
   * @return A StructType giving the table's Catalyst schema.
   * @throws SQLException if the table specification is garbage.
   * @throws SQLException if the table contains an unsupported type.
   */
  static public  org.apache.spark.sql.types.StructType resolveTable (java.lang.String url, java.lang.String table) { throw new RuntimeException(); }
  /**
   * Prune all but the specified columns from the specified Catalyst schema.
   * <p>
   * @param schema - The Catalyst schema of the master table
   * @param columns - The list of desired columns
   * <p>
   * @return A Catalyst schema corresponding to columns in the given order.
   */
  static private  org.apache.spark.sql.types.StructType pruneSchema (org.apache.spark.sql.types.StructType schema, java.lang.String[] columns) { throw new RuntimeException(); }
  /**
   * Given a driver string and an url, return a function that loads the
   * specified driver string then returns a connection to the JDBC url.
   * getConnector is run on the driver code, while the function it returns
   * is run on the executor.
   * <p>
   * @param driver - The class name of the JDBC driver for the given url.
   * @param url - The JDBC url to connect to.
   * <p>
   * @return A function that loads the driver and connects to the url.
   */
  static public  scala.Function0<java.sql.Connection> getConnector (java.lang.String driver, java.lang.String url) { throw new RuntimeException(); }
  /**
   * Build and return JDBCRDD from the given information.
   * <p>
   * @param sc - Your SparkContext.
   * @param schema - The Catalyst schema of the underlying database table.
   * @param driver - The class name of the JDBC driver for the given url.
   * @param url - The JDBC url to connect to.
   * @param fqTable - The fully-qualified table name (or paren'd SQL query) to use.
   * @param requiredColumns - The names of the columns to SELECT.
   * @param filters - The filters to include in all WHERE clauses.
   * @param parts - An array of JDBCPartitions specifying partition ids and
   *    per-partition WHERE clauses.
   * <p>
   * @return An RDD representing "SELECT requiredColumns FROM fqTable".
   */
  static public  org.apache.spark.rdd.RDD<org.apache.spark.sql.Row> scanTable (org.apache.spark.SparkContext sc, org.apache.spark.sql.types.StructType schema, java.lang.String driver, java.lang.String url, java.lang.String fqTable, java.lang.String[] requiredColumns, org.apache.spark.sql.sources.Filter[] filters, org.apache.spark.Partition[] parts) { throw new RuntimeException(); }
  public   JDBCRDD (org.apache.spark.SparkContext sc, scala.Function0<java.sql.Connection> getConnection, org.apache.spark.sql.types.StructType schema, java.lang.String fqTable, java.lang.String[] columns, org.apache.spark.sql.sources.Filter[] filters, org.apache.spark.Partition[] partitions) { throw new RuntimeException(); }
  /**
   * Retrieve the list of partitions corresponding to this RDD.
   */
  public  org.apache.spark.Partition[] getPartitions () { throw new RuntimeException(); }
  /**
   * <code>columns</code>, but as a String suitable for injection into a SQL query.
   */
  private  java.lang.String columnList () { throw new RuntimeException(); }
  /**
   * Turns a single Filter into a String representing a SQL expression.
   * Returns null for an unhandled filter.
   */
  private  java.lang.String compileFilter (org.apache.spark.sql.sources.Filter f) { throw new RuntimeException(); }
  /**
   * <code>filters</code>, but as a WHERE clause suitable for injection into a SQL query.
   */
  private  java.lang.String filterWhereClause () { throw new RuntimeException(); }
  /**
   * A WHERE clause representing both <code>filters</code>, if any, and the current partition.
   */
  private  java.lang.String getWhereClause (org.apache.spark.sql.jdbc.JDBCPartition part) { throw new RuntimeException(); }
  /**
   * Accessor for nested Scala object
   */
  public  org.apache.spark.sql.jdbc.JDBCRDD.BooleanConversion$ BooleanConversion () { throw new RuntimeException(); }
  /**
   * Accessor for nested Scala object
   */
  public  org.apache.spark.sql.jdbc.JDBCRDD.DateConversion$ DateConversion () { throw new RuntimeException(); }
  /**
   * Accessor for nested Scala object
   */
  public  org.apache.spark.sql.jdbc.JDBCRDD.DecimalConversion$ DecimalConversion () { throw new RuntimeException(); }
  /**
   * Accessor for nested Scala object
   */
  public  org.apache.spark.sql.jdbc.JDBCRDD.DoubleConversion$ DoubleConversion () { throw new RuntimeException(); }
  /**
   * Accessor for nested Scala object
   */
  public  org.apache.spark.sql.jdbc.JDBCRDD.FloatConversion$ FloatConversion () { throw new RuntimeException(); }
  /**
   * Accessor for nested Scala object
   */
  public  org.apache.spark.sql.jdbc.JDBCRDD.IntegerConversion$ IntegerConversion () { throw new RuntimeException(); }
  /**
   * Accessor for nested Scala object
   */
  public  org.apache.spark.sql.jdbc.JDBCRDD.LongConversion$ LongConversion () { throw new RuntimeException(); }
  /**
   * Accessor for nested Scala object
   */
  public  org.apache.spark.sql.jdbc.JDBCRDD.BinaryLongConversion$ BinaryLongConversion () { throw new RuntimeException(); }
  /**
   * Accessor for nested Scala object
   */
  public  org.apache.spark.sql.jdbc.JDBCRDD.StringConversion$ StringConversion () { throw new RuntimeException(); }
  /**
   * Accessor for nested Scala object
   */
  public  org.apache.spark.sql.jdbc.JDBCRDD.TimestampConversion$ TimestampConversion () { throw new RuntimeException(); }
  /**
   * Accessor for nested Scala object
   */
  public  org.apache.spark.sql.jdbc.JDBCRDD.BinaryConversion$ BinaryConversion () { throw new RuntimeException(); }
  /**
   * Maps a StructType to a type tag list.
   */
  public  org.apache.spark.sql.jdbc.JDBCRDD.JDBCConversion[] getConversions (org.apache.spark.sql.types.StructType schema) { throw new RuntimeException(); }
  /**
   * Runs the SQL query against the JDBC driver.
   */
  public  java.lang.Object compute (org.apache.spark.Partition thePart, org.apache.spark.TaskContext context) { throw new RuntimeException(); }
}
