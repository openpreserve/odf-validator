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