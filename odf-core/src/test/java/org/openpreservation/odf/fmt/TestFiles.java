package org.openpreservation.odf.fmt;

import java.net.URL;

public class TestFiles {
    final static String TEST_ROOT = "org/openpreservation/odf/fmt/";
    final static String ZIP_TEST_ROOT = TEST_ROOT + "zip/";
    final static String PKG_TEST_ROOT = TEST_ROOT + "pkg/";
    final static String MIME_TEST_ROOT = PKG_TEST_ROOT + "mimetype/";
    final static String MANIFEST_TEST_ROOT = PKG_TEST_ROOT + "manifest/";
    final static String DOCUMENT_TEST_ROOT = PKG_TEST_ROOT + "document/";
    final static String THUMBNAIL_TEST_ROOT = PKG_TEST_ROOT + "thumbnail/";
    final static String VERSION_TEST_ROOT = PKG_TEST_ROOT + "version/";
    final static String EMBEDDED_TEST_ROOT = PKG_TEST_ROOT + "embedded/";
    final static String ENCRYPTED_TEST_ROOT = PKG_TEST_ROOT + "encrypted/";
    final static String DSIG_TEST_ROOT = PKG_TEST_ROOT + "dsigs/";
    final static String INVALID_PKG_ROOT = PKG_TEST_ROOT + "invalid/";
    final static String RULES_ROOT = PKG_TEST_ROOT + "rules/";
    final static String XML_TEST_ROOT = TEST_ROOT + "xml/";
    final static String FILE_TEST_ROOT = TEST_ROOT + "files/";
    public final static URL EMPTY_ODS = ClassLoader.getSystemResource(ZIP_TEST_ROOT + "empty.ods");
    public final static URL NO_MIME_ROOT_ODS = ClassLoader.getSystemResource(MIME_TEST_ROOT + "no_mime_root.ods");
    public final static URL NO_MIME_NO_ROOT_ODS = ClassLoader.getSystemResource(MIME_TEST_ROOT + "no_mime_no_root.ods");
    public final static URL BADLY_FORMED_PKG = ClassLoader.getSystemResource(PKG_TEST_ROOT + "badly_formed.ods");
    public final static URL MIME_EXTRA_ODS = ClassLoader.getSystemResource(MIME_TEST_ROOT + "mime_extra.ods");
    public final static URL MIME_LAST_ODS = ClassLoader.getSystemResource(MIME_TEST_ROOT + "mime_last.ods");
    public final static URL MIME_COMPRESSED_ODS = ClassLoader.getSystemResource(MIME_TEST_ROOT + "mime_compressed.ods");
    public final static URL MIME_COMPRESSED_LAST_ODS = ClassLoader
            .getSystemResource(MIME_TEST_ROOT + "mime_compressed_last.ods");
    public final static URL NO_MANIFEST_ODS = ClassLoader.getSystemResource(MANIFEST_TEST_ROOT + "no_manifest.ods");
    public final static URL MANIFEST_ROOT_NO_MIME_ODS = ClassLoader
            .getSystemResource(MANIFEST_TEST_ROOT + "manifest_root_no_mime.ods");
    public final static URL MANIFEST_DIFF_MIME_ODS = ClassLoader
            .getSystemResource(MANIFEST_TEST_ROOT + "different_valid_mimes.ods");
    public final static URL MANIFEST_RAND_MIMETYPE_ODS = ClassLoader
            .getSystemResource(MANIFEST_TEST_ROOT + "different_rand_mimetype.ods");
    public final static URL MANIFEST_RAND_ROOT_MIME_ODS = ClassLoader
            .getSystemResource(MANIFEST_TEST_ROOT + "different_rand_mime_manifest.ods");
    public final static URL MANIFEST_NO_ROOT_MIME_ODS = ClassLoader
            .getSystemResource(MANIFEST_TEST_ROOT + "no_root_mime.ods");
    public final static URL MANIFEST_EMPTY_ROOT_MIME_ODS = ClassLoader
            .getSystemResource(MANIFEST_TEST_ROOT + "empty_root_mime.ods");
    public final static URL MANIFEST_NO_ROOT_MIMETYPE_ODS = ClassLoader
            .getSystemResource(MANIFEST_TEST_ROOT + "no_root_with_mimetype.ods");
    public final static URL MANIFEST_ENTRY_ODS = ClassLoader
            .getSystemResource(MANIFEST_TEST_ROOT + "manifest_entry.ods");
    public final static URL MANIFEST_MISSING_ENTRY_ODS = ClassLoader
            .getSystemResource(MANIFEST_TEST_ROOT + "manifest_missing_entry.ods");
    public final static URL MANIFEST_MISSING_XML_ENTRY_ODS = ClassLoader
            .getSystemResource(MANIFEST_TEST_ROOT + "manifest_missing_xml_entry.ods");
    public final static URL MISSING_FILE_ODS = ClassLoader.getSystemResource(MANIFEST_TEST_ROOT + "missing_file.ods");
    public final static URL MIMETYPE_ENTRY_ODS = ClassLoader
            .getSystemResource(MANIFEST_TEST_ROOT + "mimetype_entry.ods");
    public final static URL METAINF_ENTRY_ODT = ClassLoader.getSystemResource(MANIFEST_TEST_ROOT + "metainf_entry.odt");
    public final static URL NO_THUMBNAIL_ODS = ClassLoader.getSystemResource(THUMBNAIL_TEST_ROOT + "fingernail.ods");
    public final static URL VER_1_3_ODS = ClassLoader.getSystemResource(VERSION_TEST_ROOT + "T002.ods");
    public final static URL VER_1_2_ODS = ClassLoader.getSystemResource(VERSION_TEST_ROOT + "T105.ods");
    public final static URL VER_1_1_ODS = ClassLoader.getSystemResource(VERSION_TEST_ROOT + "T104.ods");
    public final static URL EMBEDDED_WORD = ClassLoader
            .getSystemResource(EMBEDDED_TEST_ROOT + "WithEmbeddedwordDoc.ods");
    public final static URL ENCRYPTED_PASSWORDS = ClassLoader
            .getSystemResource(ENCRYPTED_TEST_ROOT + "WithPassword.ods");
    public final static URL EMPTY_FODS = ClassLoader.getSystemResource(XML_TEST_ROOT + "empty.fods");
    public final static URL FLAT_NOT_WF = ClassLoader.getSystemResource(XML_TEST_ROOT + "flat_not_wf.fods");
    public final static URL FLAT_NOT_VALID = ClassLoader.getSystemResource(XML_TEST_ROOT + "flat_not_valid.fods");
    public final static URL FLAT_NO_VERSION = ClassLoader.getSystemResource(XML_TEST_ROOT + "no_version.fods");
    public final static URL FLAT_NO_MIME = ClassLoader.getSystemResource(XML_TEST_ROOT + "no_mimetype.fods");
    public final static URL EMPTY = ClassLoader.getSystemResource(FILE_TEST_ROOT + "empty.file");
    public final static URL STYLES_ONLY_DOC = ClassLoader.getSystemResource(DOCUMENT_TEST_ROOT + "styles_only_doc.ods");
    final static URL K_TEXT = ClassLoader.getSystemResource(FILE_TEST_ROOT + "k.txt");
    final static URL P_TEXT = ClassLoader.getSystemResource(FILE_TEST_ROOT + "p.txt");
    final static URL PK_TEXT = ClassLoader.getSystemResource(FILE_TEST_ROOT + "pk.txt");
    public final static URL FAKEMIME_TEXT = ClassLoader.getSystemResource(FILE_TEST_ROOT + "mimefake.txt");
    final static URL MIMESIG_ODS = ClassLoader.getSystemResource(FILE_TEST_ROOT + "mimesig-ods.txt");
    final static URL MIMESIG_OTS = ClassLoader.getSystemResource(FILE_TEST_ROOT + "mimesig-ots.txt");
    public final static URL DSIG_INVALID = ClassLoader.getSystemResource(DSIG_TEST_ROOT + "dsigs.odt");
    public final static URL DSIG_VALID = ClassLoader.getSystemResource(DSIG_TEST_ROOT + "dsigs-valid.ods");
    public final static URL DSIG_BADNAME = ClassLoader.getSystemResource(DSIG_TEST_ROOT + "bad_dsig_name.ods");
    public final static URL MANIFEST_NOT_WF = ClassLoader.getSystemResource(INVALID_PKG_ROOT + "manifest_not_wf.ods");
    public final static URL ODF4_BAD_EXT = ClassLoader.getSystemResource(RULES_ROOT + "bad_ext_odf4.ext");
    public final static URL ODF4_BAD_MIME = ClassLoader.getSystemResource(RULES_ROOT + "template_sheet_ext_odf4.ods");
    public final static URL SCHEMATRON_CHECKER_XML = ClassLoader
            .getSystemResource(RULES_ROOT + "schematron_checker.xml");
    public final static URL SCHEMATRON_CHECKER_ODS = ClassLoader
            .getSystemResource(RULES_ROOT + "schematron_checker.ods");
    public final static URL MACRO_XML = ClassLoader.getSystemResource(RULES_ROOT + "macro.xml");
    public final static URL MACRO_PACKAGE = ClassLoader.getSystemResource(RULES_ROOT + "macro.ods");
    public final static URL STAR_BASIC = ClassLoader.getSystemResource(RULES_ROOT + "star_basic.ods");
    public final static URL OLE_EMBEDDED_XML = ClassLoader.getSystemResource(RULES_ROOT + "odf_6_embedded.xml");
    public final static URL OLE_EMBEDDED_PACKAGE = ClassLoader.getSystemResource(RULES_ROOT + "odf_6_embedded.ods");
}
