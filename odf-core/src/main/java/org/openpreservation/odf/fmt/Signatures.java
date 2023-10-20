package org.openpreservation.odf.fmt;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

public enum Signatures {
    NOMATCH(new byte[0]),
    ZIP(Utils.hexStringToByteArray("504B0304")),
    ZIP_EMPTY(Utils.hexStringToByteArray("504B0506")),
    ZIP_SPANNED(Utils.hexStringToByteArray("504B0708")),
    XML_UTF_8(Utils.hexStringToByteArray("3C3F786D6C20")),
    XML_UTF_16_LE(Utils.hexStringToByteArray("3C003F0078006D006C0020")),
    XML_UTF_16_BE(Utils.hexStringToByteArray("003C003F0078006D006C0020")),
    XML_UTF_32_LE(Utils.hexStringToByteArray("3C0000003F000000780000006D0000006C00000020000000")),
    XML_UTF_32_BE(Utils.hexStringToByteArray("0000003C0000003F000000780000006D0000006C00000020")),
    ODF_MIME(Utils.stringToHexBytes(Constants.MIMETYPE), 30),
    ODB(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_ODB), 30),
    ODC(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_ODC), 30),
    ODF(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_ODF), 30),
    ODG(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_ODG), 30),
    ODI(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_ODI), 30),
    ODM(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_ODM), 30),
    ODP(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_ODP), 30),
    ODS(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_ODS), 30),
    ODT(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_ODT), 30),
    OTC(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_OTC), 30),
    OTF(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_OTF), 30),
    OTG(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_OTG), 30),
    OTH(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_OTH), 30),
    OTI(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_OTI), 30),
    OTM(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_OTM), 30),
    OTP(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_OTP), 30),
    OTS(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_OTS), 30),
    OTT(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_OTT), 30);

    public static final int MAX_LENGTH = maxLength();


    public static Signatures match(final byte[] signature) {
        return match(signature, 0);
    }

    public static Signatures match(final byte[] bytes, final int offset) {
        return match(bytes, offset, Arrays.asList(Signatures.values()));
    }

    public static Signatures match(final byte[] bytes, final int offset, final Collection<Signatures> sigs) {
        Signatures result = Signatures.NOMATCH;
        for (final Signatures sig : sigs) {
            if (sig.isMatch(bytes, offset)) {
                result = sig;
            }
        }
        return result;
    }

    public static Set<Signatures> matchAll(final byte[] signature) {
        return matchAll(signature, 0);
    }

    public static Set<Signatures> matchAll(final byte[] bytes, final int offset) {
        return matchAll(bytes, offset, Arrays.asList(Signatures.values()));
    }

    private static Set<Signatures> matchAll(final byte[] bytes, final int offset, final Collection<Signatures> sigs) {
        final Set<Signatures> result = EnumSet.noneOf(Signatures.class);
        for (final Signatures sig : sigs) {
            if (sig == Signatures.NOMATCH) {
                continue;
            }
            if (sig.isMatch(bytes, offset)) {
                result.add(sig);
            }
        }
        return result;
    }

    private static int maxLength() {
        int max = 0;
        for (final Signatures sig : Signatures.values()) {
            if (sig.getLength() + sig.offset > max) {
                max = sig.signature.length + sig.offset;
            }
        }
        return max;
    }

    private final byte[] signature;

    public final int offset;

    private Signatures(final byte[] signature) {
        this(signature, 0);
    }

    private Signatures(final byte[] signature, final int offset) {
        this.signature = signature;
        this.offset = offset;
    }

    public int getLength() {
        return signature.length;
    }

    public byte[] getSignature() {
        return signature.clone();
    }

    public boolean isMatch(final byte[] bytes) {
        return this.isMatch(bytes, 0);
    }

    public boolean isMatch(final byte[] bytes, final int offset) {
        return (signature.length <= (bytes.length - offset - this.offset)
                && (Arrays.equals(signature,
                        Arrays.copyOfRange(bytes, this.offset + offset,
                                signature.length + offset + this.offset))));
    }
}
