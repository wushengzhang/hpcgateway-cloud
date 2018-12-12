/* -*-mode:java; c-basic-offset:2; -*- */
/*
 * This program is based on zlib-1.1.3, so all credit should go authors
 * Jean-loup Gailly(jloup@gzip.org) and Mark Adler(madler@alumni.caltech.edu)
 * and contributors of zlib.
 */

package hpcgateway.wp.util.jzlib;

public class GZIPException extends java.io.IOException {
  public GZIPException() {
    super();
  }
  public GZIPException(String s) {
    super(s);
  }
}
