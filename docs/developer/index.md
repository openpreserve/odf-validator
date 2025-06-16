# ODF Validation developer documentation

For developers wishing to integrate the ODF Validator into their own applications, the following information may be useful. You'll need to use the odf-core package which is available in Maven Central repositories.

## Including the core validation library

To include the core validation library in your project, add the following dependency to your pom.xml:

```xml
<dependency>
    <groupId>org.openpreservation.odf</groupId>
    <artifactId>odf-core</artifactId>
    <version>0.18.4</version>
</dependency>
```

## Overview of main classes

The library employs a parser that parses ODF packages and creates an internal model of the package. This model is then used by the validator classes to validate the package. Validators effectively test properties of the model against the ODF Specification, or other policies.

## Parsing an ODF package

The library allows a non-validating parse of an ODF package, indeed this is a pre-requisite to valdiation which is performed against a package instance. The following code snippet shows how to parse an ODF package:

```java
import java.nio.file.Path;
import java.io.IOException;
import java.io.InputStream;

import org.openpreservation.odf.pkg.FileEntry;
import org.openpreservation.odf.pkg.Manifest;
import org.openpreservation.odf.pkg.OdfPackage;
import org.openpreservation.odf.pkg.OdfPackages;
import org.openpreservation.odf.pkg.PackageParser;

// Get a package parser instance and parse a package
PackageParser packageParser = OdfPackages.getPackageParser();
OdfPackage odfPackage = packageParser.parsePackage(Path.of("path/to/package.ods"));

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
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
}
```

Note the use of the factory method [`OdfPackages.getPackageParser()`](site/apidocs/org/openpreservation/odf/pkg/OdfPackages.html#getPackageParser()) to obtain a package parser instance. This can be reused to parse multiple packages. You can read more about the ODF Package types in the API documentation:

* The high-level ODF Package interface: [`OdfPackage`](site/apidocs/org/openpreservation/odf/pkg/OdfPackage.html)
* The interface for working with package manifests: [`Manifest`](site/apidocs/org/openpreservation/odf/pkg/Manifest.html)
* An interface for deailing with manifett file entries: [`FileEntry`](site/apidocs/org/openpreservation/odf/pkg/FileEntry.html)

## Validating an ODF package

```java
import java.nio.file.Path;

import org.openpreservation.odf.validation.Check;
import org.openpreservation.odf.validation.OdfValidator;
import org.openpreservation.odf.validation.OdfValidators;
import org.openpreservation.odf.validation.ValidationReport;

// Get a validator instance
OdfValidator validator = OdfValidators.getOdfValidator();

// Validate the file at this path
ValidationReport report = validator.validate(Path.of("path/to/package.ods"));
if (!report.isValid()) {
    List<Check> checks = report.getChecks();
    // Loop through the messages
    for (Check check : checks) {
        // Get the message id
        System.out.println(check.getMessage().getId());
        // Get the message severity (INFO, WARNING, ERROR)
        System.out.println(check.getMessage().getSeverity());
        // Print out the message text
        System.out.println(check.getMessage().getText());
    }
} else {
    System.out.println("The document is valid");
}
```

This time a [`OdfValidator`](site/apidocs/org/openpreservation/odf/validation/OdfValidator.html) instance is used. The [`OdfValidator.validate(Path)`](site/apidocs/org/openpreservation/odf/validation/OdfValidator.html#validate(java.nio.file.Path)) method returns a [`ValidationReport`](site/apidocs/org/openpreservation/odf/validation/ValidationReport.html) instance. This can be used to determine if the package is valid or not. If the package is not valid, the report can be used to obtain the error messages.

## Validation of Spreadsheets Only

The ODF Validator can be used to validate spreadsheets only. This is useful if you want to validate a spreadsheet without having to parse the entire package. The following code snippet shows how to validate a spreadsheet:

```java
import java.nio.file.Path;
import java.util.List;

import org.openpreservation.odf.validation.Check;
import org.openpreservation.odf.validation.ValidationReport;
import org.openpreservation.odf.validation.OdfValidator;

// Get a validator instance
OdfValidator validator = OdfValidators.getOdfValidator();
// Use the validate method that specifies a particular format
ValidationReport report = validator.validate(Path.of("path/to/spreadsheet.ods"), Formats.ODS);
if (!report.isValid()) {
    List<Check> checks = report.getChecks();
    // Loop through the messages
    for (Check check : checks) {
        // Get the message id
        System.out.println(check.getMessage().getId());
        // Get the message severity (INFO, WARNING, ERROR)
        System.out.println(check.getMessage().getSeverity());
        // Print out the message text
        System.out.println(check.getMessage().getText());
    }
} else {
    System.out.println("The spreadsheet is valid");
}
```

## Validation usigng the Preservation Specification profile

There is an alternative profile validator that can run custom rules to check properties that are beyond the scope of the ODF specification. The following code snippet shows how to validate a spreadsheet using the Preservation Specification profile:

```java
import java.nio.file.Path;
import java.util.List;

import org.openpreservation.odf.validation.Check;
import org.openpreservation.odf.validation.Profile;
import org.openpreservation.odf.validation.ValidationReport;
import org.openpreservation.odf.validation.rules.Rules;

// Create a Profile instance for the validator
Profile dnaProfile = Rules.getDnaProfile();
// Create a validator instance
OdfValidator validator = OdfValidators.getOdfValidator();
// Validate the path with the profile using the profile method
ValidationReport report = validator.profile(Path.of("path/to/package.ods"), dnaProfile);
// Check if the result is valid and print out any messages found
if (!report.isValid()) {
    List<Check> checks = report.getChecks();
    // Loop through the messages
    for (Check check : checks) {
        // Get the message id
        System.out.println(check.getMessage().getId());
        // Get the message severity (INFO, WARNING, ERROR)
        System.out.println(check.getMessage().getSeverity());
        // Print out the message text
        System.out.println(check.getMessage().getText());
    }
} else {
    System.out.println("The document is valid");
}
```

## Serialisation of ValidationReports

Once you have a `ValidationReport` it can be converted to XML or JSON using the `org.openpreservation.odf.validation.ValidationReports` class.

```java
import java.nio.file.Path;

import org.openpreservation.odf.validation.OdfValidator;
import org.openpreservation.odf.validation.OdfValidators;
import org.openpreservation.odf.validation.ValidationReport;
import org.openpreservation.odf.validation.ValidationReports;

OdfValidator validator = OdfValidators.getOdfValidator();
ValidationReport report = validator.validate(Path.of("path/to/package.ods"));

// Serialise the report using the ValidationReports method to generate a JSON string and output it
String jsonReport = ValidationReports.reportToJson(report);
System.out.println(jsonReport);

// Serialise the report using the ValidationReports method to generate an XML string and output it
String xmlReport = ValidationReports.reportToXml(report);
System.out.println(xmlReport);
```
