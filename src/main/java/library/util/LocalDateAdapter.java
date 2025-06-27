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
        // When reading from XML, convert the String back to LocalDate
        if (v == null || v.trim().isEmpty()) {
            return null; // Handle empty or null strings
        }
        try {
            return LocalDate.parse(v);
        } catch (DateTimeParseException e) {
            // Log or rethrow a more specific exception if needed
            System.err.println("Error parsing date string: " + v + " - " + e.getMessage());
            throw new Exception("Invalid date format in XML: " + v, e);
        }
    }

    @Override
    public String marshal(LocalDate v) throws Exception {
        // When writing to XML, convert the LocalDate to a String
        if (v == null) {
            return null; // Handle null LocalDate
        }
        return v.toString();
    }
}