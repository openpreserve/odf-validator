# ODF Validation developer documentation

For developers wishing to integrate the ODF Validator into their own applications, the following information may be useful. You'll need to use the odf-core package which is currently in the OPF's Maven repository.

## Setting up the Maven repository

For now the Maven artefacts are hosted on the OPF's artifactory server. To use them you'll need to add the following to your Maven setting file (usually ~/.m2/settings.xml):

```xml
<settings xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.1.0 http://maven.apache.org/xsd/settings-1.1.0.xsd" xmlns="http://maven.apache.org/SETTINGS/1.1.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <profiles>
    <profile>
      <repositories>
        <repository>
          <snapshots>
            <enabled>false</enabled>
          </snapshots>
          <id>central</id>
          <name>opf-dev</name>
          <url>https://artifactory.openpreservation.org/artifactory/opf-dev</url>
        </repository>
        <repository>
          <snapshots />
          <id>snapshots</id>
          <name>opf-dev</name>
          <url>https://artifactory.openpreservation.org/artifactory/opf-dev</url>
        </repository>
      </repositories>
      <id>artifactory</id>
    </profile>
  </profiles>
  <activeProfiles>
    <activeProfile>artifactory</activeProfile>
  </activeProfiles>
</settings>
```

## Including the core validation library

To include the core validation library in your project, add the following dependency to your pom.xml:

```xml
<dependency>
    <groupId>org.openpreservation.odf</groupId>
    <artifactId>odf-core</artifactId>
    <version>${project.version}</version>
</dependency>
```

## Overview of main classes

The library employs a parser that parses ODF packages and creates an internal model of the package. This model is then used by the validator classes to validate the package. Validators effectively test properties of the model against the ODF Specification, or other policies.

## Parsing an ODF package

The library allows a non-validating parse of an ODF package, indeed this is a pre-requisite to valdiation which is performed against a package instance. The following code snippet shows how to parse an ODF package:

```java
import org.openpreservation.odf.pkg.FileEntry;
import org.openpreservation.odf.pkg.Manifest;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.OdfPackages;
import org.openpreservation.odf.pkg.PackageParser;

// Get a package parser instance
PackageParser packageParser = OdfPackages.getPackageParser();

File packageFile = new File("path/to/package.ods");
OdfPackage odfPackage = packageParser.parsePackage(packageFile);

// Get the package manifest
Manifest manifest = odfPackage.getManifest();

// Get the file entries from the manifest
for (FileEntry entry : manifest.getEntries()) {
    // Get the entry declared MIME type
    String mediaType = entry.getMediaType();
    // Get the entry declared full path
    String fullPath = entry.getFullPath();
    // Get the entry Input Stream
    try (InputStream is = odfPackage.getEntryStream(entry)) {
        // Do something with the entry
    }
}
```

Note the use of the factory method [`OdfPackages.getPackageParser()`](site/apidocs/org/openpreservation/odf/pkg/OdfPackages.html#getPackageParser()) to obtain a package parser instance. This can be reused to parse multiple packages. You can read more about the ODF Package types in the API documentation:

* The high-level ODF Package interface: [`OdfPackage`](site/apidocs/org/openpreservation/odf/pkg/OdfPackage.html)
* The interface for working with package manifests: [`Manifest`](site/apidocs/org/openpreservation/odf/pkg/Manifest.html)
* An interface for deailing with manifett file entries: [`FileEntry`](site/apidocs/org/openpreservation/odf/pkg/FileEntry.html)

## Validating an ODF package

```java
import org.openpreservation.messages.Message;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.validation.ValidatingParser;
import org.openpreservation.odf.validation.ValidationReport;
import org.openpreservation.odf.validation.Validators;

ValidatingParser packageParser = Validators.getValidatingParser();

File packageFile = new File("path/to/package.ods");

// Get the OdfPackage instance from the parser
OdfPackage odfPackage = packageParser.parsePackage(packageFile.toPath());

// Now validate the package and get the validation report
ValidationReport report = packageParser.validatePackage(odfPackage);

// Is the package valid?
if (report.isValid()) {
    System.out.println("Package is valid");
    // Get any warnings or info message (no errors as the package is valid)
    List<Message> messages = report.getMessages();
    // Loop through the messages
    for (Message message : messages) {
        // Get the message id
        System.out.println(message.getId());
        // Get the message severity (INFO, WARNING, ERROR)
        System.out.println(message.getSeverity());
        // Print out the message text
        System.out.println(message.getMessage());
    }
} else {
    System.out.println("Package is not valid");
    // Get the error messages
    List<Message> messages = report.getErrors();
    for (Message message : messages) {
        // Get the message id
        System.out.println(message.getId());
        // Print out the message text
        System.out.println(message.getMessage());
    }
}
```

This time a [`ValidatingParser`](site/apidocs/org/openpreservation/odf/validation/ValidatingParser.html) instance is used. The [`ValidatingParser.validatePackage(OdfPackage)`](site/apidocs/org/openpreservation/odf/validation/ValidatingParser.html#validatePackage(org.openpreservation.odf.pkg.OdfPackage)) method returns a [`ValidationReport`](site/apidocs/org/openpreservation/odf/validation/ValidationReport.html) instance. This can be used to determine if the package is valid or not. If the package is not valid, the report can be used to obtain the error messages.

## Validation of Spreadsheets Only

The ODF Validator can be used to validate spreadsheets only. This is useful if you want to validate a spreadsheet without having to parse the entire package. The following code snippet shows how to validate a spreadsheet:

```java
import org.openpreservation.messages.Message;
import org.openpreservation.odf.validation.ValidationReport;
import org.openpreservation.odf.validation.Validator;

Validator validator = new Validator();
ValidationReport report = validator.validateSpreadsheet(new File("path/to/package.ods"));
if (!report.isValid()) {
    List<Message> messages = report.getMessages();
    // Loop through the messages
    for (Message message : messages) {
        // Get the message id
        System.out.println(message.getId());
        // Get the message severity (INFO, WARNING, ERROR)
        System.out.println(message.getSeverity());
        // Print out the message text
        System.out.println(message.getMessage());
    }
} else {
    System.out.println("The document is valid");
}
```

## Validation usigng the Preservation Specification profile

There is an alternative profile validator that can run custom rules to check properties that are beyond the scope of the ODF specification. The following code snippet shows how to validate a spreadsheet using the Preservation Specification profile:

```java

Profile dnaProfile = Rules.getDnaProfile();
ProfileResult result = dnaProfile.check(parser.parsePackage(Path.of("path/to/package.ods")));
// Check if the result is valid and print out any messages found
if (!result.isValid()) {
    List<Message> messages = report.getProfileMessages().getMessages();
    // Loop through the messages
    for (Message message : messages) {
        // Get the message id
        System.out.println(message.getId());
        // Get the message severity (INFO, WARNING, ERROR)
        System.out.println(message.getSeverity());
        // Print out the message text
        System.out.println(message.getMessage());
    }
} else {
    System.out.println("The document is valid");
}
```

