/* -*-mode:java; c-basic-offset:2; -*- */
/*
 * This program is based on zlib-1.1.3, so all credit should go authors
 * Jean-loup Gailly(jloup@gzip.org) and Mark Adler(madler@alumni.caltech.edu)
 * and contributors of zlib.
 */

package hpcgateway.wp.util.jzlib;

public class ZStreamException extends java.io.IOException {
  public ZStreamException() {
    super();
  }
  public ZStreamException(String s) {
    super(s);
  }
}
