package org.jlab.elog;

import java.util.GregorianCalendar;
import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author ryans
 */
final class XMLUtil {

    private static DatatypeFactory typeFactory;

    static {
        try {
            typeFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new IllegalStateException(
                    "Unable to initialize a DatatypeFactory.", e);
        }
    }

    private XMLUtil() {
        // Can't instantiate publicly
    }

    /**
     * Generates a formated DateTime String appropriate for insertion into an
     * XML document.
     *
     * @param calendar The calendar to format
     * @return The formatted String
     */
    public static String toXMLFormat(GregorianCalendar calendar) {
        return typeFactory.newXMLGregorianCalendar(calendar).normalize().
                toXMLFormat();
    }

    /**
     * Generates a calendar from an XML DateTime String.
     *
     * @param xmlDateTime The DateTime String
     * @return The calendar
     */
    public static GregorianCalendar toGregorianCalendar(String xmlDateTime) {
        return typeFactory.newXMLGregorianCalendar(xmlDateTime).normalize().
                toGregorianCalendar();
    }

    /**
     * Encodes an array of bytes to base64.
     * 
     * @param data The bytes
     * @return A base64 encoded String
     */
    public static String encodeBase64(byte[] data) {
        return DatatypeConverter.printBase64Binary(data);
    }
    
    /**
     * Decodes a base64 String to an array of bytes.
     * 
     * @param data The base64 encoded String
     * @return The bytes
     */
    public static byte[] decodeBase64(String data) {
        return DatatypeConverter.parseBase64Binary(data);
    }
    
    /**
     * Create a new element with child text node and append it to the specified
     * parent.
     *
     * @param doc The Document.
     * @param parent The parent
     * @param tagName The new element tag name
     * @param text The child text node value
     * @return The newly created Element
     */
    public static Element appendElementWithText(Document doc, Element parent,
            String tagName, String text) {
        Element child = doc.createElement(tagName);
        parent.appendChild(child);
        child.setTextContent(text);
        //Text textNode = doc.createTextNode(text);
        //child.appendChild(textNode);
        return child;
    }

    /**
     * Creates a new element with child text for a comma separated list of
     * values and appends them to the specified parent.
     *
     * @param doc The Document.
     * @param parent The parent.
     * @param tagName The tag name to use for the new Elements
     * @param list The comma separated values
     */
    public static void appendCommaDelimitedElementsWithText(Document doc,
            Element parent, String tagName, String list) {
        String[] tokens = list.split(",");

        for (String token : tokens) {
            appendElementWithText(doc, parent, tagName, token.trim());
        }
    }

    /**
     * Creates a new element with child and grandchild with text from a comma
     * separated list of values and appends them to the specified parent.
     *
     * @param doc The Document.
     * @param parent The parent.
     * @param childTagName The child tag name.
     * @param grandchildTagName The grandchild tag name.
     * @param list The comma separated values.
     */
    public static void appendCommaDelimitedElementsWithGrandchildAndText(
            Document doc, Element parent, String childTagName,
            String grandchildTagName, String list) {
        String[] tokens = list.split(",");

        for (String token : tokens) {
            Element child = doc.createElement(childTagName);
            parent.appendChild(child);
            appendElementWithText(doc, child, grandchildTagName, token.trim());
        }
    }

    /**
     * Builds a comma separated list of text values from a NodeList of Elements
     * with Text children.
     *
     * @param nodes The Elements with Text children
     * @return The comma separated values
     */
    public static String buildCommaDelimitedFromText(NodeList nodes) {
        StringBuilder csvBuilder = new StringBuilder();

        for (int i = 0; i < nodes.getLength(); i++) {
            csvBuilder.append(nodes.item(i).getTextContent());
            csvBuilder.append(",");
        }

        // Remove trailing comma
        csvBuilder.deleteCharAt(csvBuilder.length() - 1);

        return csvBuilder.toString();
    }

    /**
     * Removes all children from a parent Element.
     *
     * @param parent The parent
     */
    public static void removeChildren(Element parent) {
        while (parent.hasChildNodes()) {
            parent.removeChild(parent.getFirstChild());
        }
    }
    
    /**
     * Returns the first occurrence of a child element with the specified tag
     * name.  Note: only immediate children are candidates (not all 
     * descendents).
     * 
     * @param parent The parent node
     * @param tagName The child tag name
     * @return The child Element or null if none found
     */
    public static Element getChildElementByName(Node parent, String tagName) {
        Element child = null;

        NodeList children = parent.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i) instanceof Element) {
                Element candidate = (Element) children.item(i);
                if (candidate.getTagName().equals(tagName)) {
                    child = candidate;
                    break;
                }
            }
        }

        return child;
    }    
}
