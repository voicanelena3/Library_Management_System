package library.util;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException; // Import this

/**
 * XmlAdapter for marshalling/unmarshalling LocalDate objects to/from XML strings.
 * JAXB doesn't have built-in support for java.time.LocalDate, so a custom adapter is needed.
 */
public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

    @Override
    public LocalDate unmarshal(String v) throws Exception {
        if (v == null || v.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(v);
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing date string: " + v + " - " + e.getMessage());
            throw new Exception("Invalid date format in XML: " + v, e);
        }
    }

    @Override
    public String marshal(LocalDate v) throws Exception {
        if (v == null) {
            return null;
        }
        return v.toString();
    }
}