package org.openpreservation.odf.fmt;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

public enum Signatures {
    /**
     * No match found.
     */
    NOMATCH(new byte[0]),
    /**
     * ZIP file signature, all ODF packages are ZIP files.
     */
    ZIP(Utils.hexStringToByteArray("504B0304")),
    /**
     * Signature of an empty zip.
     */
    ZIP_EMPTY(Utils.hexStringToByteArray("504B0506")),
    /**
     * Signature of a spanned, i.e. multi -file zip.
     */
    ZIP_SPANNED(Utils.hexStringToByteArray("504B0708")),
    /**
     * XML UTF-8 signature.
     */
    XML_UTF_8(Utils.hexStringToByteArray("3C3F786D6C20")),
    /**
     * XML UTF-16 Little Endian signature.
     */
    XML_UTF_16_LE(Utils.hexStringToByteArray("3C003F0078006D006C0020")),
    /**
     * XML UTF-16 Big Endian signature.
     */
    XML_UTF_16_BE(Utils.hexStringToByteArray("003C003F0078006D006C0020")),
    /**
     * XML UTF-32 Little Endian signature.
     */
    XML_UTF_32_LE(Utils.hexStringToByteArray("3C0000003F000000780000006D0000006C00000020000000")),
    /**
     * XML UTF-32 Big Endian signature.
     */
    XML_UTF_32_BE(Utils.hexStringToByteArray("0000003C0000003F000000780000006D0000006C00000020")),
    /**
     * Base match of ODF `mimetype` file.
     */
    ODF_MIME(Utils.stringToHexBytes(Constants.MIMETYPE), 30),
    /**
     * ODF Database signature.
     */
    ODB(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_ODB), 30),
    /**
     * ODF Chart signature.
     */
    ODC(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_ODC), 30),
    /**
     * ODF Formula signature.
     */
    ODF(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_ODF), 30),
    /**
     * ODF Graphics signature.
     */
    ODG(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_ODG), 30),
    /**
     * ODF Image signature.
     */
    ODI(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_ODI), 30),
    /**
     * ODF Master Document signature.
     */
    ODM(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_ODM), 30),
    /**
     * ODF Presentation signature.
     */
    ODP(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_ODP), 30),
    /**
     * ODF Spreadsheet signature.
     */
    ODS(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_ODS), 30),
    /**
     * ODF Text signature.
     */
    ODT(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_ODT), 30),
    /**
     * ODF Chart Template signature.
     */
    OTC(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_OTC), 30),
    /**
     * ODF Formula Template signature.
     */
    OTF(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_OTF), 30),
    /**
     * ODF Graphics Template signature.
     */
    OTG(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_OTG), 30),
    /**
     * ODF Text Web signature.
     */
    OTH(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_OTH), 30),
    /**
     * ODF Image Template signature.
     */
    OTI(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_OTI), 30),
    /**
     * ODF Master Document Template signature.
     */
    OTM(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_OTM), 30),
    /**
     * ODF Presentation Template signature.
     */
    OTP(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_OTP), 30),
    /**
     * ODF Spreadsheet Template signature.
     */
    OTS(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_OTS), 30),
    /**
     * ODF Text Template signature.
     */
    OTT(Utils.stringToHexBytes(Constants.MIMETYPE + Constants.MIME_OTT), 30);

    /**
     * The length of the longest signature.
     */
    public static final int MAX_LENGTH = maxLength();

    /**
     * Match a signature at the start of the given byte array.
     * @param signature the signature to match
     * @return the matching signature, or NOMATCH if no match
     */
    public static Signatures match(final byte[] signature) {
        return match(signature, 0);
    }

    /**
     * Match a signature at the given offset in the byte array.
     *
     * @param bytes the byte array to match
     * @param offset the offset in the byte array to start matching
     * @return the matching signature, or NOMATCH if no match
     */
    public static Signatures match(final byte[] bytes, final int offset) {
        return match(bytes, offset, Arrays.asList(Signatures.values()));
    }

    /**
     * Match a signature at the given offset in the byte array, from a given collection of signatures.
     *
     * @param bytes the byte array to search for the signature
     * @param offset the offset in the byte array to start matching
     * @param sigs the collection of signatures to match
     * @return the matching signature, or NOMATCH if no match
     */
    public static Signatures match(final byte[] bytes, final int offset, final Collection<Signatures> sigs) {
        Signatures result = Signatures.NOMATCH;
        for (final Signatures sig : sigs) {
            if (sig.isMatch(bytes, offset)) {
                result = sig;
            }
        }
        return result;
    }

    /**
     * Find all matching signatures in the given byte array. For example an ODF package will match both ZIP and an ODF signature.
     *
     * @param signature the byte array to search for signatures
     * @return a set of all matching signatures
     */
    public static Set<Signatures> matchAll(final byte[] signature) {
        return matchAll(signature, 0);
    }

    /**
     * Find all matching signatures in the given byte array, starting at the given offset.
     *
     * @param bytes the byte array to search for signatures
     * @param offset the offset in the byte array to start matching
     * @return a set of all matching signatures
     */
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
