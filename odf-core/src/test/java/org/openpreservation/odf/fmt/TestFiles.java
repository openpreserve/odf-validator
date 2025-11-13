package org.openpreservation.odf.fmt;

import java.net.URL;

public class TestFiles {
    static final String TEST_ROOT = "org/openpreservation/odf/fmt/";
    static final String ZIP_TEST_ROOT = TEST_ROOT + "zip/";
    static final String PKG_TEST_ROOT = TEST_ROOT + "pkg/";
    static final String MIME_TEST_ROOT = PKG_TEST_ROOT + "mimetype/";
    static final String MANIFEST_TEST_ROOT = PKG_TEST_ROOT + "manifest/";
    static final String DOCUMENT_TEST_ROOT = PKG_TEST_ROOT + "document/";
    static final String THUMBNAIL_TEST_ROOT = PKG_TEST_ROOT + "thumbnail/";
    static final String VERSION_TEST_ROOT = PKG_TEST_ROOT + "version/";
    static final String EMBEDDED_TEST_ROOT = PKG_TEST_ROOT + "embedded/";
    static final String ENCRYPTED_TEST_ROOT = PKG_TEST_ROOT + "encrypted/";
    static final String EXTENDED_TEST_ROOT = PKG_TEST_ROOT + "extended/";
    static final String DSIG_TEST_ROOT = PKG_TEST_ROOT + "dsigs/";
    static final String INVALID_PKG_ROOT = PKG_TEST_ROOT + "invalid/";
    static final String RULES_ROOT = PKG_TEST_ROOT + "rules/";
    static final String XML_TEST_ROOT = TEST_ROOT + "xml/";
    static final String FILE_TEST_ROOT = TEST_ROOT + "files/";
    public static final URL EMPTY_ODS = ClassLoader.getSystemResource(ZIP_TEST_ROOT + "empty.ods");
    public static final URL NO_MIME_ROOT_ODS = ClassLoader.getSystemResource(MIME_TEST_ROOT + "no_mime_root.ods");
    public static final URL NO_MIME_NO_ROOT_ODS = ClassLoader.getSystemResource(MIME_TEST_ROOT + "no_mime_no_root.ods");
    public static final URL BADLY_FORMED_PKG = ClassLoader.getSystemResource(PKG_TEST_ROOT + "badly_formed.ods");
    public static final URL MIME_EXTRA_ODS = ClassLoader.getSystemResource(MIME_TEST_ROOT + "mime_extra.ods");
    public static final URL MIME_LAST_ODS = ClassLoader.getSystemResource(MIME_TEST_ROOT + "mime_last.ods");
    public static final URL MIME_COMPRESSED_ODS = ClassLoader.getSystemResource(MIME_TEST_ROOT + "mime_compressed.ods");
    public static final URL MIME_COMPRESSED_LAST_ODS = ClassLoader
            .getSystemResource(MIME_TEST_ROOT + "mime_compressed_last.ods");
    public static final URL MANIFEST_RAND_MIMETYPE_ODS = ClassLoader
            .getSystemResource(MANIFEST_TEST_ROOT + "different_rand_mimetype.ods");
    public static final URL NO_MANIFEST_ODS = ClassLoader.getSystemResource(MANIFEST_TEST_ROOT + "no_manifest.ods");
    public static final URL MANIFEST_ROOT_NO_MIME_ODS = ClassLoader
            .getSystemResource(MANIFEST_TEST_ROOT + "manifest_root_no_mime.ods");
    public static final URL MANIFEST_DIFF_MIME_ODS = ClassLoader
            .getSystemResource(MANIFEST_TEST_ROOT + "different_valid_mimes.ods");
    public static final URL MANIFEST_RAND_ROOT_MIME_ODS = ClassLoader
            .getSystemResource(MANIFEST_TEST_ROOT + "different_rand_mime_manifest.ods");
    public static final URL MANIFEST_NO_ROOT_MIME_ODS = ClassLoader
            .getSystemResource(MANIFEST_TEST_ROOT + "no_root_mime.ods");
    public static final URL MANIFEST_EMPTY_ROOT_MIME_ODS = ClassLoader
            .getSystemResource(MANIFEST_TEST_ROOT + "empty_root_mime.ods");
    public static final URL MANIFEST_NO_ROOT_MIMETYPE_ODS = ClassLoader
            .getSystemResource(MANIFEST_TEST_ROOT + "no_root_with_mimetype.ods");
    public static final URL MANIFEST_ENTRY_ODS = ClassLoader
            .getSystemResource(MANIFEST_TEST_ROOT + "manifest_entry.ods");
    public static final URL MANIFEST_MISSING_ENTRY_ODS = ClassLoader
            .getSystemResource(MANIFEST_TEST_ROOT + "manifest_missing_entry.ods");
    public static final URL MANIFEST_MISSING_XML_ENTRY_ODS = ClassLoader
            .getSystemResource(MANIFEST_TEST_ROOT + "manifest_missing_xml_entry.ods");
    public static final URL MISSING_FILE_ODS = ClassLoader.getSystemResource(MANIFEST_TEST_ROOT + "missing_file.ods");
    public static final URL MIMETYPE_ENTRY_ODS = ClassLoader
            .getSystemResource(MANIFEST_TEST_ROOT + "mimetype_entry.ods");
    public static final URL METAINF_ENTRY_ODT = ClassLoader.getSystemResource(MANIFEST_TEST_ROOT + "metainf_entry.odt");
    public static final URL NO_THUMBNAIL_ODS = ClassLoader.getSystemResource(THUMBNAIL_TEST_ROOT + "fingernail.ods");
    public static final URL VER_1_3_ODS = ClassLoader.getSystemResource(VERSION_TEST_ROOT + "T002.ods");
    public static final URL VER_1_2_ODS = ClassLoader.getSystemResource(VERSION_TEST_ROOT + "T105.ods");
    public static final URL VER_1_1_ODS = ClassLoader.getSystemResource(VERSION_TEST_ROOT + "T104.ods");
    public static final URL EMBEDDED_WORD = ClassLoader
            .getSystemResource(EMBEDDED_TEST_ROOT + "WithEmbeddedwordDoc.ods");
    public static final URL ENCRYPTED_PASSWORDS = ClassLoader
            .getSystemResource(ENCRYPTED_TEST_ROOT + "WithPassword.ods");
    public static final URL EXTENDED_SPREADSHEET = ClassLoader
            .getSystemResource(EXTENDED_TEST_ROOT + "lo_ext.ods");

