Base64Tool
==========

DESCRIPTION
-----------

This application translates a String into its Base64 encoded version and vice
versa. It knows several variants:

   * **Plain Base64** simply translates the binary encoding of the string into
     a 6-bit character string. It differs from _Standard Base64_ in that no
     padding is applied.
   * **Standard Base64** is based on _Plain Base64_, but adds characters to
     reach a multiple of 24 bit output length. The output conforms to RFC 4648.
   * **MIME** is like _Standard Base64_ but breaks the output into lines of at
     most 76 characters. The output satisfies RFC 2045.
   * **Radix64** is an extension of the _MIME_ encoding. To ensure message
     integrity, it adds, respectively checks an encoded CRC24 checksum at the
     end of the encoded string. This encoding is used for *OpenPGP*.
   * **Base64url** generates a string suitable for URLs. It is unpadded and
     unwrapped, but differs from _Plain Base64_ by using a different mapping.
   * **Base64XML** is unpadded and unwrapped like _Plain Base64_, but uses a
     different mapping to be suitable for XML identifiers.

Note that, while the implemented routines produce output that comply with the
mentioned RFC, it does not follow all of the recommendations when it comes to
treating input. Don't use this application for validation of Base64 encoded
Strings.

REQUIREMENTS
------------

   * Java Runtime Environment 1.8 or higher

_Base64Tool_ is written entirely in Java, it should run on most systems. No
additional libraries are used except for those that come with the JRE. However,
some functions use recently added methods to overcome the limitations of Java
when it comes to unsigned number primitives.

USAGE
-----

Run the `Base64Tool.jar` package, select your encoding flavor and enter your
data. The encoding/decoding direction is determined automatically: the text
field which received the last key stroke is considered as input, the other
one as output.

Error messages should be self explanatory.

DEVELOPMENT
-----------

_Base64Tool_ has been developed using JDK 1.8 and Eclipse Mars for Java. No
further dependencies are needed to build the application.

LICENSE
-------

**Copyright (c) 2016, David Lichti**  
**All rights reserved.**

The _Base64Tool_ is published and licensed under the _BSD-2-Clause_ license. A
copy of the license text can be found in `LICENSE.md`.

AUTHOR
------

David Lichti [dlichtistw@gmx.de]
