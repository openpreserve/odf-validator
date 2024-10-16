package org.openpreservation.odf.apps;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import picocli.CommandLine.IVersionProvider;

public class BuildVersionProvider implements IVersionProvider {
    private static final String RAW_DATE_FORMAT = "${maven.build.timestamp.format}";

    @Override
    public String[] getVersion() throws Exception {
        Properties props = fromResource("org/openpreservation/odf/apps/build.properties");
        String name = props.getProperty("project.name"); //$NON-NLS-1$
        String version = props.getProperty("release.version"); //$NON-NLS-1$
        String dateFormat = props.getProperty("date.format"); //$NON-NLS-1$
        Date date = new Date();
        if (!dateFormat.equals(RAW_DATE_FORMAT)) {
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
            try {
                date = formatter.parse(props.getProperty("release.date")); //$NON-NLS-1$
            } catch (ParseException e) {
                /**
                 * Safe to ignore this exception as release simply set to new
                 * date.
                 */
            }
        }
        return new String[] { name + " v" + version, //$NON-NLS-1$
                "Built: " + new SimpleDateFormat("yyyy-MM-dd").format(date) }; //$NON-NLS-1$
    }

    private static Properties fromResource(final String resourceName) {
        try (InputStream is = BuildVersionProvider.class.getClassLoader().getResourceAsStream(resourceName)) {
            Properties props = new Properties();
            if (is != null) {
                props.load(is);
            }
            return props;
        } catch (IOException excep) {
            throw new IllegalStateException("Couldn't load resource:" + resourceName, excep); //$NON-NLS-1$
        }
    }

}
