package org.apache.spark.sql.columnar;
// no position
public  class BOOLEAN$ extends org.apache.spark.sql.columnar.NativeColumnType<org.apache.spark.sql.types.BooleanType$> {
  /**
   * Static reference to the singleton instance of this Scala object.
   */
  public static final BOOLEAN$ MODULE$ = null;
  public   BOOLEAN$ () { throw new RuntimeException(); }
  public  void append (boolean v, java.nio.ByteBuffer buffer) { throw new RuntimeException(); }
  public  void append (org.apache.spark.sql.Row row, int ordinal, java.nio.ByteBuffer buffer) { throw new RuntimeException(); }
  public  boolean extract (java.nio.ByteBuffer buffer) { throw new RuntimeException(); }
  public  void extract (java.nio.ByteBuffer buffer, org.apache.spark.sql.catalyst.expressions.MutableRow row, int ordinal) { throw new RuntimeException(); }
  public  void setField (org.apache.spark.sql.catalyst.expressions.MutableRow row, int ordinal, boolean value) { throw new RuntimeException(); }
  public  boolean getField (org.apache.spark.sql.Row row, int ordinal) { throw new RuntimeException(); }
  public  void copyField (org.apache.spark.sql.Row from, int fromOrdinal, org.apache.spark.sql.catalyst.expressions.MutableRow to, int toOrdinal) { throw new RuntimeException(); }
}
