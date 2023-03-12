package org.openpreservation.odf.fmt;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

import org.openpreservation.utils.Checks;

public enum Signatures {
    NOMATCH(new byte[0]),
    ZIP(Checks.hexStringToByteArray("504B0304")),
    ZIP_EMPTY(Checks.hexStringToByteArray("504B0506")),
    ZIP_SPANNED(Checks.hexStringToByteArray("504B0708")),
    XML_UTF_8(Checks.hexStringToByteArray("3C3F786D6C20")),
    XML_UTF_16_LE(Checks.hexStringToByteArray("3C003F0078006D006C0020")),
    XML_UTF_16_BE(Checks.hexStringToByteArray("003C003F0078006D006C0020")),
    XML_UTF_32_LE(Checks.hexStringToByteArray("3C0000003F000000780000006D0000006C00000020000000")),
    XML_UTF_32_BE(Checks.hexStringToByteArray("0000003C0000003F000000780000006D0000006C00000020")),
    ODS(Checks.hexStringToByteArray(
            "6D696D65747970656170706C69636174696F6E2F766E642E6F617369732E6F70656E646F63756D656E742E7370726561647368656574"),
            30),
    ODT(Checks.hexStringToByteArray(
            "6D696D65747970656170706C69636174696F6E2F766E642E6F617369732E6F70656E646F63756D656E742E73707265616473686565742D74656D706C6174650D0A"),
            30);

    private final byte[] signature;
    public final int offset;

    private Signatures(final byte[] signature) {
        this(signature, 0);
    }

    private Signatures(final byte[] signature, final int offset) {
        this.signature = signature;
        this.offset = offset;
    }

    static final Set<Signatures> ZIPSSIGS = EnumSet.of(ZIP, ZIP_EMPTY, ZIP_SPANNED);
    static final Set<Signatures> XMLSIGS = EnumSet.of(XML_UTF_8, XML_UTF_16_LE, XML_UTF_16_BE, XML_UTF_32_LE,
            XML_UTF_32_BE);
    static final Set<Signatures> ODFSIGS = EnumSet.of(ODS, ODT);

    public int getLength() {
        return signature.length;
    }

    public static Signatures match(final byte[] signature) {
        return match(signature, 0);
    }

    public static Signatures match(final byte[] bytes, int offest) {
        Signatures result = Signatures.NOMATCH;
        for (Signatures sig : Signatures.values()) {
            if (sig.signature.length <= (bytes.length - offest - sig.offset)
                    && (Arrays.equals(sig.signature,
                            Arrays.copyOfRange(bytes, sig.offset + offest, sig.signature.length + offest + sig.offset)))) {
                result = sig;
            }
        }
        return result;
    }
}