    /**
     * OpenDocument XML test files
     */
    public static final URL EMPTY_FODS = ClassLoader.getSystemResource(XML_TEST_ROOT + "empty.fods");
    public static final URL FLAT_NOT_WF = ClassLoader.getSystemResource(XML_TEST_ROOT + "flat_not_wf.fods");
    public static final URL FLAT_NOT_VALID = ClassLoader.getSystemResource(XML_TEST_ROOT + "flat_not_valid.fods");
    public static final URL FLAT_NO_VERSION = ClassLoader.getSystemResource(XML_TEST_ROOT + "no_version.fods");
    public static final URL FLAT_NO_MIME = ClassLoader.getSystemResource(XML_TEST_ROOT + "no_mimetype.fods");
    public static final URL CONTENT_SVG = ClassLoader.getSystemResource(XML_TEST_ROOT + "content_svg.xml");
    public static final URL LOEXT_EXTENDED_CONFORMANCE = ClassLoader
            .getSystemResource(XML_TEST_ROOT + "loext_ext_cnfrm.xml");
    public static final URL EXTENDED_CONFORMANCE_INVALID = ClassLoader
            .getSystemResource(XML_TEST_ROOT + "extended_invalid.xml");
    public static final URL EXTENDED_CONFORMANCE_VALID = ClassLoader
            .getSystemResource(XML_TEST_ROOT + "extended_valid.xml");

    public static final URL EMPTY = ClassLoader.getSystemResource(FILE_TEST_ROOT + "empty.file");
    public static final URL STYLES_ONLY_DOC = ClassLoader.getSystemResource(DOCUMENT_TEST_ROOT + "styles_only_doc.ods");
    static final URL K_TEXT = ClassLoader.getSystemResource(FILE_TEST_ROOT + "k.txt");
    static final URL P_TEXT = ClassLoader.getSystemResource(FILE_TEST_ROOT + "p.txt");
    static final URL PK_TEXT = ClassLoader.getSystemResource(FILE_TEST_ROOT + "pk.txt");
    public static final URL FAKEMIME_TEXT = ClassLoader.getSystemResource(FILE_TEST_ROOT + "mimefake.txt");
    static final URL MIMESIG_ODS = ClassLoader.getSystemResource(FILE_TEST_ROOT + "mimesig-ods.txt");
    static final URL MIMESIG_OTS = ClassLoader.getSystemResource(FILE_TEST_ROOT + "mimesig-ots.txt");
    public static final URL DSIG_INVALID = ClassLoader.getSystemResource(DSIG_TEST_ROOT + "dsigs.odt");
    public static final URL DSIG_VALID = ClassLoader.getSystemResource(DSIG_TEST_ROOT + "dsigs-valid.ods");
    public static final URL DSIG_BADNAME = ClassLoader.getSystemResource(DSIG_TEST_ROOT + "bad_dsig_name.ods");
    public static final URL MANIFEST_NOT_WF = ClassLoader.getSystemResource(INVALID_PKG_ROOT + "manifest_not_wf.ods");
    public static final URL ODF4_BAD_EXT = ClassLoader.getSystemResource(RULES_ROOT + "bad_ext_odf4.ext");
    public static final URL ODF4_BAD_MIME = ClassLoader.getSystemResource(RULES_ROOT + "template_sheet_ext_odf4.ods");
    public static final URL SCHEMATRON_CHECKER_XML = ClassLoader
            .getSystemResource(RULES_ROOT + "schematron_checker.xml");
    public static final URL SCHEMATRON_CHECKER_ODS = ClassLoader
            .getSystemResource(RULES_ROOT + "schematron_checker.ods");
    public static final URL MACRO_XML = ClassLoader.getSystemResource(RULES_ROOT + "macro.xml");
    public static final URL MACRO_PACKAGE = ClassLoader.getSystemResource(RULES_ROOT + "macro.ods");
    public static final URL STAR_BASIC = ClassLoader.getSystemResource(RULES_ROOT + "star_basic.ods");
    public static final URL OLE_EMBEDDED_XML = ClassLoader.getSystemResource(RULES_ROOT + "odf_6_embedded.xml");
    public static final URL OLE_EMBEDDED_PACKAGE = ClassLoader.getSystemResource(RULES_ROOT + "odf_6_embedded.ods");
}
