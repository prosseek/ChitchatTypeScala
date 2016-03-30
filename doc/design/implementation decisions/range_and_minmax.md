### [2016/03/30]03:16PM

Does this Bit instantiation make sense?

    val a = Bit("a", 5, false, 0, 3)
    val b = Bit("b", 6, false, 0, 2)

Actually, it's OK even though it wastes space.

The encoded value is stored in a byte array (8 bits), and it uses 5 bits
when the range uses only 2 bits (0 - 3).

As a result, the 8 bits are used as follows. I use `X` for patching the
specified location in 8 bits, `F+P` for 5 bits allocation, and `P` for actually
used space.

    XXXFFFPP --- (1)

So, we get the same results with the following by removing the F.

    val a = Bit("a", 2, false, 0, 3)
    XXXXXXPP --- (2)

However, we do not have to care about this, as the false positive checking is in
    bytes boundary, and the range check returns same results for both (1) and (2).