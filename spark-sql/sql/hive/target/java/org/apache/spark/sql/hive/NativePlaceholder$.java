package org.apache.spark.sql.hive;
// no position
/**
 * Used when we need to start parsing the AST before deciding that we are going to pass the command
 * back for Hive to execute natively.  Will be replaced with a native command that contains the
 * cmd string.
 */
private  class NativePlaceholder$ extends org.apache.spark.sql.catalyst.plans.logical.Command implements scala.Product, scala.Serializable {
  /**
   * Static reference to the singleton instance of this Scala object.
   */
  public static final NativePlaceholder$ MODULE$ = null;
  public   NativePlaceholder$ () { throw new RuntimeException(); }
}
